<template>
  <div>
    <MainHeader />
    <main>
      <div class="container mt-4">
        <!-- Sidebar và Banner -->
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
              class="img-fluid rounded shadow"
              alt="Banner"
            />
          </div>
        </div>

        <!-- 🔥 Sản phẩm bán chạy -->
        <div class="section-title">SẢN PHẨM BÁN CHẠY</div>
        <div class="row g-3">
          <div class="col-md-3" v-for="product in hotProducts" :key="product.id">
            <router-link :to="`/products/${product.id}`" class="product-card-link">
              <div class="product-card">
                <div class="image-wrapper">
                  <img :src="product.imageUrl" alt="Ảnh sản phẩm" />
                </div>
                <div class="product-info mt-2">
                  <div class="product-name">{{ product.name }}</div>
                  <div class="product-price">Giá: {{ formatPrice(product.price) }} VNĐ</div>
                </div>
              </div>
            </router-link>
          </div>
        </div>

        <!-- 💸 Sản phẩm sale -->
        <div class="section-title">SẢN PHẨM SALE</div>
        <div class="row g-3">
          <div class="col-md-3" v-for="product in saleProducts" :key="product.id">
            <router-link :to="`/products/${product.id}`" class="product-card-link">
              <div class="product-card">
                <div class="image-wrapper">
                  <img :src="product.imageUrl" alt="Ảnh sản phẩm" />
                </div>
                <div class="product-info mt-2">
                  <div class="product-name">{{ product.name }}</div>
                  <div class="product-price">Giá: {{ formatPrice(product.price) }} VNĐ</div>
                </div>
              </div>
            </router-link>
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
      hotProducts: [],
      saleProducts: [],
    };
  },
  mounted() {
    this.fetchHotProducts();
    this.fetchSaleProducts();
  },
  methods: {
    fetchHotProducts() {
      axios.get('http://localhost:8080/products/hot')
        .then(res => {
          this.hotProducts = res.data;
        })
        .catch(err => console.error('Lỗi khi tải hot products:', err));
    },
    fetchSaleProducts() {
      axios.get('http://localhost:8080/products/sale')
        .then(res => {
          this.saleProducts = res.data;
        })
        .catch(err => console.error('Lỗi khi tải sale products:', err));
    },
    formatPrice(price) {
      return new Intl.NumberFormat('vi-VN').format(price);
    }
  }
};
</script>

<style scoped>
.product-card-link {
  text-decoration: none;
  color: inherit;
}
.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 8px;
  background: white;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.image-wrapper {
  width: 100%;
  height: 250px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}
.image-wrapper img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}
.product-name {
  font-weight: bold;
  font-size: 1rem;
  text-align: center;
}
.product-price {
  color: red;
  font-weight: bold;
  text-align: center;
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
