<template>
  <div>
    <MainHeader />
    <main>
      <div class="container mt-4">
        <!-- Sidebar và banner -->
        <div class="row">
          <div class="col-md-4 mb-3">
            <ul class="list-group">
              <li class="list-group-item">Deal sốc hội viên</li>
              <li class="list-group-item">Áo</li>
              <li class="list-group-item">Set bộ</li>
              <li class="list-group-item">Quần</li>
              <li class="list-group-item">Phụ kiện</li>
              <li class="list-group-item">Sản phẩm khác</li>
            </ul>
          </div>
          <div class="col-md-8 mb-4">
            <img
              src="@/assets/img/banner.jpg"
              class="img-fluid rounded shadow w-100"
              alt="Banner"
            />
          </div>
        </div>

        <!-- Sản phẩm nổi bật từ API -->
        <div class="section-title">🛍️ SẢN PHẨM NỔI BẬT</div>

        <div v-if="loading" class="text-muted">Đang tải sản phẩm...</div>
        <div v-else-if="products.length === 0" class="text-danger">Không có sản phẩm nào.</div>

        <div class="row g-4" v-else>
          <div class="col-md-6 col-xl-4" v-for="product in products" :key="product.id">
            <div class="card h-100 shadow-sm">
              <img :src="resolveImageUrl(product.imageUrl)" class="card-img-top" style="height: 250px; object-fit: cover;" />
              <div class="card-body">
                <h5 class="card-title">{{ product.name }}</h5>
                <p class="card-text">{{ product.description }}</p>
                <p class="text-muted small">📂 {{ product.category?.name }}</p>
                <p class="text-muted small">📅 {{ formatDate(product.createdAt) }}</p>
                <!-- Nút xem chi tiết -->
                <button class="btn btn-outline-primary btn-sm" @click="viewProduct(product.id)">
                  Xem chi tiết
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <MainFooter />
  </div>
</template>

<script>
import axios from 'axios';
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

export default {
  name: 'HomePage',
  components: {
    MainHeader,
    MainFooter,
  },
  data() {
    return {
      products: [],
      loading: true,
    };
  },
  methods: {
    async fetchProducts() {
      this.loading = true;
      try {
        const res = await axios.get('http://localhost:8080/products');
        this.products = res.data; // Gán dữ liệu sản phẩm
      } catch (err) {
        console.error('Lỗi khi tải sản phẩm:', err);
        this.products = [];
      } finally {
        this.loading = false;
      }
    },
    resolveImageUrl(url) {
      return url && url.startsWith('http') ? url : `http://localhost:8080${url}`;
    },
    formatDate(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString('vi-VN') : '—';
    },
    viewProduct(id) {
      this.$router.push(`/productDetails/${id}`); // Điều hướng đến trang chi tiết
    }
  },
  mounted() {
    this.fetchProducts(); // Gọi phương thức để tải sản phẩm khi component được mount
  }
};
</script>

<style scoped>
.section-title {
  background-color: #00b5e2;
  color: white;
  padding: 10px 20px;
  font-size: 1.25rem;
  font-weight: bold;
  margin: 30px 0 20px;
  display: inline-block;
  border-radius: 6px;
}
.card-img-top {
  border-bottom: 1px solid #ddd;
}
</style>