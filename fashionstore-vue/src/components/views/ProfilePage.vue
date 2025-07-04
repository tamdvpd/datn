<template>
  <MainHeader />
  <div class="p-4 d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div style="width: 100%; max-width: 600px;">
      <h2 class="mb-3 text-center">👤 Thông tin cá nhân</h2>
      <p class="text-center">Quản lý và chỉnh sửa thông tin tài khoản của bạn.</p>

      <div class="text-center mb-4 position-relative" style="cursor: pointer;">
  <img 
    :src="user.imageUrl || require('@/assets/img/default-avatar.png')" 
    alt="Avatar" 
    class="rounded-circle border" 
    width="120" height="120"
    @click="triggerFileSelect" 
  />
  <input 
    type="file" 
    ref="fileInput" 
    accept="image/*" 
    @change="handleImageChange" 
    style="display: none;" 
  />
  <div class="text-muted mt-2">Click vào ảnh để thay đổi</div>
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

function handleImageChange(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      user.value.imageUrl = e.target.result; // Hiển thị ảnh mới ngay
    };
    reader.readAsDataURL(file);
  }
}

function updateProfile() {
  axios.put(`http://localhost:8080/users/${user.value.id}/update`, user.value)
    .then(() => {
      alert('Cập nhật thông tin thành công!');
      localStorage.setItem('user', JSON.stringify(user.value));
    })
    .catch((error) => {
      console.error(error);
      alert('Cập nhật thất bại!');
    });
}
</script>


<style scoped>
.card {
  border-radius: 12px;
}
</style>
