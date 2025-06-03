<template>
    <div class="container py-5" style="max-width: 500px;">
        <div class="card shadow">
            <div class="card-body">
                <h4 class="card-title text-center mb-4">{{ isLogin ? 'Đăng nhập' : 'Đăng ký' }}</h4>

                <!-- Form Đăng nhập -->
                <form v-if="isLogin" @submit.prevent="handleLogin">
                    <div class="mb-3">
                        <label for="loginEmail" class="form-label">Email</label>
                        <input v-model="loginForm.email" type="email" class="form-control" id="loginEmail" required>
                    </div>
                    <div class="mb-3">
                        <label for="loginPassword" class="form-label">Mật khẩu</label>
                        <input v-model="loginForm.password" type="password" class="form-control" id="loginPassword"
                            required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
                </form>

                <!-- Form Đăng ký -->
                <form v-else @submit.prevent="handleRegister">
                    <div class="mb-3">
                        <label for="registerName" class="form-label">Họ tên</label>
                        <input v-model="registerForm.name" type="text" class="form-control" id="registerName" required>
                    </div>
                    <div class="mb-3">
                        <label for="registerEmail" class="form-label">Email</label>
                        <input v-model="registerForm.email" type="email" class="form-control" id="registerEmail"
                            required>
                    </div>
                    <div class="mb-3">
                        <label for="registerPassword" class="form-label">Mật khẩu</label>
                        <input v-model="registerForm.password" type="password" class="form-control"
                            id="registerPassword" required>
                    </div>
                    <button type="submit" class="btn btn-success w-100">Đăng ký</button>
                </form>

                <!-- Liên kết chuyển đổi -->
                <div class="text-center mt-3">
                    <small>
                        {{ isLogin ? "Chưa có tài khoản?" : "Đã có tài khoản?" }}
                        <a href="#" @click.prevent="toggleForm">
                            {{ isLogin ? "Đăng ký" : "Đăng nhập" }}
                        </a>
                    </small>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'

const isLogin = ref(true)

const loginForm = ref({
    email: '',
    password: ''
})

const registerForm = ref({
    name: '',
    email: '',
    password: ''
})

function toggleForm() {
    isLogin.value = !isLogin.value
}

function handleLogin() {
    // Xử lý đăng nhập
    console.log('Login:', loginForm.value)
    alert('Đăng nhập thành công!')
}

function handleRegister() {
    // Xử lý đăng ký
    console.log('Register:', registerForm.value)
    alert('Đăng ký thành công!')
}
</script>
  
