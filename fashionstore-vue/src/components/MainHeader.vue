<template>
  <header>
    <div class="top-bar py-2 text-center text-white bg-primary">
      Chào mừng đến với Haravan shop
    </div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <!-- Logo -->
        <router-link to="/" class="navbar-brand">
          <img src="@/assets/img/LogoChinh.png" alt="Logo" width="200" height="50" />
        </router-link>

        <!-- Tìm kiếm -->
        <form class="d-flex me-auto ms-5" @submit.prevent="onSearch">
          <input
            class="form-control me-2"
            type="search"
            v-model="searchQuery"
            placeholder="Tìm kiếm sản phẩm..."
            aria-label="Search"
          />
          <button class="btn btn-outline-success" type="submit">Tìm</button>
        </form>

        <!-- Nút chức năng -->
        <div class="d-flex">
          <router-link to="/cart" class="btn btn-outline-primary me-2">
            🛒 Giỏ hàng
          </router-link>
          <router-link to="/login" class="btn btn-outline-secondary me-2">
            Đăng nhập
          </router-link>
          <router-link to="/register" class="btn btn-outline-secondary">
            Đăng ký
          </router-link>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
export default {
  name: 'MainHeader',
  data() {
    return {
      searchQuery: '',
    };
  },
  methods: {
    onSearch() {
      this.$emit('search', this.searchQuery);
    },
  },
};
</script>

<style scoped>
.top-bar {
  background-color: #00b0f0;
  font-weight: bold;
}

.navbar {
  background-color: #00c0f1 !important;
}

.navbar .form-control,
.navbar .btn {
  border-color: white;
}

.navbar .btn-outline-success {
  color: white;
  border-color: white;
}

.navbar .btn-outline-success:hover {
  background-color: white;
  color: #00c0f1;
}

.navbar-brand img {
  filter: drop-shadow(1px 1px 1px rgba(0, 0, 0, 0.1));
}
</style>
