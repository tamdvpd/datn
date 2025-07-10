<template>
  <div class="auth-container">
    <div class="auth-card">
      <router-link to="/" class="d-inline-block">
        <img src="@/assets/img/LogoChinh.png" class="logo mb-3" alt="Logo" />
      </router-link>

      <h3 class="text-center mb-4">Đăng nhập</h3>
      <form @submit.prevent="handleLogin">
        <div class="form-group mb-3">
          <input v-model="loginForm.email" type="email" class="form-control" placeholder="Email" required />
        </div>
        <div class="form-group mb-3">
          <input v-model="loginForm.password" type="password" class="form-control" placeholder="Mật khẩu" required />
        </div>
        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
        <GoogleLogin :onSuccess="onGoogleSuccess" :onError="onGoogleError" />

      </form>
      <div class="text-center mt-3">
        <span>Chưa có tài khoản? <router-link to="/register">Đăng ký</router-link></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { GoogleLogin } from 'vue3-google-login'  // 👈 THÊM DÒNG NÀY

const router = useRouter();

const loginForm = ref({
  email: "",
  password: "",
});

onMounted(() => {
  const jwt = localStorage.getItem("jwt");
  if (jwt) {
    router.push("/");
  }
});

function handleLogin() {
  axios
    .post("http://localhost:8080/users/auth/login", {
      email: loginForm.value.email,
      password: loginForm.value.password
    })
    .then((response) => {
      alert("Đăng nhập thành công!");
      localStorage.setItem("user", JSON.stringify(response.data));
      router.push("/");
    })
    .catch((error) => {
      if (error.response) {
        if (error.response.status === 403) {
          alert(error.response.data?.message || "Tài khoản đã bị khóa.");
        } else if (error.response.status === 401) {
          alert(error.response.data?.message || "Email hoặc mật khẩu không chính xác.");
        } else {
          alert("Đăng nhập thất bại.");
        }
      } else {
        alert("Không thể kết nối tới máy chủ.");
      }
      console.error(error);
    });
}

// ✅ Hàm xử lý thành công từ Google
const onGoogleSuccess = async (response) => {
  const idToken = response.credential;
  try {
    const res = await fetch("http://localhost:8080/users/auth/google", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ idToken })
    });
    const data = await res.json();
    localStorage.setItem("jwt", data.jwt);
    alert("Đăng nhập Google thành công!");
    router.push("/");
  } catch (error) {
    console.error(error);
    alert("Đăng nhập Google thất bại.");
  }
};

const onGoogleError = () => {
  alert("Google login không thành công.");
};
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
