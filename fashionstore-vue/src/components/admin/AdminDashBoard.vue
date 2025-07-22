<template>
  <div class="admin-dashboard">
    <!-- Navbar -->
    <nav class="navbar navbar-light bg-white shadow-sm px-4 d-flex justify-content-between align-items-center">
      <div class="d-flex align-items-center">
        <!-- Nút mở sidebar trên màn hình nhỏ -->
        <button class="btn btn-outline-primary d-md-none me-3" type="button" data-bs-toggle="offcanvas"
          data-bs-target="#sidebarMenu">
          ☰
        </button>
        <router-link to="/admin" class="navbar-brand">
          <img src="@/assets/img/LogoChinh.png" alt="Logo" height="40" />
        </router-link>
      </div>
      <div>
        <button class="btn btn-outline-secondary me-2">🔐 Tài khoản</button>
        <button class="btn btn-outline-danger" @click="handleLogout">⬆️ Đăng xuất</button>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <!-- Sidebar (offcanvas trên mobile, hiển thị luôn trên desktop) -->
        <div class="col-md-2 d-none d-md-block p-0">
          <div class="bg-light vh-100 admin-sidebar pt-3">
            <ul class="nav flex-column px-2">
              <li class="nav-item"><router-link class="nav-link" to="/admin">🏠 TRANG CHỦ</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/categories">📂 DANH MỤC</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/products">👕SẢN PHẨM</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/orders">📦 ĐƠN HÀNG</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/coupons">🏷️ MÃ GIẢM GIÁ</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/payments">💳 THANH TOÁN</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/shipping">🚚 VẬN CHUYỂN</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/inventory">📦 KHO HÀNG</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/invoice">🧾 PHIẾU NHẬP</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/inventory_adjustment">🗂️ PHIẾU ĐIỀU
                  CHỈNH</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/users">👤 NGƯỜI DÙNG</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/ui">🎨 GIAO DIỆN</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/supplier">🏭 NHÀ CUNG CẤP</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/support">💬 HỖ TRỢ KHÁCH HÀNG</router-link>
              </li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/reports">📊 BÁO CÁO & THỐNG KÊ</router-link>
              </li>
            </ul>
          </div>
        </div>

        <!-- Offcanvas Sidebar (mobile) -->
        <div class="offcanvas offcanvas-start d-md-none admin-sidebar" tabindex="-1" id="sidebarMenu">
          <div class="offcanvas-header">
            <h5 class="offcanvas-title">📋 Menu</h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
          </div>
          <div class="offcanvas-body p-0">
            <ul class="nav flex-column px-2">
              <li class="nav-item"><router-link class="nav-link" to="/admin">🏠 TRANG CHỦ</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/categories">📂DANH MỤC</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/products">👕SẢN PHẨM</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/orders">📦 ĐƠN HÀNG</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/coupons">🏷️ MÃ GIẢM GIÁ</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/payments">💳 THANH TOÁN</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/shipping">🚚 VẬN CHUYỂN</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/inventory">📦 KHO HÀNG</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/invoice">🧾 PHIẾU NHẬP</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/inventory_adjustment">🗂️ PHIẾU ĐIỀU
                  CHỈNH</router-link></li>
                  <li class="nav-item"><router-link class="nav-link" to="/admin/invoicedetails">📥CHI TIẾT PHIẾU NHẬP</router-link></li>     
              <li class="nav-item"><router-link class="nav-link" to="/admin/users">👤 NGƯỜI DÙNG</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/ui">🎨GIAO DIỆN</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/supplier">🏭NHÀ CUNG CẤP</router-link></li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/support">💬 HỖ TRỢ KHÁCH HÀNG</router-link>
              </li>
              <li class="nav-item"><router-link class="nav-link" to="/admin/reports">📊 BÁO CÁO & THỐNG KÊ</router-link>
              </li>
            </ul>
          </div>
        </div>
        <!-- Main Content -->
        <main class="col-md-10 ms-auto p-4">
          <router-view></router-view>
        </main>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminDashBoard',
  methods: {
    handleLogout() {
      localStorage.removeItem('user'); // Xóa token hoặc dữ liệu user
      this.$router.push('/login');     // Điều hướng về trang Login
    }
  }
};
</script>

<style scoped>
.admin-sidebar .nav-link {
  font-weight: 500;
  color: #000;
  margin-bottom: 5px;
}

.admin-sidebar .nav-link.router-link-exact-active,
.admin-sidebar .nav-link:hover {
  background-color: #e0f7fa;
  border-radius: 4px;
}
</style>
