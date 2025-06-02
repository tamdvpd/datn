<template>
  <div class="haravan-product-page">
    <div class="top-bar">
      <span>Chào mừng bạn đến với website Thời Trang HARAVAN</span>
    </div>

    <header class="main-header" role="banner">
      <div class="header-logo">
        <img
          alt="Haravan logo with white text on blue background"
          class="logo-img"
          src="https://storage.googleapis.com/a1aa/image/e4fab516-e29e-45c6-0c20-87f5d504fe03.jpg"
        />
      </div>
      <form
        @submit.prevent="onSearch"
        aria-label="Search products"
        class="search-form"
        role="search"
      >
        <input
          v-model="searchQuery"
          class="search-input"
          placeholder="Tìm kiếm sản phẩm..."
          type="text"
          aria-label="Tìm kiếm sản phẩm"
        />
        <button aria-label="Tìm kiếm" class="search-button" type="submit">
          TÌM KIẾM
        </button>
      </form>
      <div class="header-actions">
        <button aria-label="Giỏ hàng" class="action-button" type="button">
          <i class="fas fa-shopping-cart"></i>
          <span class="action-text">GIỎ HÀNG</span>
        </button>
        <button aria-label="Đăng nhập / Đăng ký" class="action-button" type="button">
          <i class="fas fa-user"></i>
          <span class="action-text">ĐĂNG NHẬP / ĐĂNG KÝ</span>
        </button>
      </div>
    </header>

    <nav class="main-nav" role="navigation">
      <a href="#" class="nav-link">TRANG CHỦ</a>
      <a href="#" class="nav-link">SẢN PHẨM</a>
      <a href="#" class="nav-link">ÁO</a>
      <a href="#" class="nav-link">QUẦN</a>
      <a href="#" class="nav-link">PHỤ KIỆN</a>
      <a href="#" class="nav-link">SALE</a>
    </nav>

    <main class="main-content">
      <h2 class="section-title">SẢN PHẨM</h2>
      <section aria-label="Danh sách sản phẩm" class="product-grid">
        <article
          v-for="(product, index) in filteredProducts"
          :key="index"
          class="product-card"
        >
          <img :alt="product.alt" class="product-image" :src="product.image" />
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-price">{{ product.price }}</p>
        </article>
      </section>
    </main>

    <footer class="main-footer">
      <div class="footer-section">
        <h3 class="footer-heading">HỖ TRỢ KHÁCH HÀNG</h3>
        <p class="footer-text">HOTLINE: 0933.153.834 - MR.HÙNG</p>
        <p class="footer-text">Email: dochoidientu@gmail.com</p>
        <p class="footer-text">ĐỊA CHỈ: Số 7A Thương mại Hàng Giang</p>
        <p class="footer-text">Giờ mở cửa: 08:00 - 21:00 các ngày trong tuần</p>
        <p class="footer-text footer-sub-heading">TP. HCM:</p>
        <p class="footer-text">Chi nhánh 01: 79/12 Lê Văn Sỹ</p>
        <p class="footer-text">Chi nhánh 02: 79/12 Lê Văn Sỹ</p>
        <p class="footer-text">Quận Phú Nhuận, TP.HCM</p>
      </div>
      <div class="footer-section">
        <h3 class="footer-heading">CHÍNH SÁCH WEBSITE</h3>
        <ul class="footer-list">
          <li>Thông tin về sản phẩm</li>
          <li>Chính sách đổi trả</li>
          <li>Chính sách bảo mật thông tin</li>
          <li>Chính sách vận chuyển</li>
          <li>Chính sách thanh toán</li>
          <li>Chính sách hoàn tiền</li>
        </ul>
      </div>
      <div class="footer-section">
        <h3 class="footer-heading">KINH NGHIỆM MUA HÀNG</h3>
        <ul class="footer-list">
          <li>Kinh nghiệm nhận biết quần áo</li>
          <li>Kinh nghiệm chọn trang phục online</li>
          <li>Kinh nghiệm chọn giày lười</li>
          <li>Kinh nghiệm chọn quần áo người lớn</li>
        </ul>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: "HaravanProductPage",
  data() {
    return {
      searchQuery: "",
      products: [
        {
          name: "Áo sơ mi dài tay trơn GATB011",
          price: "550,000 VND",
          image:
            "https://tse2.mm.bing.net/th?id=OIP.8Jf8KBowjgTKL6ekrtQuegHaHa&pid=Api&P=0&h=180",
          alt:
            "Áo sơ mi dài tay trơn màu xanh nhạt, kiểu dáng cổ bẻ, gấp tay áo gọn gàng",
        },
        {
          name: "Áo polo trơn bo kẻ thiếu logo ngực GSTP066",
          price: "342,250 VND",
          image:
            "https://tse4.mm.bing.net/th?id=OIP._5l5qjFUHOJXAixo70MvggHaHa&pid=Api&P=0&h=180",
          alt: "Áo polo trơn bo kẻ thiếu logo ngực màu nâu đậm, cổ bẻ, nút áo",
        },
        {
          name: "Quần kaki cái basic GABK021",
          price: "450,000 VND",
          image:
            "https://tse3.mm.bing.net/th?id=OIP.g2XJAdc9jiIcXK0E_LU4jQHaHa&pid=Api&P=0&h=180",
          alt: "Quần kaki màu be basic, kiểu dáng quần dài, ống côn",
        },
        {
          name: "Áo polo kẻ ngang bo tay phối màu GSTP039",
          price: "299,000 VND",
          image:
            "https://tse3.mm.bing.net/th?id=OIP.yBqhpeoPDn90ptOZ2K1LmwHaHa&pid=Api&P=0&h=180",
          alt: "Áo polo kẻ ngang bo tay phối màu trắng và đen, cổ bẻ, nút áo",
        },
        {
          name: "Kính Chống Tia UV | Gọng Vuông",
          price: "130,000 VND",
          image:
            "https://tse3.mm.bing.net/th?id=OIP.P_vf5KJLUqkvKVfHgO_K3wHaEo&pid=Api&P=0&h=180",
          alt: "Kính chống tia UV gọng vuông màu đen, kiểu dáng hiện đại",
        },
        {
          name: "Mũ Lưỡi Trai/ Nón Chống Tia UV",
          price: "90,000 VND",
          image:
            "https://tse2.mm.bing.net/th?id=OIP.lcMFbREe3gjSIdQwrdlWkgHaHa&pid=Api&P=0&h=180",
          alt: "Mũ lưỡi trai màu đen chống tia UV, kiểu dáng thể thao",
        },
        // Thêm 2 sản phẩm mới
        {
          name: "Áo Khoác Nỉ Form Rộng Unisex",
          price: "380,000 VND",
          image:
            "https://tse4.mm.bing.net/th?id=OIP.Wtv_t4d6CQZfv2xtEKXRtwHaHa&pid=Api&P=0&h=180",
          alt: "Áo khoác nỉ form rộng unisex màu xám nhạt, có mũ trùm đầu",
        },
        {
          name: "Giày Sneaker Nam Nữ Hot Trend",
          price: "720,000 VND",
          image:
            "https://tse2.mm.bing.net/th?id=OIP.DpkC3n4GINLmGQ847q8kswHaHa&pid=Api&P=0&h=180",
          alt: "Giày sneaker màu trắng phối xanh và đỏ, kiểu dáng thể thao",
        },
      ],
    };
  },
  computed: {
    filteredProducts() {
      if (!this.searchQuery.trim()) {
        return this.products;
      }
      const q = this.searchQuery.trim().toLowerCase();
      return this.products.filter((p) => p.name.toLowerCase().includes(q));
    },
  },
  methods: {
    onSearch() {
      // Không cần hành động đặc biệt ở đây vì filteredProducts tự động cập nhật
    },
  },
};
</script>

<style scoped>
/* Base Styles */
/* Đặt margin và padding của body về 0 để giao diện chiếm full chiều rộng */
html,
body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden; /* Ngăn cuộn ngang không mong muốn */
}

.haravan-product-page {
  font-family: sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333; /* A softer dark gray for text */
  min-height: 100vh; /* Đảm bảo trang chiếm ít nhất 100% chiều cao viewport */
  display: flex;
  flex-direction: column; /* Sắp xếp các phần tử theo chiều dọc */
}

/* Top Announcement Bar */
.top-bar {
  background-color: #1e3a8a; /* Darker blue */
  color: white;
  font-size: 0.75rem; /* 12px */
  padding: 0.25rem 1rem; /* 4px 16px */
  text-align: center;
  width: 100%; /* Chiếm full chiều rộng */
  box-sizing: border-box; /* Bao gồm padding trong chiều rộng */
}

/* Header */
.main-header {
  background-color: #1d4ed8; /* A vibrant blue */
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem; /* 12px 16px */
  gap: 1rem; /* 16px */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Subtle shadow */
  width: 100%; /* Chiếm full chiều rộng */
  box-sizing: border-box; /* Bao gồm padding trong chiều rộng */
}

@media (min-width: 768px) {
  .main-header {
    gap: 1.5rem; /* 24px */
    padding: 0.75rem 2rem; /* Tăng padding ngang trên màn hình lớn */
  }
}

.header-logo .logo-img {
  height: 3rem; /* 48px */
  width: auto;
}

.search-form {
  display: flex;
  flex-grow: 1;
  max-width: 40rem; /* Keeps search bar a reasonable size on large screens */
  border-radius: 0.375rem; /* 6px */
  overflow: hidden;
}

.search-input {
  flex-grow: 1;
  border: 1px solid #d1d5db; /* Light gray border */
  padding: 0.5rem 1rem; /* 8px 16px */
  font-size: 0.875rem; /* 14px */
  outline: none;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.search-input:focus {
  border-color: #3b82f6; /* Blue on focus */
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5); /* Blue ring on focus */
}

.search-button {
  background-color: #2563eb; /* A slightly darker blue for button */
  color: white;
  padding: 0.5rem 1.25rem; /* 8px 20px */
  font-size: 0.875rem; /* 14px */
  font-weight: 600; /* semi-bold */
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #1e40af; /* Darker blue on hover */
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 1rem; /* 16px */
  margin-left: auto;
}

@media (min-width: 768px) {
  .header-actions {
    gap: 1.5rem; /* 24px */
  }
}

.action-button {
  color: white;
  font-size: 1rem; /* 16px */
  display: flex;
  flex-direction: column; /* Sắp xếp icon và text theo chiều dọc */
  align-items: center;
  gap: 0.25rem; /* 4px */
  background: none;
  border: none;
  cursor: pointer;
  transition: color 0.3s ease;
}

.action-button:hover {
  color: #bfdbfe; /* Lighter blue on hover */
}

.action-button i {
  font-size: 1.125rem; /* 18px */
}

.action-text {
  font-weight: 500; /* medium */
  font-size: 0.75rem; /* Tăng kích thước chữ cho text */
  white-space: nowrap; /* Ngăn text bị xuống dòng */
}

/* Navigation Bar */
.main-nav {
  background-color: #2563eb; /* Blue */
  color: white;
  font-size: 0.875rem; /* 14px */
  font-weight: 600; /* semi-bold */
  padding: 0.75rem 1rem; /* 12px 16px */
  display: flex;
  flex-wrap: wrap;
  gap: 1rem 2rem; /* 16px 32px */
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05); /* Subtle shadow */
  width: 100%; /* Chiếm full chiều rộng */
  box-sizing: border-box; /* Bao gồm padding trong chiều rộng */
}

@media (min-width: 768px) {
  .main-nav {
    justify-content: flex-start;
    padding: 0.75rem 2rem; /* Tăng padding ngang trên màn hình lớn */
  }
}

.nav-link {
  color: white;
  text-decoration: none;
  text-transform: uppercase;
  letter-spacing: 0.05em; /* tracking-wide */
  transition: color 0.2s ease;
}

.nav-link:hover {
  color: #bfdbfe; /* Lighter blue on hover */
}

/* Main Content */
.main-content {
  flex-grow: 1; /* Cho phép main content chiếm hết không gian còn lại */
  padding: 2rem 1rem; /* 32px 16px */
  background-color: #f9fafb; /* Very light gray */
  width: 100%; /* Chiếm full chiều rộng */
  box-sizing: border-box; /* Bao gồm padding trong chiều rộng */
}

.section-title {
  font-size: 2.25rem; /* 36px */
  font-weight: 800; /* extra-bold */
  color: #1e3a8a; /* Darker blue */
  margin-bottom: 2rem; /* 32px */
  text-align: center;
  max-width: 80rem; /* Optional: Keep text width readable */
  margin-left: auto;
  margin-right: auto;
}

@media (min-width: 768px) {
  .section-title {
    text-align: left;
    padding-left: 1rem; /* Thêm padding để căn chỉnh với lưới sản phẩm */
  }
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); /* Responsive grid */
  gap: 1.5rem; /* 24px */
  max-width: 80rem; /* Giới hạn chiều rộng lưới sản phẩm */
  margin-left: auto;
  margin-right: auto;
  /* padding-left và padding-right đã được bao gồm trong main-content padding */
}

@media (min-width: 640px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (min-width: 1024px) {
  .product-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

.product-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  background-color: white;
  border-radius: 0.5rem; /* 8px */
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05); /* lg shadow */
  padding: 1.5rem; /* 24px */
  transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.product-card:hover {
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1); /* xl shadow */
  transform: translateY(-0.25rem); /* -4px */
}

.product-image {
  margin-bottom: 1rem; /* 16px */
  width: 13rem; /* 208px */
  height: 13rem; /* 208px */
  object-fit: contain;
}

.product-name {
  font-size: 1rem; /* 16px */
  font-weight: 700; /* bold */
  color: #1f2937; /* Very dark gray */
  margin-bottom: 0.5rem; /* 8px */
  line-height: 1.375; /* tight leading */
  min-height: 2.75rem; /* Đảm bảo chiều cao tối thiểu cho 2 dòng */
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* Giới hạn 2 dòng */
  -webkit-box-orient: vertical;
}

.product-price {
  font-size: 1.25rem; /* 20px */
  font-weight: 800; /* extra-bold */
  color: #dc2626; /* Red */
  margin-top: auto; /* Đẩy giá xuống cuối card */
}

/* Footer */
.main-footer {
  background-color: #1e3a8a; /* Darker blue */
  color: white;
  font-size: 0.875rem; /* 14px */
  padding: 2rem 1rem; /* 32px 16px */
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem; /* 32px */
  width: 100%; /* Chiếm full chiều rộng */
  box-sizing: border-box; /* Bao gồm padding trong chiều rộng */
}

@media (min-width: 768px) {
  .main-footer {
    grid-template-columns: repeat(3, 1fr);
    padding: 2rem 2rem; /* Tăng padding ngang trên màn hình lớn */
  }
}

.footer-section {
  max-width: 400px; /* Giới hạn chiều rộng của mỗi cột footer */
  margin: 0 auto; /* Căn giữa mỗi cột trên màn hình nhỏ */
}

.footer-heading {
  font-weight: 700; /* bold */
  margin-bottom: 1rem; /* 16px */
  font-size: 1rem; /* 16px */
  text-transform: uppercase;
  letter-spacing: 0.05em; /* tracking-wide */
  text-align: center; /* Căn giữa tiêu đề footer trên di động */
}

@media (min-width: 768px) {
  .footer-heading {
    text-align: left; /* Căn trái tiêu đề footer trên desktop */
  }
}

.footer-text,
.footer-list li {
  margin-bottom: 0.25rem; /* 4px */
  text-align: center; /* Căn giữa nội dung footer trên di động */
}

@media (min-width: 768px) {
  .footer-text,
  .footer-list li {
    text-align: left; /* Căn trái nội dung footer trên desktop */
  }
}

.footer-sub-heading {
  margin-top: 0.75rem; /* 12px */
  font-weight: 600; /* semi-bold */
}

.footer-list {
  list-style: disc;
  list-style-position: inside;
  padding: 0;
  margin: 0;
  line-height: 1.5; /* Increased line-height for better readability */
}

.footer-list li {
  margin-bottom: 0.5rem; /* 8px */
}
html,
body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden; /* Ngăn cuộn ngang không mong muốn */
}
</style>
