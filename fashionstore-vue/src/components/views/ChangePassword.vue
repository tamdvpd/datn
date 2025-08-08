<template>
  <div class="auth-container">
    <div class="auth-card">
      <h3 class="text-center mb-3">Đổi mật khẩu</h3>
      <p class="text-center text-muted mb-4">Vì an toàn, hãy chọn mật khẩu đủ mạnh</p>

      <transition name="fade">
        <div v-if="alert.message" class="alert" :class="`alert-${alert.type}`" role="alert">
          {{ alert.message }}
        </div>
      </transition>

      <form @submit.prevent="handleChangePassword" novalidate>
        <!-- Mật khẩu hiện tại -->
        <div class="form-group mb-3 position-relative">
          <input
            :type="show.old ? 'text' : 'password'"
            v-model.trim="form.oldPassword"
            class="form-control"
            placeholder="Mật khẩu hiện tại"
            required
          />
          <span class="toggle" @click="show.old = !show.old">{{ show.old ? '🙈' : '👁️' }}</span>
        </div>

        <!-- Mật khẩu mới -->
        <div class="form-group mb-2 position-relative">
          <input
            :type="show.new ? 'text' : 'password'"
            v-model="form.newPassword"
            class="form-control"
            placeholder="Mật khẩu mới"
            required
          />
          <span class="toggle" @click="show.new = !show.new">{{ show.new ? '🙈' : '👁️' }}</span>
        </div>

        <!-- Strength meter -->
        <div class="progress mb-2" style="height: 6px;">
          <div
            class="progress-bar"
            :class="strengthBar.variant"
            role="progressbar"
            :style="{ width: strengthBar.width }"
            :aria-valuenow="strength.score" aria-valuemin="0" aria-valuemax="4"
          ></div>
        </div>
        <small class="text-muted d-block mb-3">{{ strengthBar.label }}</small>

        <!-- Xác nhận lại -->
        <div class="form-group mb-3 position-relative">
          <input
            :type="show.confirm ? 'text' : 'password'"
            v-model="form.confirmPassword"
            class="form-control"
            placeholder="Xác nhận mật khẩu mới"
            required
          />
          <span class="toggle" @click="show.confirm = !show.confirm">{{ show.confirm ? '🙈' : '👁️' }}</span>
        </div>

        <button type="submit" class="btn btn-primary w-100" :disabled="loading">
          <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
          Xác nhận
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const user = ref(null)
const loading = ref(false)
const alert = ref({ type: 'warning', message: '' })

const form = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const show = ref({ old: false, new: false, confirm: false })

function notify(type, message) {
  alert.value = { type, message }
  setTimeout(() => (alert.value = { type: 'warning', message: '' }), 3000)
}

onMounted(() => {
  const storedUser = localStorage.getItem('user')
  if (storedUser) user.value = JSON.parse(storedUser)
  else router.push('/login')
})

const strength = computed(() => {
  const pwd = form.value.newPassword || ''
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

async function handleChangePassword() {
  if (!form.value.oldPassword || !form.value.newPassword) {
    return notify('warning', 'Vui lòng nhập đầy đủ mật khẩu!')
  }
  if (form.value.newPassword === form.value.oldPassword) {
    return notify('warning', 'Mật khẩu mới phải khác mật khẩu hiện tại!')
  }
  if (form.value.newPassword !== form.value.confirmPassword) {
    return notify('warning', 'Mật khẩu xác nhận không khớp!')
  }
  if (strength.value.score < 3) {
    // tuỳ bạn, có thể hạ mức yêu cầu
    return notify('warning', 'Mật khẩu mới quá yếu. Hãy thêm chữ hoa, số, ký tự đặc biệt.')
  }

  try {
    loading.value = true
    await axios.put(
      `http://localhost:8080/users/${user.value.id}/change-password`,
      null,
      {
        params: {
          oldPassword: form.value.oldPassword,
          newPassword: form.value.newPassword,
        },
      }
    )
    notify('success', 'Đổi mật khẩu thành công! Vui lòng đăng nhập lại.')
    // dọn session
    localStorage.removeItem('user')
    localStorage.removeItem('jwt')
    setTimeout(() => router.push('/login'), 700)
  } catch (error) {
    console.error(error)
    const msg =
      error?.response?.data ||
      error?.response?.data?.message ||
      'Đổi mật khẩu thất bại! Vui lòng kiểm tra thông tin.'
    notify('danger', typeof msg === 'string' ? msg : 'Đổi mật khẩu thất bại!')
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
  max-width: 420px;
}
.fade-enter-active, .fade-leave-active { transition: opacity .2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
.toggle {
  position: absolute;
  top: 50%;
  right: 12px;
  transform: translateY(-50%);
  cursor: pointer;
  user-select: none;
  font-size: 16px;
}
.progress { background-color: #eef3f7; }
</style>
