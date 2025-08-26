<template>
  <div>
    <MainHeader />

    <main>
      <div class="container">

        <!-- Banner -->
        <div class="row">
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


        <!-- 🔖 BLOG KHUYẾN MÃI / MÃ GIẢM GIÁ (chỉ hiển thị + copy) -->
        <section class="mt-4">
          <div class="d-flex align-items-center justify-content-between mb-2">
            <h3 class="h5 m-0">🎁 Khuyến mãi đang diễn ra</h3>
            <small class="text-muted" v-if="promosLoading">Đang tải mã…</small>
            <small class="text-muted" v-else>Hiện có {{ activePromos.length }} mã</small>
          </div>

          <div v-if="promosError" class="alert alert-warning py-2">
            Không tải được danh sách mã giảm giá. Bạn vẫn có thể xem sản phẩm bên dưới.
          </div>

          <div class="row g-3">
            <div
              v-for="p in activePromos"
              :key="p.id || p.code"
              class="col-12 col-sm-6 col-lg-4"
            >
              <div class="promo-card card h-100">
                <div class="card-body d-flex flex-column">
                  <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="badge bg-success">Coupon</span>
                    <small class="text-muted" v-if="p.quantity !== undefined">
                      Còn {{ p.quantity }} lượt
                    </small>
                  </div>

                  <h5 class="mb-1">{{ p.title || ('Mã ' + p.code) }}</h5>
                  <p class="mb-2">
                    <strong class="text-danger">Giảm {{ p.discountPercent }}%</strong><br />
                    <small class="text-muted">
                      HSD: {{ formatDateTime(p.expiryDate) }}
                    </small>
                  </p>

                  <div class="d-flex align-items-center gap-2 flex-wrap mb-3">
                    <code class="promo-code px-2 py-1">{{ p.code }}</code>
                    <button class="btn btn-sm btn-outline-secondary" @click="copyCode(p.code)">
                      Sao chép
                    </button>
                  </div>

                  <p class="text-secondary small mt-auto">
                    *Áp dụng tại giỏ hàng/checkout. Điều kiện khác có thể áp dụng.
                  </p>
                </div>
              </div>
            </div>

            <!-- Empty state -->
            <div v-if="!promosLoading && !activePromos.length" class="col-12">
              <div class="alert alert-light border">
                Hiện chưa có khuyến mãi nào. Xem sản phẩm bên dưới nhé!
              </div>
            </div>
          </div>
        </section>

        <!-- SẢN PHẨM BÁN CHẠY (lấy từ DB) -->
        <div class="section-title mt-4">SẢN PHẨM BÁN CHẠY</div>
        <div class="row g-3">

          <div
            class="col-md-3"
            v-for="prod in topSelling"
            :key="prod.id"
          >
            <div class="product-card">
              <div class="product-image">
                <img :src="getImageUrl(prod.imageUrl)" class="img-fluid" :alt="prod.name || 'Product Image'" loading="lazy" />
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

                <router-link :to="`/products/${prod.id}`" class="action-btn wishlist" :aria-label="`Chi tiết ${prod.name}`">

                  <i class="fas fa-info-circle"></i> Chi tiết
                </router-link>
              </div>
            </div>
          </div>

          <!-- Loading state cho top-selling -->
          <div v-if="productsLoading" class="col-12">
            <div class="alert alert-light border">Đang tải sản phẩm…</div>
          </div>
          <!-- Empty -->
          <div v-if="!productsLoading && !topSelling.length" class="col-12">
            <div class="alert alert-warning">Chưa có sản phẩm để hiển thị.</div>
          </div>
        </div>

        <!-- KHÁM PHÁ DANH MỤC (thay cho 'TẤT CẢ SẢN PHẨM') -->
        <div class="section-title">KHÁM PHÁ DANH MỤC</div>
        <div class="row g-3">

          <div
            class="col-12 col-sm-6 col-lg-4"
            v-for="cat in homeCategories"
            :key="cat.slug"
          >
            <router-link
              class="category-card d-block rounded overflow-hidden shadow-sm"
              :to="`/product?category=${encodeURIComponent(cat.slug)}`"
              :aria-label="`Xem danh mục ${cat.title}`"
            >
              <div
                class="category-cover"
                :style="{ backgroundImage: `url(${cat.image})` }"
              ></div>
              <div class="category-info d-flex justify-content-between align-items-center">
                <h3 class="h6 m-0">{{ cat.title }}</h3>
                <span class="category-cta">Xem ngay →</span>
              </div>
            </router-link>
          </div>
        </div>

        <!-- CTA: Xem tất cả -->
        <div class="text-center mt-4">
          <router-link to="/product" class="btn btn-primary px-4 py-2">
            Xem tất cả sản phẩm →
          </router-link>
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
  components: { MainHeader, MainFooter },
  data() {
    return {
      // khuyến mãi
      promosLoading: true,
      promosError: false,
      promos: [],

      // sản phẩm (từ DB)
      productsLoading: true,
      products: [],


      // danh mục điều hướng
      homeCategories: [
        {
          title: 'Áo Nam',
          slug: 'ao-nam',
          image:
            'https://images.unsplash.com/photo-1520975916090-3105956dac38?q=80&w=1200&auto=format&fit=crop'
        },
        {
          title: 'Quần Nam',
          slug: 'quan-nam',
          image:
            'https://images.unsplash.com/photo-1520975940400-3af35a6f1a5d?q=80&w=1200&auto=format&fit=crop'
        },
        {
          title: 'Giày Dép',
          slug: 'giay-dep',
          image:
            'https://images.unsplash.com/photo-1519741497674-611481863552?q=80&w=1200&auto=format&fit=crop'
        },
        {
          title: 'Phụ Kiện',
          slug: 'phu-kien',
          image:
            'https://images.unsplash.com/photo-1522312346375-d1a52e2b99b3?q=80&w=1200&auto=format&fit=crop'
        },
        {
          title: 'Bộ Sưu Tập Mới',
          slug: 'new-arrivals',
          image:
            'https://images.unsplash.com/photo-1539109136881-3be0616acf4b?q=80&w=1200&auto=format&fit=crop'
        },
        {
          title: 'Sale Sốc',
          slug: 'sale',
          image:
            'https://images.unsplash.com/photo-1460353581641-37baddab0fa2?q=80&w=1200&auto=format&fit=crop'
        }
      ]

    };
  },
  computed: {
    activePromos() {
      const now = Date.now();
      return (this.promos || [])
        .filter(p => p?.status === true)
        .filter(p => !p.expiryDate || new Date(p.expiryDate).getTime() > now)
        .filter(p => p.quantity == null || Number(p.quantity) > 0)
        .slice(0, 6); // tối đa 6 mã
    },
    // Chọn bán chạy: nếu có soldCount thì sắp xếp, không thì lấy 4 sản phẩm đầu
    topSelling() {
      if (!this.products || !this.products.length) return [];
      const hasSold = this.products.some(p => typeof p.soldCount === 'number');
      const sorted = hasSold
        ? [...this.products].sort((a, b) => (b.soldCount || 0) - (a.soldCount || 0))
        : [...this.products];
      return sorted.slice(0, 4);
    }
  },
  methods: {
    // ----- khuyến mãi -----
    fetchPromos() {
      this.promosLoading = true;
      this.promosError = false;
      fetch('http://localhost:8080/api/coupons')
        .then(r => { if (!r.ok) throw new Error(r.status); return r.json(); })
        .then(data => { this.promos = Array.isArray(data) ? data : []; })
        .catch(() => { this.promosError = true; })
        .finally(() => { this.promosLoading = false; });
    },
    copyCode(code) {
      navigator.clipboard?.writeText(code).then(() => {
        alert(`Đã sao chép mã: ${code}`);
      });
    },
    formatDateTime(dateStr) {
      if (!dateStr) return '—';
      try {
        const d = new Date(dateStr);
        return d.toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit', day: '2-digit', month: '2-digit', year: 'numeric' });
      } catch { return '—'; }
    },

    // ----- sản phẩm -----
    formatPrice(value) {

      if (!value && value !== 0) return '';
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    },
    getImageUrl(imagePath) {
      if (!imagePath) return '';
      if (imagePath.startsWith('http')) return imagePath;
      return `http://localhost:8080/images/${imagePath}`;
    },
    getDiscountPercent(prod) {

      const base = Number(prod.displayPrice || 0);
      const sale = Number(prod.displayDiscountPrice || 0);
      if (base > 0 && sale > 0 && sale < base) {
        const percent = ((base - sale) / base) * 100;

        return `-${Math.round(percent)}%`;
      }
      return null;
    },

    // tải sản phẩm từ DB
    fetchProducts() {
      this.productsLoading = true;
      fetch('http://localhost:8080/products')
        .then(res => {
          if (!res.ok) throw new Error(res.status);
          return res.json();
        })
        .then(data => {
          // chỉ lấy sản phẩm status = true
          this.products = (data || []).filter(p => p?.status);
        })
        .catch(err => {
          console.error('Lỗi tải sản phẩm:', err);
          this.products = [];
        })
        .finally(() => {
          this.productsLoading = false;
        });
    }
  },
  mounted() {
    this.fetchProducts();
    this.fetchPromos();

  },
};
</script>

<style scoped>

/* --- giữ nguyên style sản phẩm của bạn --- */

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
.product-card:hover .product-image img { transform: scale(1.05); }
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
.product-info { padding: 15px; }
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
.price-container { display: flex; align-items: center; gap: 10px; }
.current-price { font-size: 18px; font-weight: 700; color: #ff5252; }
.original-price { font-size: 14px; color: #999; text-decoration: line-through; }
.action-buttons {
  position: absolute; top: 50%; left: 0; right: 0;
  display: flex; justify-content: center; opacity: 0;
  transform: translateY(20px); transition: all .3s ease;
}
.product-card:hover .action-buttons { opacity: 1; transform: translateY(-50%); }
.action-btn {
  background: rgba(255, 255, 255, 0.9); border: none; padding: 10px 15px; margin: 0 5px;
  border-radius: 30px; font-size: 12px; font-weight: 600; cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); transition: all .3s ease; display: flex; align-items: center; text-decoration: none;
}
.action-btn:hover { background:#fff; transform: translateY(-2px); box-shadow: 0 4px 8px rgba(0,0,0,.2); }
.wishlist { color:#333; }
.wishlist:hover { color:#ff5252; }

/* 💅 promo block nhỏ gọn */
.promo-card { border: 1px dashed #e5e7eb; border-radius: 12px; }
.promo-code { background:#f3f4f6; border-radius:6px; }

/* Category tiles */
.category-card {
  text-decoration: none;
  background: #fff;
  border-radius: 12px;
  transition: transform .25s ease, box-shadow .25s ease;
}
.category-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 18px rgba(0,0,0,.12);
}
.category-cover {
  width: 100%;
  height: 190px;
  background-size: cover;
  background-position: center;
}
.category-info {
  padding: 12px 14px;
  border-top: 1px solid #f0f0f0;
  color: #1f2937;
}
.category-cta {
  font-size: .9rem;
  color: #2563eb;
  font-weight: 600;
}

.category-card:hover .category-cta { text-decoration: underline; }

</style>
