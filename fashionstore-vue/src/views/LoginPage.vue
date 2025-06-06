<template>
  <div class="login-page">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="email">Email:</label>
        <input
          id="email"
          type="email"
          v-model="email"
          placeholder="Nhập email"
          required
        />
      </div>

      <div>
        <label for="password">Password:</label>
        <input
          id="password"
          type="password"
          v-model="password"
          placeholder="Nhập mật khẩu"
          required
          minlength="6"
        />
      </div>

      <button type="submit">Login</button>
    </form>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      email: "",
      password: "",
      errorMessage: "",
    };
  },
  methods: {
    handleLogin() {
      this.errorMessage = "";

      // Validate đơn giản
      if (!this.email.includes("@")) {
        this.errorMessage = "Email không hợp lệ";
        return;
      }
      if (this.password.length < 6) {
        this.errorMessage = "Mật khẩu phải ít nhất 6 ký tự";
        return;
      }

      // Xử lý đăng nhập (ví dụ gửi API)
      alert(`Đăng nhập thành công với email: ${this.email}`);
      // Sau đó có thể chuyển trang hoặc reset form
      this.email = "";
      this.password = "";
    },
  },
};
</script>

<style scoped>
.login-page {
  max-width: 320px;
  margin: 2rem auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  text-align: left;
}
label {
  display: block;
  margin-bottom: 0.25rem;
}
input {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  box-sizing: border-box;
}
button {
  width: 100%;
  padding: 0.6rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #369870;
}
.error {
  color: red;
  margin-top: 0.5rem;
}
</style>
