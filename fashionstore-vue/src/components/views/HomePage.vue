<template>
  <div>
    <MainHeader />
    <main>
      <div class="container mt-4">
        <div class="row">
          <div class="col-4">
            <ul class="list-group">
              <button><li class="list-group-item">Deal sốc hội viên</li></button>
              <button><li class="list-group-item">Áo</li></button>
              <button><li class="list-group-item">Set bộ</li></button>
              <button><li class="list-group-item">Quần</li></button>
              <button><li class="list-group-item">Phụ kiện</li></button>
              <button><li class="list-group-item">Sản phẩm khác</li></button>
            </ul>
          </div>
          <div class="col-8">
            <img
              src="@/assets/img/banner.jpg"
              width="100%"
              class="img-fluid rounded shadow"
              alt="Banner"
            />
          </div>
        </div>

        <div class="section-title">SẢN PHẨM BÁN CHẠY</div>

        <div class="row g-3">
          <div
            class="col-md-3"
            v-for="prod in products.slice(0, 4)"
            :key="prod.id"
          >
            <div class="position-relative product-card p-2 border rounded">
              <img
                :src="getImageUrl(prod.imageUrl)"
                class="img-fluid"
                alt="Product Image"
              />
              <div class="mt-2">
                <div class="product-name">{{ prod.name }}</div>
                <div class="product-price">Giá: {{ formatPrice(prod.price) }}</div>
              </div>
              <div class="mt-2 d-flex justify-content-between">
                <button
                  class="btn btn-sm btn-outline-primary"
                  @click="addToWishlist(prod)"
                >
                  ❤️ Yêu thích
                </button>
                <router-link
                  :to="`/products/${prod.id}`"
                  class="btn btn-sm btn-outline-success"
                >
                  🔍 Xem chi tiết
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <div class="section-title">TẤT CẢ SẢN PHẨM</div>

        <div class="row g-3">
          <div
            class="col-md-3"
            v-for="prod in products"
            :key="'all-' + prod.id"
          >
            <div class="position-relative product-card p-2 border rounded">
              <img
                :src="getImageUrl(prod.imageUrl)"
                class="img-fluid"
                alt="Product Image"
              />
              <div class="mt-2">
                <div class="product-name">{{ prod.name }}</div>
                <div class="product-price">Giá: {{ formatPrice(prod.price) }}</div>
              </div>
              <div class="mt-2 d-flex justify-content-between">
                <button
                  class="btn btn-sm btn-outline-primary"
                  @click="addToWishlist(prod)"
                >
                  ❤️ Yêu thích
                </button>
                <router-link
                  :to="`/products/${prod.id}`"
                  class="btn btn-sm btn-outline-success"
                >
                  🔍 Xem chi tiết
                </router-link>
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
    };
  },
  methods: {
    getImageUrl(path) {
      if (!path) return require('@/assets/img/default-avatar.png');
      if (path.startsWith('http')) return path;
      return `http://localhost:8080${path}`;
    },
    formatPrice(value) {
      if (!value) return 'Liên hệ';
      return new Intl.NumberFormat('vi-VN').format(value) + ' VNĐ';
    },
    addToWishlist(product) {
      alert(`Đã thêm "${product.name}" vào mục yêu thích!`);
      // Hoặc lưu vào Vuex/store nếu bạn muốn
    },
  },
  mounted() {
    fetch('http://localhost:8080/products')
      .then(res => res.json())
      .then(data => {
        this.products = data;
      })
      .catch(err => console.error('Lỗi tải sản phẩm:', err));
  },
};
</script>

<style scoped>
.product-card img {
  width: 300px;
  height: 400px;
}
.product-name {
  font-weight: bold;
}
.product-price {
  color: red;
  font-weight: bold;
}
.section-title {
  background-color: #00b5e2;
  color: white;
  margin-top: 20px;
  padding: 8px 16px;
  font-size: 1.2rem;
  font-weight: bold;
  display: inline-block;
  margin-bottom: 20px;
}
</style>
