<template>
  <div class="haravan-product-page">
    <MainHeader />

    <main class="main-content">
      <h2 class="section-title">SẢN PHẨM</h2>

      <!-- Search + Sort -->
      <section class="toolbar" aria-label="Bộ lọc sản phẩm">
        <div class="toolbar-inner">
          <div class="search-wrap">
            <input
              v-model.trim="searchQuery"
              @keyup.enter="applyFilters"
              type="text"
              class="search-input"
              placeholder="Tìm kiếm sản phẩm..."
              aria-label="Tìm kiếm sản phẩm"
            />
            <button class="search-button" @click="applyFilters" aria-label="Tìm">
              Tìm
            </button>
          </div>

          <select class="sort-select" v-model="sortOption" @change="applyFilters" aria-label="Sắp xếp">
            <option value="">Sắp xếp</option>
            <option value="name-asc">Tên: A → Z</option>
            <option value="name-desc">Tên: Z → A</option>
            <option value="price-asc">Giá: Thấp → Cao</option>
            <option value="price-desc">Giá: Cao → Thấp</option>
          </select>
        </div>

        <p class="hint" v-if="!loading && filteredProducts.length">
          Tìm thấy <strong>{{ filteredProducts.length }}</strong> sản phẩm
        </p>
      </section>

      <!-- Loading skeleton -->
      <section v-if="loading" class="product-grid" aria-busy="true">
        <article v-for="n in 8" :key="'skeleton-'+n" class="product-card skeleton">
          <div class="image-skeleton"></div>
          <div class="line-skeleton w-80"></div>
          <div class="line-skeleton w-40"></div>
        </article>
      </section>

      <!-- Empty state -->
      <section v-else-if="!filteredProducts.length" class="empty-state">
        <p>Không có sản phẩm phù hợp. Hãy thử từ khoá khác hoặc xoá bộ lọc.</p>
        <button class="search-button" @click="resetFilters">Xoá bộ lọc</button>
      </section>

      <!-- Product grid -->
      <section v-else aria-label="Danh sách sản phẩm" class="product-grid">
        <article
          v-for="product in filteredProducts"
          :key="product.id"
          class="product-card"
        >
          <router-link
            class="image-link"
            :to="`/products/${product.id}`"
            :aria-label="`Xem chi tiết ${product.name}`"
          >
            <img
              :alt="product.alt || product.name"
              class="product-image"
              :src="getImageUrl(product.image)"
            />
            <span v-if="discountPercent(product)" class="discount-badge">
              -{{ discountPercent(product) }}%
            </span>
          </router-link>

          <router-link :to="`/products/${product.id}`" class="product-name">
            {{ product.name }}
          </router-link>

          <p class="price-row">
            <span class="price-current">
              {{ formatVnd(product.discountPrice ?? product.price) }}
            </span>
            <span v-if="product.discountPrice" class="price-original">
              {{ formatVnd(product.price) }}
            </span>
          </p>
        </article>
      </section>
    </main>

    <MainFooter />
  </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

const API_BASE = import.meta?.env?.VITE_API_BASE || 'http://localhost:8080';

export default {
  name: 'HaravanProductPage',
  components: { MainHeader, MainFooter },
  data() {
    return {
      loading: true,
      error: '',
      searchQuery: '',
      sortOption: '',
      products: [],       // raw từ BE
      viewProducts: [],   // sau khi map/chuẩn hoá
      filteredProducts: []
    };
  },
  methods: {
    async fetchProducts() {
      this.loading = true;
      this.error = '';
      try {
        const res = await fetch(`${API_BASE}/products`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();

        // Lọc status = true & chuẩn hoá trường để FE dùng thống nhất
        this.viewProducts = (data || [])
          .filter(p => p?.status !== false) // nếu có trường status thì chỉ lấy true
          .map(p => ({
            id: p.id,
            name: p.name,
            image: p.imageUrl, // BE trả imageUrl (đường dẫn tương đối) -> sẽ ghép ở getImageUrl
            price: p.displayPrice ?? p.price ?? 0,
            discountPrice: p.displayDiscountPrice ?? p.discount_price ?? null,
            alt: p.name
          }));

        this.applyFilters();
      } catch (e) {
        this.error = 'Không tải được sản phẩm.';
        console.error('Fetch products error:', e);
      } finally {
        this.loading = false;
      }
    },
    getImageUrl(imagePath) {
      if (!imagePath) return '';
      if (imagePath.startsWith('http')) return imagePath;
      return `${API_BASE}/images/${imagePath}`;
    },
    formatVnd(val) {
      if (val == null) return '';
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val);
    },
    discountPercent(p) {
      const base = Number(p?.price || 0);
      const sale = Number(p?.discountPrice || 0);
      if (base > 0 && sale > 0 && sale < base) {
        return Math.round(((base - sale) / base) * 100);
      }
      return null;
    },
    applyFilters() {
      let list = [...this.viewProducts];

      // search
      const q = this.searchQuery.trim().toLowerCase();
      if (q) list = list.filter(p => p.name.toLowerCase().includes(q));

      // sort
      switch (this.sortOption) {
        case 'name-asc':
          list.sort((a, b) => a.name.localeCompare(b.name));
          break;
        case 'name-desc':
          list.sort((a, b) => b.name.localeCompare(a.name));
          break;
        case 'price-asc':
          list.sort((a, b) => (a.discountPrice ?? a.price) - (b.discountPrice ?? b.price));
          break;
        case 'price-desc':
          list.sort((a, b) => (b.discountPrice ?? b.price) - (a.discountPrice ?? a.price));
          break;
      }

      this.filteredProducts = list;
    },
    resetFilters() {
      this.searchQuery = '';
      this.sortOption = '';
      this.applyFilters();
    }
  },
  mounted() {
    this.fetchProducts();
  }
};
</script>

<style scoped>
/* ===== Layout base ===== */
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
}
.haravan-product-page {
  font-family: system-ui, -apple-system, Segoe UI, Roboto, Helvetica, Arial, sans-serif;
  color: #333;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.main-content {
  flex-grow: 1;
  padding: 2rem 1rem;
  background-color: #f9fafb;
  width: 100%;
  box-sizing: border-box;
}

/* ===== Title ===== */
.section-title {
  font-size: 2.25rem;
  font-weight: 800;
  color: #1e3a8a;
  margin-bottom: 1rem;
  text-align: center;
  max-width: 80rem;
  margin-left: auto;
  margin-right: auto;
}
@media (min-width: 768px) {
  .section-title {
    text-align: left;
    padding-left: 1rem;
  }
}

/* ===== Toolbar ===== */
.toolbar {
  max-width: 80rem;
  margin: 0 auto 1rem;
  padding: 0 1rem;
}
.toolbar-inner {
  display: grid;
  gap: 0.75rem;
  grid-template-columns: 1fr auto;
  align-items: center;
}
.search-wrap {
  display: flex;
  gap: 0.5rem;
}
.search-input {
  flex: 1;
  border: 1px solid #d1d5db;
  padding: 0.5rem 0.75rem;
  font-size: 0.95rem;
  outline: none;
  transition: border-color 0.25s, box-shadow 0.25s;
  border-radius: 6px;
  background: #fff;
}
.search-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59,130,246,.25);
}
.search-button {
  background-color: #2563eb;
  color: #fff;
  padding: 0.5rem 1rem;
  font-weight: 600;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color .2s ease;
}
.search-button:hover { background-color: #1d4ed8; }
.sort-select {
  border: 1px solid #d1d5db;
  background: #fff;
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
}
.hint { color: #6b7280; margin: .5rem 0 0 0; }

/* ===== Grid ===== */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px,1fr));
  gap: 1.5rem;
  max-width: 80rem;
  margin: 1rem auto 0;
  padding: 0 1rem;
}
@media (min-width: 640px) { .product-grid { grid-template-columns: repeat(2, 1fr); } }
@media (min-width: 768px) { .product-grid { grid-template-columns: repeat(3, 1fr); } }
@media (min-width: 1024px) { .product-grid { grid-template-columns: repeat(4, 1fr); } }

/* ===== Card ===== */
.product-card {
  display: flex;
  flex-direction: column;
  text-align: center;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 14px rgba(0,0,0,.06);
  padding: 1rem;
  transition: box-shadow .25s ease, transform .25s ease;
  position: relative;
}
.product-card:hover {
  box-shadow: 0 14px 24px rgba(0,0,0,.08);
  transform: translateY(-3px);
}
.image-link { display: block; position: relative; }
.product-image {
  margin-bottom: 0.75rem;
  width: 13rem;
  height: 13rem;
  object-fit: contain;
  max-width: 100%;
  display: block;
  margin-left: auto;
  margin-right: auto;
}
.discount-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: #ef4444;
  color: #fff;
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 0.75rem;
  font-weight: 700;
}
.product-name {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  font-size: 1rem;
  font-weight: 700;
  color: #1f2937;
  line-height: 1.375;
  min-height: 2.75rem;
  margin: 0 0 .25rem 0;
  text-decoration: none;
}
.product-name:hover { text-decoration: underline; }

/* ===== Price ===== */
.price-row {
  margin-top: auto;
  display: flex;
  justify-content: center;
  gap: .5rem;
  align-items: baseline;
}
.price-current {
  font-size: 1.15rem;
  font-weight: 800;
  color: #dc2626;
}
.price-original {
  font-size: .95rem;
  color: #9ca3af;
  text-decoration: line-through;
}

/* ===== Skeleton ===== */
.skeleton { overflow: hidden; }
.image-skeleton,
.line-skeleton {
  background: linear-gradient(90deg, #eee, #f5f5f5, #eee);
  background-size: 200% 100%;
  animation: shimmer 1.25s infinite;
  border-radius: 8px;
}
.image-skeleton { width: 13rem; height: 13rem; margin: 0 auto .75rem; }
.line-skeleton { height: 14px; margin: 6px auto; }
.w-80 { width: 80%; }
.w-40 { width: 40%; }
@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* ===== Empty ===== */
.empty-state {
  max-width: 80rem;
  margin: 1rem auto 0;
  padding: 0 1rem;
  text-align: center;
  color: #6b7280;
}
</style>
