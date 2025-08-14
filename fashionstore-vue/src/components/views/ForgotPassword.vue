<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- Logo -->
      <router-link to="/" class="logo-link">
        <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      </router-link>

      <h3 class="text-center mb-2">Quên mật khẩu</h3>
      <p class="text-center text-muted mb-4">Nhập email, xác thực OTP và đặt mật khẩu mới</p>

      <!-- Alert -->
      <transition name="fade">
        <div v-if="alert.message" class="alert" :class="`alert-${alert.type}`" role="alert">
          {{ alert.message }}
        </div>
      </transition>

      <!-- Bước 1: Nhập email & gửi OTP -->
      <form v-if="step === 1" @submit.prevent="sendOtp">
        <div class="form-group mb-3">
          <input v-model.trim="email" type="email" class="form-control" placeholder="Email đã đăng ký" required />
        </div>

        <button type="submit" class="btn btn-primary w-100" :disabled="loading.otp || countdown > 0">
          <span v-if="loading.otp" class="spinner-border spinner-border-sm me-2"></span>
          <template v-if="countdown > 0">Gửi lại OTP ({{ countdown }}s)</template>
          <template v-else>Gửi mã OTP</template>
        </button>

        <div class="text-center mt-3">
          <router-link to="/login">Quay lại đăng nhập</router-link>
        </div>
      </form>

      <!-- Bước 2: Nhập OTP -->
      <form v-else-if="step === 2" @submit.prevent="verifyOtp">
        <div class="form-group mb-3">
          <input v-model.trim="otp" type="text" class="form-control" placeholder="Nhập mã OTP" required />
        </div>
        <button type="submit" class="btn btn-success w-100" :disabled="loading.verify">
          <span v-if="loading.verify" class="spinner-border spinner-border-sm me-2"></span>
          Xác minh OTP
        </button>
        <div class="text-center mt-3">
          <button class="btn btn-link p-0" type="button" @click="step = 1">Nhập lại email</button>
        </div>
      </form>

      <!-- Bước 3: Đặt mật khẩu mới -->
      <form v-else @submit.prevent="resetPassword">
        <!-- Mật khẩu -->
        <div class="form-group mb-3 position-relative">
          <input :type="showPwd ? 'text' : 'password'" v-model="newPassword" class="form-control" placeholder="Mật khẩu mới" required />
          <span class="toggle-password" @click="showPwd = !showPwd">{{ showPwd ? '🙈' : '👁️' }}</span>
        </div>

        <!-- Strength meter -->
        <div class="progress mb-2" style="height: 6px;">
          <div class="progress-bar" :class="strengthBar.variant" role="progressbar" :style="{width: strengthBar.width}" :aria-valuenow="strength.score" aria-valuemin="0" aria-valuemax="4"></div>
        </div>
        <small class="text-muted d-block mb-3">{{ strengthBar.label }}</small>

        <!-- Xác nhận -->
        <div class="form-group mb-3">
          <input :type="showPwd ? 'text' : 'password'" v-model="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu" required />
        </div>

        <button type="submit" class="btn btn-primary w-100" :disabled="loading.reset">
          <span v-if="loading.reset" class="spinner-border spinner-border-sm me-2"></span>
          Đặt lại mật khẩu
        </button>

        <div class="text-center mt-3">
          <router-link to="/login">Quay lại đăng nhập</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue'
import axios from 'axios'

// LƯU Ý: các endpoint này khớp với gợi ý backend đã trao đổi
// POST /users/password/forgot/send-otp      { email }
// POST /users/password/forgot/verify-otp    { email, otp }
// POST /users/password/forgot/reset         { email, newPassword }

const email = ref('')
const otp = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const showPwd = ref(false)

const step = ref(1)
const alert = ref({ type: 'warning', message: '' })
const loading = ref({ otp: false, verify: false, reset: false })

// Countdown resend OTP
const countdown = ref(0)
let countdownInterval = null
function startCountdown(sec = 60) {
  countdown.value = sec
  if (countdownInterval) clearInterval(countdownInterval)
  countdownInterval = setInterval(() => {
    if (countdown.value > 0) countdown.value--
    else clearInterval(countdownInterval)
  }, 1000)
}

function notify(type, message) {
  alert.value = { type, message }
  setTimeout(() => (alert.value = { type: 'warning', message: '' }), 3500)
}

async function sendOtp() {
  if (!email.value) return notify('warning', 'Vui lòng nhập email!')
  try {
    loading.value.otp = true
    await axios.post('http://localhost:8080/users/password/forgot/send-otp', { email: email.value })
    notify('success', 'Đã gửi mã OTP đến email của bạn.')
    step.value = 2
    startCountdown(60)
  } catch (err) {
    const msg = err?.response?.data || 'Gửi OTP thất bại.'
    notify('danger', typeof msg === 'string' ? msg : 'Gửi OTP thất bại.')
  } finally {
    loading.value.otp = false
  }
}

async function verifyOtp() {
  if (!otp.value) return notify('warning', 'Vui lòng nhập mã OTP!')
  try {
    loading.value.verify = true
    await axios.post('http://localhost:8080/users/password/forgot/verify-otp', { email: email.value, otp: otp.value })
    notify('success', 'Xác thực OTP thành công! Hãy đặt mật khẩu mới.')
    step.value = 3
  } catch (err) {
    const msg = err?.response?.data || 'OTP không hợp lệ hoặc đã hết hạn.'
    notify('danger', typeof msg === 'string' ? msg : 'OTP không hợp lệ hoặc đã hết hạn.')
  } finally {
    loading.value.verify = false
  }
}

const strength = computed(() => {
  const pwd = newPassword.value || ''
  let score = 0
  if (pwd.length >= 8) score++
  if (/[A-Z]/.test(pwd)) score++
  if (/[0-9]/.test(pwd)) score++
  if (/[^A-Za-z0-9]/.test(pwd)) score++
  return { score }
})

const strengthBar = computed(() => {
  const s = strength.value.score
  const map = [
    { width: '0%', label: 'Mật khẩu quá yếu', variant: 'bg-danger' },
    { width: '25%', label: 'Yếu', variant: 'bg-danger' },
    { width: '50%', label: 'Trung bình', variant: 'bg-warning' },
    { width: '75%', label: 'Khá', variant: 'bg-info' },
    { width: '100%', label: 'Mạnh', variant: 'bg-success' },
  ]
  return map[s]
})

async function resetPassword() {
  if (!newPassword.value || !confirmPassword.value)
    return notify('warning', 'Vui lòng nhập mật khẩu mới và xác nhận!')
  if (newPassword.value !== confirmPassword.value)
    return notify('warning', 'Mật khẩu nhập lại không khớp!')

  try {
    loading.value.reset = true
    await axios.post('http://localhost:8080/users/password/forgot/reset', {
      email: email.value,
      newPassword: newPassword.value,
    })
    notify('success', 'Đặt lại mật khẩu thành công! Hãy đăng nhập lại.')
    // điều hướng sau 1s
    setTimeout(() => window.location.assign('/login'), 900)
  } catch (err) {
    const msg = err?.response?.data || 'Đặt lại mật khẩu thất bại.'
    notify('danger', typeof msg === 'string' ? msg : 'Đặt lại mật khẩu thất bại.')
  } finally {
    loading.value.reset = false
  }
}

onUnmounted(() => countdownInterval && clearInterval(countdownInterval))
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
  max-width: 420px;
}
.logo-link { display: block; text-align: center; }
.logo { display: block; margin: 0 auto; max-width: 180px; height: auto; }
.fade-enter-active, .fade-leave-active { transition: opacity .2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
.toggle-password { position: absolute; top: 50%; right: 12px; transform: translateY(-50%); cursor: pointer; user-select: none; font-size: 16px; }
.progress { background-color: #eef3f7; }
</style>
