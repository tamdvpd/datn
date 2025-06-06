<template>
  <div>
    <Product />
    <LoginRegister />
  </div>
</template>

<script>
import Product from "./components/Product.vue";
import LoginRegister from './components/LoginRegister.vue';

export default {
  name: "App",
  components: {
    Product,
    LoginRegister
  }
}
</script>

<style scoped>
body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden; /* Ngăn cuộn ngang không mong muốn */
}
</style>