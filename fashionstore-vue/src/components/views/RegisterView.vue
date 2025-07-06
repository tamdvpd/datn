<template>
  <div class="auth-container">
    <div class="auth-card">
      <router-link to="/" class="d-inline-block">
        <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      </router-link>

      <h3 class="text-center mb-4">Đăng ký</h3>
      <form @submit.prevent="handleRegister">
        <div class="form-group mb-3">
          <input v-model="registerForm.fullName" type="text" class="form-control" placeholder="Họ tên" required />
        </div>
        <div class="form-group mb-3">
          <input v-model="registerForm.email" type="email" class="form-control" placeholder="Email" required />
        </div>
        <div class="form-group mb-3">
          <input v-model="registerForm.phoneNumber" type="text" class="form-control" placeholder="Số điện thoại" />
        </div>
        <div class="form-group mb-3">
          <input v-model="registerForm.address" type="text" class="form-control" placeholder="Địa chỉ" />
        </div>
        <div class="form-group mb-3">
          <input v-model="registerForm.password" type="password" class="form-control" placeholder="Mật khẩu" required />
        </div>
        <div class="form-group mb-3">
          <input v-model="registerForm.confirmPassword" type="password" class="form-control" placeholder="Nhập lại mật khẩu" required />
        </div>
        <div class="form-group mb-3 form-check">
          <input v-model="registerForm.agree" type="checkbox" class="form-check-input" required />
          <label class="form-check-label">Tôi đồng ý với điều khoản và chính sách</label>
        </div>
        <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
      </form>
      <div class="text-center mt-3">
        <span>Đã có tài khoản? <router-link to="/login">Đăng nhập</router-link></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const registerForm = ref({
  fullName: '',
  email: '',
  phoneNumber: '',
  address: '',
  password: '',
  confirmPassword: '',
  agree: false
})

function handleRegister() {
  if (!registerForm.value.fullName || !registerForm.value.email || !registerForm.value.password || !registerForm.value.confirmPassword) {
    alert('Vui lòng nhập đầy đủ thông tin!')
    return
  }
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    alert('Mật khẩu nhập lại không khớp!')
    return
  }
  if (!registerForm.value.agree) {
    alert('Bạn phải đồng ý với điều khoản để tiếp tục!')
    return
  }
  axios.post('http://localhost:8080/users/auth/register', {
    fullName: registerForm.value.fullName,
    email: registerForm.value.email,
    phoneNumber: registerForm.value.phoneNumber,
    address: registerForm.value.address,
    password: registerForm.value.password,
    role: 'USER',
    status: true
  })
  .then(() => {
    alert('Đăng ký thành công! Vui lòng đăng nhập.')
    router.push('/login')
  })
  .catch(err => {
    console.error(err)
    alert('Đăng ký thất bại! Có thể email đã tồn tại.')
  })
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
