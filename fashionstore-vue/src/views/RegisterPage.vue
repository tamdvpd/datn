<template>
  <div class="register-page">
    <h1>Register Page</h1>
    <form @submit.prevent="handleRegister">
      <div>
        <label for="username">Username:</label>
        <input id="username" v-model="username" type="text" required />
      </div>
      <div>
        <label for="email">Email:</label>
        <input id="email" v-model="email" type="email" required />
      </div>
      <div>
        <label for="password">Password:</label>
        <input id="password" v-model="password" type="password" required />
      </div>
      <button type="submit">Register</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'RegisterPage',
  data() {
    return {
      username: '',
      email: '',
      password: '',
    };
  },
  methods: {
    handleRegister() {
      // Xử lý đăng ký ở đây
      alert(`Đăng ký với username: ${this.username}, email: ${this.email}`);
    },
  },
};
</script>

<style scoped>
.register-page {
  max-width: 400px;
  margin: auto;
  padding: 1rem;
}
label {
  display: block;
  margin-top: 0.5rem;
}
input {
  width: 100%;
  padding: 0.4rem;
  margin-top: 0.2rem;
}
button {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
}
</style>
