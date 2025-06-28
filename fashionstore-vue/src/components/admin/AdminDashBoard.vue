<template>
  <div class="admin-dashboard">
    <!-- Navbar -->
    <nav class="navbar navbar-light bg-white shadow-sm px-4 d-flex justify-content-between align-items-center">
      <router-link to="/admin" class="navbar-brand">
        <img src="@/assets/img/LogoChinh.png" alt="Logo" height="40" />
      </router-link>
      <div>
        <button class="btn btn-outline-secondary me-2">🔐 Tài khoản</button>
        <router-link to="/logout" class="btn btn-outline-danger">⬆️ Đăng xuất</router-link>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <!-- Sidebar -->
        <aside class="col-md-2 bg-light pt-3 admin-sidebar">
          <ul class="nav flex-column">
            <li class="nav-item"><router-link class="nav-link" to="/admin">🏠 TRANG CHỦ</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/categories">📂 QUẢN LÝ DANH MỤC</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/products">👕 QUẢN LÝ SẢN PHẨM</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/orders">📦 QUẢN LÝ ĐƠN HÀNG</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/coupons">🏷️ QUẢN LÝ MÃ GIẢM GIÁ</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/payments">💳 QUẢN LÝ THANH TOÁN</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/shipping">🚚 QUẢN LÝ VẬN CHUYỂN</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/inventory">📦 QUẢN LÝ KHO HÀNG</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/users">👤 QUẢN LÝ NGƯỜI DÙNG</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/ui">🎨 QUẢN LÝ GIAO DIỆN</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/support">💬 HỖ TRỢ KHÁCH HÀNG</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/reports">📊 BÁO CÁO & THỐNG KÊ</router-link></li>
          </ul>
        </aside>

        <!-- Main Content -->
        <main class="col-md-10 p-4">
          <router-view></router-view>
        </main>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminDashBoard',
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
