<template>
  <div class="auth-container">
    <div class="auth-card">
      <router-link to="/" class="d-inline-block">
        <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      </router-link>

      <h3 class="text-center mb-4">Đăng ký</h3>
      <form @submit.prevent="handleRegister">
        <!-- Họ tên -->
        <div class="form-group mb-3">
          <input v-model="form.fullName" type="text" class="form-control" placeholder="Họ tên" required />
        </div>

        <!-- Email + Gửi mã -->
        <div class="form-group mb-3 d-flex gap-2">
          <input v-model="form.email" type="email" class="form-control" placeholder="Email" required />
          <button
            type="button"
            class="btn btn-outline-primary"
            @click="sendOtp"
            :disabled="countdown > 0"
          >
            <template v-if="countdown > 0">Gửi lại ({{ countdown }}s)</template>
            <template v-else>Gửi mã</template>
          </button>
        </div>

        <!-- Nhập OTP -->
        <div v-if="isOtpSent" class="form-group mb-3 d-flex gap-2">
          <input v-model="otpCode" type="text" class="form-control" placeholder="Nhập mã OTP" />
          <button type="button" class="btn btn-outline-success" @click="verifyOtp">Xác minh</button>
        </div>

        <!-- Email xác thực -->
        <p v-if="isOtpVerified" class="text-success text-center mb-3">✅ Email đã được xác thực</p>

        <!-- Số điện thoại -->
        <div class="form-group mb-3">
          <input v-model="form.phoneNumber" type="text" class="form-control" placeholder="Số điện thoại" />
        </div>

        <!-- Địa chỉ -->
        <div class="form-group mb-3">
          <input v-model="form.address" type="text" class="form-control" placeholder="Địa chỉ" />
        </div>

        <!-- Mật khẩu -->
        <div class="form-group mb-3">
          <input v-model="form.password" type="password" class="form-control" placeholder="Mật khẩu" required />
        </div>

        <!-- Nhập lại mật khẩu -->
        <div class="form-group mb-3">
          <input v-model="form.confirmPassword" type="password" class="form-control" placeholder="Nhập lại mật khẩu" required />
        </div>

        <!-- Đồng ý điều khoản -->
        <div class="form-group mb-3 form-check">
          <input v-model="form.agree" type="checkbox" class="form-check-input" required />
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

const form = ref({
  fullName: '',
  email: '',
  phoneNumber: '',
  address: '',
  password: '',
  confirmPassword: '',
  agree: false
})

const isOtpSent = ref(false)
const isOtpVerified = ref(false)
const otpCode = ref('')

// Countdown resend OTP
const countdown = ref(0)
let countdownInterval = null

function sendOtp() {
  if (!form.value.email) {
    alert('Vui lòng nhập email trước!')
    return
  }

  axios.post('http://localhost:8080/users/register/send-otp', {
    email: form.value.email
  }).then(() => {
    isOtpSent.value = true
    alert('Đã gửi mã xác thực đến email.')
    // countdown 60s
    countdown.value = 60
    if (countdownInterval) clearInterval(countdownInterval)
    countdownInterval = setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--
      } else {
        clearInterval(countdownInterval)
      }
    }, 1000)
  }).catch(err => {
    console.error(err)
    alert('❌ Gửi OTP thất bại!')
  })
}

function verifyOtp() {
  if (!otpCode.value) {
    alert('Vui lòng nhập mã OTP!')
    return
  }

  axios.post('http://localhost:8080/users/register/verify-otp', {
    email: form.value.email,
    otp: otpCode.value
  }).then(() => {
    isOtpVerified.value = true
    alert('✅ Xác thực email thành công!')
  }).catch(() => {
    alert('❌ Mã OTP không hợp lệ hoặc đã hết hạn!')
  })
}

function handleRegister() {
  const { fullName, email, phoneNumber, address, password, confirmPassword, agree } = form.value

  if (!fullName || !email || !password || !confirmPassword) {
    alert('Vui lòng nhập đầy đủ thông tin!')
    return
  }

  if (password !== confirmPassword) {
    alert('Mật khẩu nhập lại không khớp!')
    return
  }

  if (!agree) {
    alert('Bạn phải đồng ý với điều khoản để tiếp tục!')
    return
  }

  if (!isOtpVerified.value) {
    alert('Vui lòng xác thực email trước khi đăng ký!')
    return
  }

  axios.post('http://localhost:8080/users/auth/register', {
    fullName,
    email,
    phoneNumber,
    address,
    password
  }).then(() => {
    alert('✅ Đăng ký thành công! Vui lòng đăng nhập.')
    router.push('/login')
  }).catch(err => {
    const errorMsg = err?.response?.data?.message || 'Đăng ký thất bại!'
    alert(`❌ ${errorMsg}`)
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