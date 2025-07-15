<template>
  <header>
    <div class="top-bar py-2 text-center text-white bg-primary">
      Chào mừng đến với Haravan shop
    </div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
<<<<<<< Updated upstream
        <router-link to="/" class="navbar-brand">
          <img
            src="@/assets/img/LogoChinh.png"
            alt="Logo"
            width="200"
            height="50"
          />
        </router-link>

=======
        <!-- Logo -->
        <router-link to="/" class="navbar-brand">
          <img src="@/assets/img/LogoChinh.png" alt="Logo" width="200" height="50" />
        </router-link>

        <!-- Tìm kiếm -->
>>>>>>> Stashed changes
        <form class="d-flex me-auto ms-5" @submit.prevent="onSearch">
          <input
            class="form-control me-2"
            type="search"
            v-model="searchQuery"
            placeholder="Tìm kiếm sản phẩm..."
<<<<<<< Updated upstream
=======
            aria-label="Search"
>>>>>>> Stashed changes
          />
          <button class="btn btn-outline-success" type="submit">Tìm</button>
        </form>

<<<<<<< Updated upstream
        <div class="d-flex align-items-center">
          <router-link to="/cart" class="btn btn-outline-primary me-2"
            >🛒 Giỏ hàng</router-link
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
=======
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
>>>>>>> Stashed changes
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
export default {
<<<<<<< Updated upstream
  name: "MainHeader",
  data() {
    return {
      searchQuery: "",
      currentUser: null,
    };
  },
  created() {
    this.loadCurrentUser();
  },
  methods: {
    onSearch() {
      this.$emit("search", this.searchQuery);
    },
    loadCurrentUser() {
      const user = localStorage.getItem("user");
      if (user) {
        this.currentUser = JSON.parse(user);
      }
    },
    handleLogout() {
      localStorage.removeItem("user");
      this.currentUser = null;
      this.$router.push("/");
=======
  name: 'MainHeader',
  data() {
    return {
      searchQuery: '',
    };
  },
  methods: {
    onSearch() {
      this.$emit('search', this.searchQuery);
>>>>>>> Stashed changes
    },
  },
};
</script>

<style scoped>
.top-bar {
  background-color: #00b0f0;
  font-weight: bold;
}
<<<<<<< Updated upstream
.navbar {
  background-color: #00c0f1 !important;
}
=======

.navbar {
  background-color: #00c0f1 !important;
}

>>>>>>> Stashed changes
.navbar .form-control,
.navbar .btn {
  border-color: white;
}
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
.navbar .btn-outline-success {
  color: white;
  border-color: white;
}
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
.navbar .btn-outline-success:hover {
  background-color: white;
  color: #00c0f1;
}
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
.navbar-brand img {
  filter: drop-shadow(1px 1px 1px rgba(0, 0, 0, 0.1));
}
</style>
