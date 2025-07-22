<template>
  <MainHeader />
  <div class="p-4 d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div style="width: 100%; max-width: 600px;">
      <h2 class="mb-3 text-center">👤 Thông tin cá nhân</h2>
      <p class="text-center">Quản lý và chỉnh sửa thông tin tài khoản của bạn.</p>

      <div class="text-center mb-4 position-relative">
        <img 
          :src="user.imageUrl || require('@/assets/img/default-avatar.png')" 
          alt="Avatar" 
          class="rounded-circle border" 
          width="120" height="120"
        />
        
        <div class="mt-2">
          <button class="btn btn-outline-primary btn-sm me-2" @click="triggerFileSelect">
            Chọn ảnh từ máy
          </button>
          <button class="btn btn-outline-secondary btn-sm" @click="enterImageUrl">
            Nhập URL ảnh
          </button>
        </div>

        <input 
          type="file" 
          ref="fileInput" 
          accept="image/*" 
          @change="handleImageChange" 
          style="display: none;" 
        />
      </div>

      <div class="card shadow-sm">
        <div class="card-header bg-info text-white">
          <h5 class="mb-0">Cập nhật thông tin cá nhân</h5>
        </div>
        <div class="card-body">
          <form @submit.prevent="updateProfile">
            <div class="mb-3">
              <label class="form-label">Họ tên</label>
              <input v-model="user.fullName" type="text" class="form-control" required />
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input v-model="user.email" type="email" class="form-control" disabled />
            </div>
            <div class="mb-3">
              <label class="form-label">Số điện thoại</label>
              <input v-model="user.phoneNumber" type="text" class="form-control" />
            </div>
            <div class="mb-3">
              <label class="form-label">Địa chỉ</label>
              <input v-model="user.address" type="text" class="form-control" />
            </div>
            <div class="text-end">
              <button type="submit" class="btn btn-success">
                <i class="bi bi-check-circle me-1"></i> Cập nhật
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <MainFooter />
</template>
<script setup>
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const user = ref({});
const fileInput = ref(null);

onMounted(() => {
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    user.value = JSON.parse(storedUser);
  } else {
    router.push('/login');
  }
});

function triggerFileSelect() {
  fileInput.value.click();
}

function uploadAvatar(file) {
  const formData = new FormData();
  formData.append('file', file);

  return axios.post('http://localhost:8080/upload/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
}

function handleImageChange(event) {
  const file = event.target.files[0];
  if (file) {
    uploadAvatar(file)
      .then(res => {
        user.value.imageUrl = res.data.url;
      })
      .catch(err => {
        console.error('Lỗi upload ảnh:', err);
        alert('Tải ảnh thất bại, vui lòng thử lại.');
      });
  }
}

function enterImageUrl() {
  const url = prompt("Nhập URL ảnh:");
  if (url && url.startsWith("http")) {
    user.value.imageUrl = url;
  } else if (url) {
    alert("URL không hợp lệ, vui lòng nhập lại.");
  }
}

function updateProfile() {
  const payload = {
    fullName: user.value.fullName,
    phoneNumber: user.value.phoneNumber,
    address: user.value.address,
    imageUrl: user.value.imageUrl
  };

  axios.put(`http://localhost:8080/users/${user.value.id}/update`, payload)
    .then(() => {
      alert('Cập nhật thành công!');
      Object.assign(user.value, payload);
      localStorage.setItem('user', JSON.stringify(user.value));
    })
    .catch((error) => {
      console.error('Lỗi chi tiết:', error.response?.data || error);
      alert('Cập nhật thất bại!');
    });
}
</script>
