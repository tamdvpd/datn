<template>
  <div class="haravan-product-page">
    <MainHeader />

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

    <MainFooter />
  </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

export default {
  name: "HaravanProductPage",
  components: {
    MainHeader,
    MainFooter
  },
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
    onSearch() {},
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
  background-color: #00c0f1; /* Darker blue */
  color: white;
  font-size: 0.75rem; /* 12px */
  padding: 0.25rem 1rem; /* 4px 16px */
  text-align: center;
  width: 100%; /* Chiếm full chiều rộng */
  box-sizing: border-box; /* Bao gồm padding trong chiều rộng */
}

/* Header */
.main-header {
  background-color: #00c0f1; /* A vibrant blue */
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
  background-color: #00c0f1; /* Darker blue on hover */
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
  background-color: #00c0f1; /* Blue */
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
</style>