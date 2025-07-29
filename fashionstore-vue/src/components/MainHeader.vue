<template>
  <header>
    <div class="top-bar py-2 text-center text-white bg-primary">
      Chào mừng đến với Haravan shop
    </div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <router-link to="/" class="navbar-brand">
          <img
            src="@/assets/img/LogoChinh.png"
            alt="Logo"
            width="200"
            height="50"
          />
        </router-link>

        <form class="d-flex me-auto ms-5" @submit.prevent="onSearch">
          <input
            class="form-control me-2"
            type="search"
            v-model="searchQuery"
            placeholder="Tìm kiếm sản phẩm..."
          />
          <button class="btn btn-outline-success" type="submit">Tìm</button>
        </form>

        <div class="d-flex align-items-center">
          <router-link to="/cart" class="btn btn-outline-primary me-2 position-relative"
            >
            🛒 Giỏ hàng
            <span v-if="cartItemCount > 0" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
              {{ cartItemCount }}
            </span>
          </router-link
          >

          <div v-if="!currentUser">
            <router-link to="/login" class="btn btn-outline-secondary me-2"
              >Đăng nhập</router-link
            >
            <router-link to="/register" class="btn btn-outline-secondary"
              >Đăng ký</router-link
            >
          </div>

          <div v-else class="dropdown">
            <img
              :src="
                currentUser.imageUrl ||
                require('@/assets/img/default-avatar.png')
              "
              alt="Avatar"
              width="40"
              height="40"
              class="rounded-circle me-2 dropdown-toggle"
              data-bs-toggle="dropdown"
              style="cursor: pointer"
            />
            <ul class="dropdown-menu dropdown-menu-end">
              <li>
                <router-link to="/profile" class="dropdown-item"
                  >👤 Thông tin cá nhân</router-link
                >
              </li>
              <li><hr class="dropdown-divider" /></li>
              <li>
                <router-link to="/change-password" class="dropdown-item"
                  >Đổi mật khẩu</router-link
                >
              </li>
              <li>
                <a class="dropdown-item" @click="handleLogout">🚪 Đăng xuất</a>
              </li>
              <li v-if="currentUser.role === 'ADMIN'">
                <router-link to="/admin" class="dropdown-item"
                  >🛠️ Quản lý Admin</router-link
                >
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
export default {
  name: "MainHeader",
  data() {
    return {
      searchQuery: "",
      currentUser: null,
      cartItemCount: 0,
    };
  },
  created() {
    this.loadCurrentUser();
    if (this.currentUser) {
      this.loadCartCount();
    }
    // Lắng nghe sự kiện thay đổi giỏ hàng
    window.addEventListener('storage', this.handleStorageChange);
  },
  beforeUnmount() {
    window.removeEventListener('storage', this.handleStorageChange);
  },
  methods: {
    onSearch() {
      this.$emit("search", this.searchQuery);
    },
    loadCurrentUser() {
      const user = localStorage.getItem("user");
      if (user) {
        this.currentUser = JSON.parse(user);
        this.loadCartCount();
      }
    },
    async loadCartCount() {
      if (!this.currentUser || !this.currentUser.id) return;
      
      try {
        const response = await fetch(`http://localhost:8080/api/cart/${this.currentUser.id}`);
        if (response.ok) {
          const cart = await response.json();
          this.cartItemCount = cart.length;
        }
      } catch (error) {
        console.error('Lỗi khi tải số lượng giỏ hàng:', error);
      }
    },
    handleStorageChange(event) {
      if (event.key === 'cartUpdated' && this.currentUser) {
        this.loadCartCount();
      }
    },
    handleLogout() {
      localStorage.removeItem("user");
      this.currentUser = null;
      this.$router.push("/");
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
