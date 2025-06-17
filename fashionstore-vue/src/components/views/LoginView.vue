<template>
  <div class="auth-container">
    <div class="auth-card">
      <router-link to="/" class="d-inline-block">
        <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      </router-link>

      <h3 class="text-center mb-4">Đăng nhập</h3>
      <form @submit.prevent="handleLogin">
        <div class="form-group mb-3">
          <input
            v-model="loginForm.username"
            type="text"
            class="form-control"
            placeholder="Tên đăng nhập"
            required
          />
        </div>
        <div class="form-group mb-3">
          <input
            v-model="loginForm.password"
            type="password"
            class="form-control"
            placeholder="Mật khẩu"
            required
          />
        </div>
        <button type="submit" class="btn btn-primary w-100" :disabled="loading">
          {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
        </button>
      </form>

      <div v-if="error" class="alert alert-danger mt-3">{{ error }}</div>

      <div class="text-center mt-3">
        <span>Chưa có tài khoản? <router-link to="/register">Đăng ký</router-link></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const loginForm = ref({
  username: '',
  password: ''
})

const loading = ref(false)
const error = ref('')

async function handleLogin() {
  loading.value = true
  error.value = ''

  try {
    const params = new URLSearchParams()
    params.append('username', loginForm.value.username)
    params.append('password', loginForm.value.password)

    const response = await axios.post('http://localhost:8080/users/login', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    const user = response.data
    localStorage.setItem('user', JSON.stringify(user));
    window.dispatchEvent(new Event("user-updated"));
    localStorage.setItem('role', user.role);

    if (user.role === 'admin') {
      router.push('/admin')
    } else {
      router.push('/')
    }
  } catch (err) {
    console.error(err)
    error.value = err.response?.data?.message || 'Đăng nhập thất bại!'
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
