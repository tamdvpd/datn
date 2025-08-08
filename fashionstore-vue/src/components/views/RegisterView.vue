<template>
  <div class="auth-container d-flex align-items-center justify-content-center min-vh-100">
    <div class="auth-card card shadow border-0 p-3 p-md-4" style="max-width: 460px; width: 100%">
      <!-- Logo -->
      <router-link to="/" class="text-center d-block mb-2">
        <img src="@/assets/img/LogoChinh.png" class="logo" alt="Logo" />
      </router-link>

      <h3 class="text-center fw-bold mb-3">Tạo tài khoản</h3>
      <p class="text-center text-muted mb-4">Nhập thông tin để bắt đầu mua sắm ✨</p>

      <!-- Alert zone -->
      <transition name="fade">
        <div v-if="alert.message" class="alert" :class="`alert-${alert.type}`" role="alert">
          {{ alert.message }}
        </div>
      </transition>

      <form @submit.prevent="handleRegister" novalidate>
        <!-- Họ tên -->
        <div class="form-floating mb-3">
          <input v-model.trim="form.fullName" type="text" class="form-control" id="fullName" placeholder="Họ tên" required />
          <label for="fullName">Họ tên</label>
        </div>

        <!-- Email + Gửi mã -->
        <div class="mb-3">
          <div class="input-group">
            <div class="form-floating flex-grow-1">
              <input v-model.trim="form.email" type="email" class="form-control rounded-end-0" id="email" placeholder="Email" required />
              <label for="email">Email</label>
            </div>
            <button
              type="button"
              class="btn btn-outline-primary rounded-start-0 d-flex align-items-center"
              @click="sendOtp"
              :disabled="countdown > 0 || loading.otp"
              style="white-space: nowrap"
            >
              <span v-if="loading.otp" class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
              <template v-if="countdown > 0">Gửi lại ({{ countdown }}s)</template>
              <template v-else>Gửi mã</template>
            </button>
          </div>
          <div class="form-text">Sử dụng email thật để nhận mã xác thực.</div>
        </div>

        <!-- Nhập OTP -->
        <transition name="slide-fade">
          <div v-if="isOtpSent" class="mb-3">
            <div class="input-group">
              <input v-model.trim="otpCode" type="text" class="form-control" placeholder="Nhập mã OTP" />
              <button type="button" class="btn btn-outline-success" @click="verifyOtp" :disabled="loading.verify">
                <span v-if="loading.verify" class="spinner-border spinner-border-sm me-2"></span>
                Xác minh
              </button>
            </div>
            <small v-if="isOtpVerified" class="text-success d-block mt-1">✅ Email đã được xác thực</small>
          </div>
        </transition>

        <!-- Số điện thoại -->
        <div class="form-floating mb-3">
          <input v-model.trim="form.phoneNumber" type="tel" class="form-control" id="phone" placeholder="Số điện thoại" />
          <label for="phone">Số điện thoại</label>
        </div>

        <!-- Địa chỉ -->
        <div class="form-floating mb-3">
          <input v-model.trim="form.address" type="text" class="form-control" id="address" placeholder="Địa chỉ" />
          <label for="address">Địa chỉ</label>
        </div>

        <!-- Mật khẩu + toggle -->
        <div class="mb-2">
          <div class="input-group">
            <div class="form-floating flex-grow-1">
              <input :type="showPwd ? 'text' : 'password'" v-model="form.password" class="form-control rounded-end-0" id="password" placeholder="Mật khẩu" required />
              <label for="password">Mật khẩu</label>
            </div>
            <button type="button" class="btn btn-outline-secondary rounded-start-0" @click="showPwd = !showPwd">
              <i :class="showPwd ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </button>
          </div>
          <!-- Strength meter -->
          <div class="progress mt-2" style="height: 6px;">
            <div class="progress-bar" :class="strengthBar.variant" role="progressbar" :style="{width: strengthBar.width}" :aria-valuenow="strength.score" aria-valuemin="0" aria-valuemax="4"></div>
          </div>
          <small class="text-muted">{{ strengthBar.label }}</small>
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="form-floating mb-3">
          <input :type="showPwd ? 'text' : 'password'" v-model="form.confirmPassword" class="form-control" id="confirmPassword" placeholder="Nhập lại mật khẩu" required />
          <label for="confirmPassword">Nhập lại mật khẩu</label>
        </div>

        <!-- Điều khoản -->
        <div class="form-check mb-3">
          <input v-model="form.agree" class="form-check-input" type="checkbox" id="agree" required />
          <label class="form-check-label" for="agree">Tôi đồng ý với điều khoản và chính sách</label>
        </div>

        <button type="submit" class="btn btn-primary w-100 py-2" :disabled="loading.register">
          <span v-if="loading.register" class="spinner-border spinner-border-sm me-2"></span>
          Đăng ký
        </button>
      </form>

      <div class="text-center mt-3">
        <span>Đã có tài khoản? <router-link to="/login">Đăng nhập</router-link></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue'
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
  agree: false,
})

const isOtpSent = ref(false)
const isOtpVerified = ref(false)
const otpCode = ref('')
const showPwd = ref(false)

const alert = ref({ type: 'warning', message: '' })
const loading = ref({ otp: false, verify: false, register: false })

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
  if (!form.value.email) return notify('warning', 'Vui lòng nhập email trước!')
  try {
    loading.value.otp = true
    await axios.post('http://localhost:8080/users/register/send-otp', { email: form.value.email })
    isOtpSent.value = true
    notify('success', 'Đã gửi mã xác thực đến email.')
    startCountdown(60)
  } catch (err) {
    console.error(err)
    notify('danger', 'Gửi OTP thất bại!')
  } finally {
    loading.value.otp = false
  }
}

async function verifyOtp() {
  if (!otpCode.value) return notify('warning', 'Vui lòng nhập mã OTP!')
  try {
    loading.value.verify = true
    await axios.post('http://localhost:8080/users/register/verify-otp', {
      email: form.value.email,
      otp: otpCode.value,
    })
    isOtpVerified.value = true
    notify('success', 'Xác thực email thành công!')
  } catch (err) {
    notify('danger', 'Mã OTP không hợp lệ hoặc đã hết hạn!')
  } finally {
    loading.value.verify = false
  }
}

const strength = computed(() => {
  const pwd = form.value.password || ''
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

async function handleRegister() {
  const { fullName, email, phoneNumber, address, password, confirmPassword, agree } = form.value

  if (!fullName || !email || !password || !confirmPassword) {
    return notify('warning', 'Vui lòng nhập đầy đủ thông tin!')
  }
  if (password !== confirmPassword) {
    return notify('warning', 'Mật khẩu nhập lại không khớp!')
  }
  if (!agree) {
    return notify('warning', 'Bạn phải đồng ý với điều khoản để tiếp tục!')
  }
  if (!isOtpVerified.value) {
    return notify('warning', 'Vui lòng xác thực email trước khi đăng ký!')
  }

  try {
    loading.value.register = true
    await axios.post('http://localhost:8080/users/auth/register', {
      fullName,
      email,
      phoneNumber,
      address,
      password,
    })
    notify('success', 'Đăng ký thành công! Vui lòng đăng nhập.')
    setTimeout(() => router.push('/login'), 800)
  } catch (err) {
    const errorMsg = err?.response?.data?.message || 'Đăng ký thất bại!'
    notify('danger', `❌ ${errorMsg}`)
  } finally {
    loading.value.register = false
  }
}

onUnmounted(() => countdownInterval && clearInterval(countdownInterval))
</script>

<style scoped>
.auth-container {
  background: linear-gradient(135deg, #e7f3ff 0%, #f6fbff 100%);
  padding: 24px;
}
.logo { max-width: 160px; }
.fade-enter-active, .fade-leave-active { transition: opacity .2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
.slide-fade-enter-active { transition: all .25s ease; }
.slide-fade-enter-from { opacity: 0; transform: translateY(-6px); }

/* Card polish */
.auth-card { border-radius: 18px; }
.form-control:focus { box-shadow: 0 0 0 .2rem rgba(13,110,253,.15); }
.btn:focus { box-shadow: 0 0 0 .2rem rgba(13,110,253,.25); }
</style>
