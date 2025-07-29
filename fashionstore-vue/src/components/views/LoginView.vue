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

        <!-- Google login -->
<div class="mt-3 text-center">
  <GoogleLogin :onSuccess="onGoogleSuccess" :onError="onGoogleError">
    <template #default>
      <button class="btn btn-social btn-google">
        <i class="fab fa-google me-2"></i> Đăng nhập bằng Google
      </button>
    </template>
  </GoogleLogin>
</div>

<!-- Facebook login -->
<div class="mt-2 text-center">
  <button class="btn btn-social btn-facebook" @click="handleFacebookLogin">
    <i class="fab fa-facebook me-2"></i> Đăng nhập bằng Facebook
  </button>
</div>


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
import { GoogleLogin } from "vue3-google-login";

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
    .post("http://localhost:8080/users/auth/login", loginForm.value)
    .then((response) => {
      alert("Đăng nhập thành công!");
      localStorage.setItem("jwt", response.data.jwt || "");
      localStorage.setItem("user", JSON.stringify(response.data));
      router.push("/");
    })
    .catch((error) => {
      const msg = error.response?.data?.message || "Đăng nhập thất bại";
      alert(msg);
    });
}

// ✅ Xử lý đăng nhập bằng Google
const onGoogleSuccess = async (response) => {
  console.log("📥 Google response:", response);

  const idToken = response.credential;
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

      if (data.newUser === true) {
        alert("✅ Tài khoản mới đã được tạo từ Google và bạn đã được đăng nhập!");
      } else {
        alert("✅ Đăng nhập Google thành công!");
      }

      router.push("/");
    } else {
      alert(data.message || "Đăng nhập Google thất bại.");
    }
  } catch (error) {
    console.error("❌ Lỗi kết nối server:", error);
    alert("Không thể kết nối máy chủ.");
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
 .btn i {
            margin-right: 10px;
        }
        
        .btn-google {
            --bg-color: white;
            --border-color: #e5e7eb;
            --text-color: #4285F4;
        }
        
        .btn-google:hover {
            border-color: #4285F4;
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(66, 133, 244, 0.15);
        }
        
        .btn-facebook {
            --bg-color: white;
            --border-color: #e5e7eb;
            --text-color: #1877f2;
        }
        
        .btn-facebook:hover {
            border-color: #1877f2;
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(24, 119, 242, 0.15);
        }
</style>