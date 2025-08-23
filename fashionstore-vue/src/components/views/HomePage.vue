<template>
  <div>
    <MainHeader />

    <main>
      <div class="container">
        <div class="row">

          <!-- Banner -->
         <img
          src="https://bizweb.dktcdn.net/100/462/587/themes/880841/assets/slider_1.jpg?1755556746118"
          style="width: 100%; height: 400px; object-fit: cover;"
          class="rounded shadow"
          alt="Banner"
/>
        </div>
<!-- tìm kiếm -->
      <div class="row mb-3 search-sort-bar mt-5">
  <div class="col-md-3 mb-2 d-flex justify-content-center">
    <input
      type="text"
      class="form-control search-input"
      placeholder="Tìm kiếm sản phẩm..."
      v-model="keyword"
      @keyup.enter="applyFilters"
    />
    <button class="btn btn-primary ms-2" @click="applyFilters">
      <i class="fas fa-search"></i> Tìm
    </button>
  </div>
  <div class="col-md-3 mb-2">
    <select class="form-select sort-select" v-model="sortOption" @change="applyFilters">
      <option value="">Sắp xếp</option>
      <option value="name-asc">Tên: sản phẩm A → Z</option>
      <option value="name-desc">Tên: sản phẩm Z → A</option>
      <option value="price-asc">Giá: Thấp → Cao</option>
      <option value="price-desc">Giá: Cao → Thấp</option>
    </select>
  </div>
</div>


        <!-- SẢN PHẨM BÁN CHẠY -->
        <div class="section-title">SẢN PHẨM BÁN CHẠY</div>
        <div class="row g-3">
          <div class="col-md-3" v-for="prod in filteredProducts.slice(0, 4)" :key="prod.id">
            <div class="product-card">
              <div class="product-image">
                <img :src="getImageUrl(prod.imageUrl)" class="img-fluid" alt="Product Image" />
                <div class="discount-badge" v-if="getDiscountPercent(prod)">
                  {{ getDiscountPercent(prod) }}
                </div>
              </div>

              <div class="product-info">
                <h3 class="product-name">{{ prod.name }}</h3>
                <div class="price-container">
                  <span class="current-price" v-if="prod.displayDiscountPrice">
                    {{ formatPrice(prod.displayDiscountPrice) }}
                  </span>
                  <span class="current-price" v-else-if="prod.displayPrice">
                    {{ formatPrice(prod.displayPrice) }}
                  </span>
                  <span class="original-price" v-if="prod.displayDiscountPrice">
                    {{ formatPrice(prod.displayPrice) }}
                  </span>
                </div>
              </div>

              <div class="action-buttons">
                <router-link :to="`/products/${prod.id}`" class="action-btn wishlist">
                  <i class="fas fa-info-circle"></i> Chi tiết
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- TẤT CẢ SẢN PHẨM -->
        <div class="section-title">TẤT CẢ SẢN PHẨM</div>
        <div class="row g-3">
          <div class="col-md-3" v-for="prod in filteredProducts" :key="'all-' + prod.id">
            <div class="product-card">
              <div class="product-image">
                <img :src="getImageUrl(prod.imageUrl)" class="img-fluid" alt="Product Image" />
                <div class="discount-badge" v-if="getDiscountPercent(prod)">
                  {{ getDiscountPercent(prod) }}
                </div>
              </div>

              <div class="product-info">
                <h3 class="product-name">{{ prod.name }}</h3>
                <div class="price-container">
                  <span class="current-price" v-if="prod.displayDiscountPrice">
                    {{ formatPrice(prod.displayDiscountPrice) }}
                  </span>
                  <span class="current-price" v-else-if="prod.displayPrice">
                    {{ formatPrice(prod.displayPrice) }}
                  </span>
                  <span class="original-price" v-if="prod.displayDiscountPrice">
                    {{ formatPrice(prod.displayPrice) }}
                  </span>
                </div>
              </div>

              <div class="action-buttons">
                
                <router-link :to="`/products/${prod.id}`" class="action-btn wishlist">
                  <i class="fas fa-info-circle"></i> Chi tiết
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
      filteredProducts: [],
      keyword: '',
      sortOption: '',
    };
  },
  methods: {
    formatPrice(value) {
      if (!value) return '';
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    },
    addToCart(product) {
      // Logic thêm vào giỏ
      console.log('Thêm vào giỏ:', product.name);
    },
    getImageUrl(imagePath) {
      if (!imagePath) return '';
      if (imagePath.startsWith('http')) return imagePath;
      return `http://localhost:8080/images/${imagePath}`;
    },
    getDiscountPercent(prod) {
      if (prod.displayPrice && prod.displayDiscountPrice && prod.displayDiscountPrice < prod.displayPrice) {
        const percent = ((prod.displayPrice - prod.displayDiscountPrice) / prod.displayPrice) * 100;
        return `-${Math.round(percent)}%`;
      }
      return null;
    },
    applyFilters() {
      let result = this.products.filter(p =>
        p.name.toLowerCase().includes(this.keyword.toLowerCase())
      );

      if (this.sortOption === 'name-asc') result.sort((a, b) => a.name.localeCompare(b.name));
      else if (this.sortOption === 'name-desc') result.sort((a, b) => b.name.localeCompare(a.name));
      else if (this.sortOption === 'price-asc')
        result.sort((a, b) => (a.displayDiscountPrice || a.displayPrice) - (b.displayDiscountPrice || b.displayPrice));
      else if (this.sortOption === 'price-desc')
        result.sort((a, b) => (b.displayDiscountPrice || b.displayPrice) - (a.displayDiscountPrice || a.displayPrice));

      this.filteredProducts = result;
    }
  },
  mounted() {
    fetch('http://localhost:8080/products')
      .then(res => res.json())
      .then(data => {
        this.products = data.filter(p => p.status);
        this.filteredProducts = this.products; // hiển thị mặc định
      })
      .catch(err => console.error('Lỗi tải sản phẩm:', err));
  },
};
</script>

<style scoped>
/* --- giữ nguyên toàn bộ style từ file cũ --- */
.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-top: 20px;
  margin-bottom: 20px;
  text-transform: uppercase;
  border-bottom: 2px solid #ddd;
  padding-bottom: 5px;
  
}

.product-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
  transition: transform 0.3s ease;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.product-image {
  width: 100%;
  height: 280px;
  position: relative;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.discount-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #ff5252;
  color: white;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
  height: 40px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.price-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.current-price {
  font-size: 18px;
  font-weight: 700;
  color: #ff5252;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.action-buttons {
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.3s ease;
}

.product-card:hover .action-buttons {
  opacity: 1;
  transform: translateY(-50%);
}

.action-btn {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  padding: 10px 15px;
  margin: 0 5px;
  border-radius: 30px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  text-decoration: none;
}

.action-btn:hover {
  background: #fff;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.buy-now {
  color: #fff;
  background: #ff5252;
}

.wishlist {
  color: #333;
}

.action-btn i {
  margin-right: 5px;
  font-size: 14px;
}

.wishlist:hover {
  color: #ff5252;
}
</style>
