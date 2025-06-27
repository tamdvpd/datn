<template>
  <footer class="main-footer">
    <div class="footer-container">
      <div class="footer-content">
        <div class="footer-section">
          <h4>HỖ TRỢ KHÁCH HÀNG</h4>
          <div class="footer-logo">
            <img src="@/assets/img/LogoChinh.png" alt="Haravan Logo" class="footer-logo-img" />
          </div>
          <p>HOTLINE: 0933.153.834 - MR.HÙNG</p>
          <p>Email: dochoidientu@gmail.com</p>
          <p>Địa chỉ: Số 7A Thương mại Hàng Giang</p>
          <p>Giờ mở cửa: 08:00 - 21:00 các ngày trong tuần</p>
        </div>
        <div class="footer-section">
          <h4>CHÍNH SÁCH WEBSITE</h4>
          <ul>
            <li>Thông tin về sản phẩm</li>
            <li>Chính sách đổi trả</li>
            <li>Chính sách bảo mật thông tin</li>
            <li>Chính sách vận chuyển</li>
            <li>Chính sách thanh toán</li>
            <li>Chính sách hoàn tiền</li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>KINH NGHIỆM MUA HÀNG</h4>
          <ul>
            <li>Kinh nghiệm nhận biết quần áo</li>
            <li>Kinh nghiệm chọn trang phục online</li>
            <li>Kinh nghiệm chọn giày lười</li>
            <li>Kinh nghiệm chọn quần áo người lớn</li>
          </ul>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.main-footer {
  background-color: #00b0f0;
  color: white;
  padding: 2rem 1rem;
  font-size: 14px;
}

.footer-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.footer-logo {
  margin: 1rem 0;
}

.footer-logo-img {
  width: 160px;
  height: auto;
}

.footer-content {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  justify-content: space-between;
  width: 100%;
  max-width: 1200px;
}

.footer-section {
  flex: 1 1 300px;
}

.footer-section h4 {
  margin-bottom: 0.5rem;
  font-weight: bold;
  font-size: 15px;
}

.footer-section ul {
  list-style-type: disc;
  padding-left: 20px;
  margin: 0;
}

.footer-section li {
  margin-bottom: 0.3rem;
}
</style>