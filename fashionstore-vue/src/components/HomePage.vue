<template>
  <div>
    <MainHeader />
    <main>
      <div class="container mt-4">
        <!-- Danh mục bên trái và banner -->
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

        <!-- Sản phẩm bán chạy -->
        <div class="section-title">SẢN PHẨM BÁN CHẠY</div>
        <div class="row g-3">
          <div class="col-md-3" v-for="product in bestSellers" :key="product.id">
            <div class="position-relative product-card">
              <img :src="product.imageUrl" class="img-fluid" alt="Sản phẩm" />
              <div class="mt-2">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-price">{{ formatPrice(product.price) }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Sản phẩm sale -->
        <div class="section-title">SẢN PHẨM SALE</div>
        <div class="row g-3">
          <div class="col-md-3" v-for="product in saleProducts" :key="product.id">
            <div class="position-relative product-card">
              <img :src="product.imageUrl" class="img-fluid" alt="Sản phẩm" />
              <div class="mt-2">
                <div class="product-name">{{ product.name }}</div>
                <div class="product-price">{{ formatPrice(product.price) }}</div>
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
      bestSellers: [],
      saleProducts: [],
    };
  },
  methods: {
    fetchProducts() {
      axios.get('http://localhost:8080/products/with-price')
        .then(res => {
          const all = res.data;

          // Chia 4 sản phẩm đầu và 4 cuối làm ví dụ
          this.bestSellers = all.slice(0, 4);
          this.saleProducts = all.slice(4, 8);
        })
        .catch(err => {
          console.error('Lỗi khi tải sản phẩm:', err);
        });
    },
    formatPrice(price) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
      }).format(price);
    }
  },
  mounted() {
    this.fetchProducts();
  }
};
</script>

<style scoped>
.product-card img {
  width: 100%;
  height: 300px;
  object-fit: cover;
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
