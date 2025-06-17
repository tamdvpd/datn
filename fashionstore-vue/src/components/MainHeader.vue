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
          />
          <button class="btn btn-outline-success" type="submit">Tìm</button>
        </form>

        <!-- Điều hướng và người dùng -->
        <div class="d-flex align-items-center">
          <!-- ✅ Nút xem tất cả sản phẩm -->
          <router-link to="/products" class="btn btn-outline-dark me-2">🛍️ Tất cả sản phẩm</router-link>

          <!-- Giỏ hàng -->
          <router-link to="/cart" class="btn btn-outline-primary me-2">🛒 Giỏ hàng</router-link>

          <!-- Nếu đã đăng nhập -->
          <div v-if="user" class="dropdown">
            <button
              class="btn btn-outline-secondary dropdown-toggle"
              type="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              {{ user.username }}
            </button>
            <ul class="dropdown-menu dropdown-menu-end">
              <li>
                <router-link class="dropdown-item" to="/profile">Quản lý tài khoản</router-link>
              </li>
              <li v-if="user.role?.toUpperCase() === 'ADMIN'">
                <router-link class="dropdown-item" to="/admin">Quản lý Admin</router-link>
              </li>
              <li><hr class="dropdown-divider" /></li>
              <li>
                <a class="dropdown-item" href="#" @click.prevent="logout">Đăng xuất</a>
              </li>
            </ul>
          </div>

          <!-- Nếu chưa đăng nhập -->
          <div v-else>
            <router-link to="/login" class="btn btn-outline-secondary me-2">Đăng nhập</router-link>
            <router-link to="/register" class="btn btn-outline-secondary">Đăng ký</router-link>
          </div>
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
      user: null,
    };
  },
  mounted() {
    this.loadUser()
    window.addEventListener('user-updated', this.loadUser)
  },
  beforeUnmount() {
    window.removeEventListener('user-updated', this.loadUser)
  },
  methods: {
    onSearch() {
      this.$emit("search", this.searchQuery)
    },
    loadUser() {
      const stored = localStorage.getItem("user")
      try {
        const parsed = stored ? JSON.parse(stored) : null
        this.user = parsed?.user || null
      } catch (e) {
        this.user = null
      }
    },
    logout() {
      localStorage.removeItem("user")
      this.user = null
      this.$router.push("/")
    }
  }
}
</script>

<style scoped>
.top-bar {
  font-weight: bold;
}
</style>
