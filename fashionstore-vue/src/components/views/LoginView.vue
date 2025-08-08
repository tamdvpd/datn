<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- Logo -->
      <router-link to="/" class="logo-link">
        <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      </router-link>

      <h3 class="text-center mb-4">Đăng nhập</h3>

      <form @submit.prevent="handleLogin">
        <!-- Email -->
        <div class="form-group mb-3">
          <input
            v-model="loginForm.email"
            type="email"
            class="form-control"
            placeholder="Email"
            required
          />
        </div>

        <!-- Mật khẩu + link quên mật khẩu -->
        <div class="form-group mb-3">
          <input
            v-model="loginForm.password"
            type="password"
            class="form-control"
            placeholder="Mật khẩu"
            required
          />
          <div class="text-end mt-1">
            <router-link to="/forgot-password" class="small text-primary">
              Quên mật khẩu?
            </router-link>
          </div>
        </div>

        <!-- Nút đăng nhập -->
        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>

        <!-- Google Login -->
        <div class="mt-3 text-center">
          <GoogleLogin :onSuccess="onGoogleSuccess" :onError="onGoogleError" />
        </div>
      </form>

      <!-- Chuyển sang đăng ký -->
      <div class="text-center mt-3">
        <span>Chưa có tài khoản?
          <router-link to="/register">Đăng ký</router-link>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { GoogleLogin } from "vue3-google-login";

const router = useRouter();

const loginForm = ref({
  email: "",
  password: "",
});

onMounted(() => {
  const jwt = localStorage.getItem("jwt");
  if (jwt) router.push("/");
});

function handleLogin() {
  axios
    .post("http://localhost:8080/users/auth/login", loginForm.value)
    .then((response) => {
      alert("✅ Đăng nhập thành công!");
      localStorage.setItem("jwt", response.data.jwt || "");
      localStorage.setItem("user", JSON.stringify(response.data));
      router.push("/");
    })
    .catch((error) => {
      const msg = error.response?.data?.message || "❌ Đăng nhập thất bại";
      alert(msg);
    });
}

// Đăng nhập bằng Google
const onGoogleSuccess = async (response) => {
  const idToken = response?.credential;
  if (!idToken) {
    alert("Không nhận được idToken từ Google");
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/users/auth/google", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ idToken }),
    });

    const data = await res.json();

    if (res.ok && data.jwt) {
      localStorage.setItem("jwt", data.jwt);
      localStorage.setItem("user", JSON.stringify(data.user || {}));
      alert(data.newUser ? "✅ Đã tạo tài khoản Google & đăng nhập!" : "✅ Đăng nhập Google thành công!");
      router.push("/");
    } else {
      alert(data.message || "Đăng nhập Google thất bại.");
    }
  } catch (error) {
    console.error("❌ Lỗi kết nối server:", error);
    alert("Không thể kết nối máy chủ.");
  }
};

const onGoogleError = () => alert("❌ Google login không thành công.");
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
.logo-link { display: block; text-align: center; }
.logo { display: block; margin: 0 auto; max-width: 180px; height: auto; }
</style>
