<template>
  <div class="auth-container">
    <div class="auth-card">
      <router-link to="/" class="d-inline-block">
        <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      </router-link>

      <h3 class="text-center mb-4">Đăng ký</h3>

      <form @submit.prevent="handleRegister">
        <div class="form-group mb-3">
          <input
            v-model="registerForm.username"
            type="text"
            class="form-control"
            placeholder="Tên đăng nhập"
            required
          />
        </div>

        <div class="form-group mb-3">
          <input
            v-model="registerForm.email"
            type="email"
            class="form-control"
            placeholder="Email"
            required
          />
        </div>

        <div class="form-group mb-3">
          <input
            v-model="registerForm.password"
            type="password"
            class="form-control"
            placeholder="Mật khẩu"
            required
          />
        </div>

        <button type="submit" class="btn btn-primary w-100" :disabled="loading">
          {{ loading ? 'Đang đăng ký...' : 'Đăng ký' }}
        </button>
      </form>

      <div v-if="error" class="alert alert-danger mt-3">{{ error }}</div>
      <div v-if="success" class="alert alert-success mt-3">{{ success }}</div>

      <div class="text-center mt-3">
        <span>Đã có tài khoản? <router-link to="/login">Đăng nhập</router-link></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const registerForm = ref({
  username: '',
  email: '',
  password: ''
})

const loading = ref(false)
const error = ref('')
const success = ref('')

async function handleRegister() {
  loading.value = true
  error.value = ''
  success.value = ''
  try {
    const response = await axios.post('http://localhost:8080/users/register', registerForm.value)
    success.value = 'Đăng ký thành công!'
    setTimeout(() => router.push('/login'), 1500)
  } catch (err) {
    console.error(err)
    error.value = err.response?.data?.message || 'Đăng ký thất bại!'
  } finally {
    loading.value = false
  }
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
