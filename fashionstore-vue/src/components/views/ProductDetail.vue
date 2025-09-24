<template>
  <div>
    <MainHeader />
    <main class="container mt-4">
      <div v-if="product" class="row g-4">
        <!-- Ảnh sản phẩm + thumbnails -->
        <div class="col-lg-6 col-md-12">
          <div class="bg-white p-3 rounded shadow-sm h-100 text-center">
            <!-- Ảnh lớn -->
            <div class="mb-3 d-flex align-items-center justify-content-center">
              <img :src="getImageUrl(selectedImage)" class="product-image-large" :alt="product.name" />
            </div>

            <!-- Ảnh nhỏ bên dưới -->
            <div class="d-flex justify-content-center gap-2 flex-wrap">
              <img v-for="(detail, index) in productDetails.slice(0, 4)" :key="index"
                :src="getImageUrl(detail.imageUrl)" @click="selectedImage = getImageUrl(detail.imageUrl)"
                class="rounded border"
                :class="{ 'border-primary': selectedImage === getImageUrl(detail.imageUrl) }"
                style="width: 60px; height: 60px; object-fit: cover; cursor: pointer;" />
            </div>
          </div>
        </div>

        <!-- Thông tin sản phẩm -->
        <div class="col-lg-6 col-md-12">
          <div class="bg-white p-4 rounded shadow-sm h-100">
            <h2 class="fw-bold mb-2">{{ product.name }}</h2><br>
            <p class="text-muted mb-1">Thương hiệu: {{ product.brand || 'Đang cập nhật' }}</p><br>

            <!-- Hiển thị giá -->
            <p class="fs-4 text-danger mb-2">
              Giá khuyến mãi: {{ formatPrice(selectedDetail?.discountPrice || selectedDetail?.price || 0) }}
            </p>
            <p v-if="selectedDetail?.discountPrice && selectedDetail?.discountPrice < selectedDetail?.price"
              class="text-muted text-decoration-line-through">
              Giá gốc: {{ formatPrice(selectedDetail.price) }}
            </p>

            <div class="row mb-3">
              <!-- Chọn Màu -->
              <div class="col-md-3 col-6">
                <label class="form-label fw-semibold small">Chọn Màu:</label>
                <select v-model="selectedColor" class="form-select form-select-sm" @change="onColorChange">
                  <option disabled value="">-- Chọn Màu --</option>
                  <option v-for="color in uniqueColors" :key="color">{{ color }}</option>
                </select>
              </div>

              <!-- Chọn Size -->
              <div class="col-md-3 col-6">
                <label class="form-label fw-semibold small">Chọn Size:</label>
                <select v-model="selectedSize" class="form-select form-select-sm" @change="onSizeChange"
                  :disabled="!selectedColor">
                  <option disabled value="">-- Chọn Size --</option>
                  <option v-for="size in filteredSizes" :key="size">{{ size }}</option>
                </select>
              </div>
            </div>

            <!-- Nhập số lượng -->
            <div class="mb-2 w-25">
              <label class="form-label fw-semibold small">Số lượng:</label>
              <input type="number" class="form-control form-control-sm" v-model.number="selectedQuantity" min="1" /><br>
            </div>

            <!-- Nút hành động -->
            <div class="d-flex flex-wrap gap-2 mt-3">
              <button class="btn btn-outline-primary btn-lg shadow-sm d-flex align-items-center gap-2 px-4 py-2"
                @click="addToCart" :disabled="!selectedDetail || selectedQuantity <= 0">
                <i class="bi bi-cart-plus-fill fs-5"></i>
                <span>Thêm vào giỏ</span>
              </button>
              <button class="btn btn-success btn-lg shadow-sm d-flex align-items-center gap-2 px-4 py-2" @click="buyNow"
                :disabled="!selectedDetail || selectedQuantity <= 0">
                <i class="bi bi-lightning-fill fs-5"></i>
                <span>Mua ngay</span>
              </button>
              <button class="btn btn-outline-secondary" @click="addToWishlist">
                ❤️ Yêu thích
              </button>
            </div>

            <!-- Thông báo trạng thái -->
            <div v-if="!selectedDetail" class="alert alert-warning mt-3" role="alert">
              <i class="bi bi-exclamation-triangle"></i>
              Vui lòng chọn màu sắc và size sản phẩm để thêm vào giỏ hàng
            </div>
          </div>
        </div>

        <!-- Mô tả sản phẩm -->
        <div class="col-12 mt-5">
          <div class="bg-light p-4 rounded text-center">
            <h4 class="fw-bold mb-3">Mô tả sản phẩm</h4>
            <p class="fs-5 mb-0">{{ product.description || 'Chưa có mô tả' }}</p>
          </div>
        </div>
      </div>

      <!-- Đang tải -->
      <div v-else class="text-center py-5">
        <div class="spinner-border text-primary" role="status"></div>
        <p class="mt-3">Đang tải thông tin sản phẩm...</p>
      </div>
    </main>
    <MainFooter />
  </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

export default {
  name: 'ProductDetail',
  components: { MainHeader, MainFooter },
  data() {
    return {
      product: null,
      productDetails: [],
      selectedDetail: null,
      selectedSize: '',
      selectedColor: '',
      selectedQuantity: 1,
      selectedImage: '',
    };
  },
  computed: {
    uniqueColors() {
      const colors = this.productDetails.map(d => d.color);
      return [...new Set(colors)];
    },
    filteredSizes() {
      return this.productDetails
        .filter(d => d.color === this.selectedColor)
        .map(d => d.size)
        .filter((value, index, self) => self.indexOf(value) === index);
    }
  },
  methods: {
    fetchProductDetails(productId) {
      fetch(`http://localhost:8080/products/${productId}`)
        .then(res => {
          if (!res.ok) throw new Error('Không thể lấy dữ liệu');
          return res.json();
        })
        .then(data => {
          if (!data || !data.productDetails || data.productDetails.length === 0) {
            alert('Sản phẩm không có phân loại.');
            this.$router.push('/');
            return;
          }

          this.product = {
            id: data.id,
            name: data.name,
            brand: data.brand,
            description: data.description,
            imageUrl: data.imageUrl,
            status: data.status,
          };
          this.productDetails = data.productDetails;
          this.selectedImage = this.getImageUrl(data.productDetails[0]?.imageUrl || data.imageUrl);

          // ✅ chọn màu đầu tiên mặc định
          this.selectedColor = this.uniqueColors[0] || '';
          this.onColorChange();
        })
        .catch(err => {
          console.error(err);
          alert('Lỗi khi tải dữ liệu sản phẩm.');
        });
    },
    onColorChange() {
      this.selectedSize = '';
      this.selectedDetail = null;
      if (this.filteredSizes.length > 0) {
        this.selectedSize = this.filteredSizes[0];
        this.onSizeChange();
      }
      const detailWithColor = this.productDetails.find(d => d.color === this.selectedColor);
      if (detailWithColor?.imageUrl) {
        this.selectedImage = this.getImageUrl(detailWithColor.imageUrl);
      }
    },
    onSizeChange() {
      this.selectedDetail = this.productDetails.find(
        d => d.color === this.selectedColor && d.size === this.selectedSize
      );
      if (this.selectedDetail?.imageUrl) {
        this.selectedImage = this.getImageUrl(this.selectedDetail.imageUrl);
      }
    },
    getImageUrl(path) {
      if (!path) return require('@/assets/img/default-avatar.png');
      if (path.startsWith('http')) return path;
      if (path.includes('productDetails')) return `http://localhost:8080/images/productDetails/${path}`;
      return `http://localhost:8080/images/productDetails/${path}`;
    },
    formatPrice(value) {
      if (!value || value === 0) return '0 VND';
      return new Intl.NumberFormat('vi-VN').format(value) + ' VNĐ';
    },
    addToCart() {
      if (!this.selectedDetail) {
        alert('Vui lòng chọn màu sắc và size sản phẩm!');
        return;
      }
      if (this.selectedQuantity <= 0) {
        alert('Số lượng phải lớn hơn 0!');
        return;
      }

      const user = JSON.parse(localStorage.getItem('user'));
      if (!user || !user.id) {
        alert('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!');
        this.$router.push('/login');
        return;
      }

      fetch('http://localhost:8080/api/cart', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userId: parseInt(user.id),
          productDetailId: parseInt(this.selectedDetail.id),
          quantity: parseInt(this.selectedQuantity)
        })
      })
        .then(response => {
          if (!response.ok) {
            return response.text().then(text => {
              throw new Error(`HTTP ${response.status}: ${text}`);
            });
          }
          return response.json();
        })
        .then(() => {
          alert(`🛒 Đã thêm "${this.product.name}" vào giỏ hàng!`);
          localStorage.setItem('cartUpdated', Date.now().toString());
        })
        .catch(error => {
          console.error('Lỗi khi thêm vào giỏ hàng:', error);
          alert('Không thể thêm vào giỏ. Vui lòng thử lại!');
        });
    },
    async buyNow() {
      if (!this.selectedDetail) {
        alert('Vui lòng chọn màu sắc và size sản phẩm!');
        return;
      }
      if (this.selectedQuantity <= 0) {
        alert('Số lượng phải lớn hơn 0!');
        return;
      }
      const user = JSON.parse(localStorage.getItem('user'));
      if (!user || !user.id) {
        alert('Vui lòng đăng nhập để mua sản phẩm!');
        this.$router.push('/login');
        return;
      }

      try {
        const response = await fetch('http://localhost:8080/api/checkout/review', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            id: this.selectedDetail.id,
            quantity: this.selectedQuantity
          })
        });

        const result = await response.json();

        if (result.success) {
          localStorage.setItem("buyNowProduct", JSON.stringify({
            productDetailId: this.selectedDetail.id,
            quantity: this.selectedQuantity
          }));
          this.$router.push('/check-out');
        } else {
          alert(result.message || 'Đã xảy ra lỗi!');
        }

      } catch (error) {
        console.error('Lỗi khi gửi yêu cầu mua hàng:', error);
        alert('Có lỗi xảy ra khi kết nối đến server.');
      }
    },
   async addToWishlist() {
  const user = JSON.parse(localStorage.getItem("user"));
  if (!user || !user.id) {
    alert("Vui lòng đăng nhập để thêm sản phẩm vào yêu thích!");
    this.$router.push("/login");
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/api/wishlists", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        user: { id: user.id },
        product: { id: this.product.id }
      }),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(`Lỗi API: ${text}`);
    }

    const result = await res.json();
    alert(`❤️ Đã thêm "${this.product.name}" vào wishlist!`);
    console.log("Wishlist saved:", result);

  } catch (err) {
    console.error("Lỗi khi thêm wishlist:", err);
    alert("Không thể thêm vào wishlist. Vui lòng thử lại!");
  }
}
  },
  mounted() {
    const productId = this.$route.params.id;
    if (!productId) {
      alert("Không có sản phẩm hợp lệ.");
      this.$router.push('/');
    } else {
      this.fetchProductDetails(productId);
    }
  }
};
</script>

<style scoped>
img {
  max-width: 100%;
  height: auto;
}
.product-image-large {
  width: 70%;
  max-height: 500px;
  object-fit: contain;
  margin: auto;
  display: block;
  border-radius: 10px;
}
select.form-select-sm,
input.form-control-sm {
  font-size: 0.75rem;
  padding: 0.2rem 0.4rem;
}
label.form-label.small {
  font-size: 0.75rem;
  margin-bottom: 0.25rem;
}
</style>