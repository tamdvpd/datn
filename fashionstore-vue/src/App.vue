<template>
  <div class="app-container">
    <!-- Chỉ hiển thị component theo route -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App"
}
</script>

<style scoped>
.app-container {
  padding: 20px;
  font-family: Arial, sans-serif;
}
</style>
