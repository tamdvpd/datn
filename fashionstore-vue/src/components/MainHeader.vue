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


        <!-- Nhóm button bên phải -->
        <div class="d-flex align-items-center ms-auto">
          <!-- Nút sản phẩm gọn -->
          <router-link to="/product" class="btn btn-warning btn-sm me-2">
            🛍️ Sản phẩm
          </router-link>

          <!-- Giỏ hàng -->
          <router-link to="/cart" class="btn btn-outline-primary btn-sm me-2">

            🛒 Giỏ hàng
          </router-link>

          <!-- Nếu chưa đăng nhập -->
          <div v-if="!currentUser">

            <router-link to="/login" class="btn btn-outline-secondary btn-sm me-2">
              Đăng nhập
            </router-link>
            <router-link to="/register" class="btn btn-outline-secondary btn-sm">

              Đăng ký
            </router-link>
          </div>

          <!-- Nếu đã đăng nhập -->
          <div v-else class="dropdown">
            <img :src="currentUser.imageUrl || require('@/assets/img/default-avatar.png')" alt="Avatar" width="36"
              height="36" class="rounded-circle me-2 dropdown-toggle" data-bs-toggle="dropdown"
              style="cursor: pointer" />
            <ul class="dropdown-menu dropdown-menu-end">
              <li>
                <router-link to="/profile" class="dropdown-item">
                  👤 Thông tin cá nhân
                </router-link>
              </li>
              <li>
                <router-link to="/order" class="dropdown-item">
                  📦 Đơn hàng
                </router-link>
              </li>
              <li>
                <hr class="dropdown-divider" />
              </li>
              <li>
                <router-link to="/change-password" class="dropdown-item">
                  🔑 Đổi mật khẩu
                </router-link>
              </li>
              <li>
                <a class="dropdown-item" @click="handleLogout">🚪 Đăng xuất</a>
              </li>
              <li v-if="currentUser.role === 'ADMIN'">
                <router-link to="/admin/orders" class="dropdown-item">
                  ⚙️ Quản lý Admin
                </router-link>
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
      currentUser: null,
    };
  },
  created() {
    this.loadCurrentUser();
  },
  methods: {


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

.navbar-brand img {
  filter: drop-shadow(1px 1px 1px rgba(0, 0, 0, 0.1));
}

/* chỉnh nút sản phẩm nhỏ gọn */
.btn-warning.btn-sm {
  padding: 0.25rem 0.6rem;
  font-size: 0.85rem;
  font-weight: 600;
  border-radius: 6px;
}
</style>