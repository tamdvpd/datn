<template>
  <div class="auth-container">
    <div class="auth-card">
      <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      <h3 class="text-center mb-4">Đăng nhập</h3>
      <form @submit.prevent="handleLogin">
        <div class="form-group mb-3">
          <input v-model="loginForm.email" type="email" class="form-control" placeholder="Email" required />
        </div>
        <div class="form-group mb-3">
          <input v-model="loginForm.password" type="password" class="form-control" placeholder="Mật khẩu" required />
        </div>
        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
      </form>
      <div class="text-center mt-3">
        <span>Chưa có tài khoản?
          <router-link to="/register">Đăng ký</router-link>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const loginForm = ref({
  email: '',
  password: ''
})

function handleLogin() {
  console.log('Login:', loginForm.value)
  alert('Đăng nhập thành công!')
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
.logo {
  display: block;
  margin: 0 auto;
  max-width: 180px;
}
</style>
