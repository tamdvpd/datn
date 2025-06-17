<template>
  <div class="haravan-product-page">
    <MainHeader @search="onSearch" />

    <main class="main-content">
      <h2 class="section-title">TẤT CẢ SẢN PHẨM</h2>
      <section class="product-grid">
        <router-link
          v-for="product in filteredProducts"
          :key="product.id"
          :to="`/products/${product.id}`"
          class="product-card text-decoration-none"
        >
          <img :src="product.imageUrl" :alt="product.name" class="product-image" />
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-price">{{ formatPrice(product.price) }} VNĐ</p>
        </router-link>
      </section>
    </main>

    <MainFooter />
  </div>
</template>

<script>
import axios from 'axios'
import MainHeader from '@/components/MainHeader.vue'
import MainFooter from '@/components/MainFooter.vue'

export default {
  name: "ProductsPage",
  components: {
    MainHeader,
    MainFooter,
  },
  data() {
    return {
      products: [],
      searchQuery: '',
    }
  },
  computed: {
    filteredProducts() {
      const q = this.searchQuery.toLowerCase().trim()
      if (!q) return this.products
      return this.products.filter(p => p.name.toLowerCase().includes(q))
    }
  },
  mounted() {
    this.fetchProducts()
  },
  methods: {
    async fetchProducts() {
      try {
        const res = await axios.get('http://localhost:8080/products')
        this.products = res.data
      } catch (err) {
        console.error('Lỗi tải sản phẩm:', err)
      }
    },
    onSearch(query) {
      this.searchQuery = query
    },
    formatPrice(price) {
      return new Intl.NumberFormat('vi-VN').format(price)
    }
  }
}
</script>

<style scoped>
.main-content {
  padding: 2rem 1rem;
  background-color: #f9fafb;
}

.section-title {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 2rem;
  color: #1e3a8a;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 1.5rem;
  max-width: 1200px;
  margin: 0 auto;
}

.product-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
  margin-bottom: 1rem;
  border-radius: 6px;
}

.product-name {
  font-weight: bold;
  font-size: 1rem;
  margin-bottom: 0.5rem;
  min-height: 2.5em;
}

.product-price {
  color: red;
  font-weight: bold;
  font-size: 1.1rem;
}
</style>
