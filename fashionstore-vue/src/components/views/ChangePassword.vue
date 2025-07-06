<template>
  <div class="auth-container">
    <div class="auth-card">
      <h3 class="text-center mb-4">Đổi mật khẩu</h3>
      <form @submit.prevent="handleChangePassword">
        <div class="form-group mb-3">
          <input v-model="form.oldPassword" type="password" class="form-control" placeholder="Mật khẩu hiện tại" required />
        </div>
        <div class="form-group mb-3">
          <input v-model="form.newPassword" type="password" class="form-control" placeholder="Mật khẩu mới" required />
        </div>
        <div class="form-group mb-3">
          <input v-model="form.confirmPassword" type="password" class="form-control" placeholder="Xác nhận mật khẩu mới" required />
        </div>
        <button type="submit" class="btn btn-primary w-100">Xác nhận</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const user = ref(null);
const form = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (storedUser) {
    user.value = JSON.parse(storedUser);
  } else {
    router.push("/login");
  }
});

function handleChangePassword() {
  if (form.value.newPassword !== form.value.confirmPassword) {
    alert("Mật khẩu xác nhận không khớp!");
    return;
  }

  axios.put(`http://localhost:8080/users/${user.value.id}/change-password`, null, {
    params: {
      oldPassword: form.value.oldPassword,
      newPassword: form.value.newPassword
    }
  })
  .then(() => {
    alert("Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
    localStorage.removeItem("user");
    router.push("/login");
  })
  .catch((error) => {
    console.error(error);
    alert("Đổi mật khẩu thất bại! Vui lòng kiểm tra thông tin.");
  });
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background-color: #f2f9fb;
}
.auth-card {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}
</style>
