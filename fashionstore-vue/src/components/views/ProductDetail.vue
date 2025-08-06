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
                class="rounded border" :class="{ 'border-primary': selectedImage === getImageUrl(detail.imageUrl) }"
                style="width: 60px; height: 60px; object-fit: cover; cursor: pointer;" />
            </div>
          </div>
        </div>


        <!-- Thông tin sản phẩm -->
        <div class="col-lg-6 col-md-12">
          <div class="bg-white p-4 rounded shadow-sm h-100">
            <h2 class="fw-bold mb-2">{{ product.name }}</h2><br>
            <p class="text-muted mb-1">Thương hiệu: {{ product.brand || 'Đang cập nhật' }}</p><br>
            <p class="fs-4 text-danger mb-4">
              Giá: {{ formatPrice(selectedDetail?.price || 0) }}
            </p>
            <div class="row mb-3">
              <!-- Chọn Size -->
              <div class="col-md-3 col-6">
                <label class="form-label fw-semibold small">Chọn Size:</label>
                <select v-model="selectedSize" class="form-select form-select-sm" @change="onSizeChange">
                  <option disabled value="">-- Chọn Size --</option>
                  <option v-for="size in uniqueSizes" :key="size">{{ size }}</option>
                </select>
              </div>

              <!-- Chọn Màu -->
              <div class="col-md-3 col-6">
                <label class="form-label fw-semibold small">Chọn Màu:</label>
                <select v-model="selectedColor" class="form-select form-select-sm" @change="onColorChange"
                  :disabled="!selectedSize">
                  <option disabled value="">-- Chọn Màu --</option>
                  <option v-for="color in filteredColors" :key="color">{{ color }}</option>
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
              Vui lòng chọn size và màu sắc sản phẩm để thêm vào giỏ hàng
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
      selectedImage: '', // ảnh được chọn để hiển thị lớn

    };
  },
  computed: {
    uniqueSizes() {
      const sizes = this.productDetails.map(d => d.size);
      return [...new Set(sizes)];
    },
    filteredColors() {
      return this.productDetails
        .filter(d => d.size === this.selectedSize)
        .map(d => d.color)
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
          this.selectedSize = this.uniqueSizes[0] || '';
          this.onSizeChange();
        })
        .catch(err => {
          console.error(err);
          alert('Lỗi khi tải dữ liệu sản phẩm.');
        });
    },
    onSizeChange() {
      this.selectedColor = '';
      this.selectedDetail = null;
      // Nếu chỉ có 1 màu hoặc muốn tự động chọn màu đầu tiên
      if (this.filteredColors.length > 0) {
        this.selectedColor = this.filteredColors[0];
        this.onColorChange(); // cập nhật detail và ảnh luôn
      }
    },
    onColorChange() {
      this.selectedDetail = this.productDetails.find(
        d => d.size === this.selectedSize && d.color === this.selectedColor
      );
      // ✅ Cập nhật ảnh lớn khi chọn màu
      if (this.selectedDetail && this.selectedDetail.imageUrl) {
        this.selectedImage = this.getImageUrl(this.selectedDetail.imageUrl);
      }
    },
    getImageUrl(path) {
      if (!path) return require('@/assets/img/default-avatar.png');
      if (path.startsWith('http')) return path;
      if (path.includes('productDetails')) return `http://localhost:8080/images/productDetails/${path}`;
      return `http://localhost:8080/images/products/${path}`;
    },
    formatPrice(value) {
      if (!value || value === 0) return '0 VND';
      return new Intl.NumberFormat('vi-VN').format(value) + ' VNĐ';
    },
    addToCart() {
      if (!this.selectedDetail) {
        alert('Vui lòng chọn size và màu sắc sản phẩm!');
        return;
      }

      if (this.selectedQuantity <= 0) {
        alert('Số lượng phải lớn hơn 0!');
        return;
      }

      // Kiểm tra đăng nhập
      const user = JSON.parse(localStorage.getItem('user'));
      if (!user || !user.id) {
        alert('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!');
        this.$router.push('/login');
        return;
      }

      // Gọi API để thêm vào giỏ hàng
      console.log('Sending request with data:', {
        userId: user.id,
        productDetailId: this.selectedDetail.id,
        quantity: this.selectedQuantity
      });

      fetch('http://localhost:8080/api/cart', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
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
        .then(data => {
          alert(`🛒 Đã thêm "${this.product.name}" vào giỏ hàng!`);
          // Thông báo cập nhật giỏ hàng
          localStorage.setItem('cartUpdated', Date.now().toString());
        })
        .catch(error => {
          console.error('Lỗi khi thêm vào giỏ hàng:', error);
          if (error.message.includes('Failed to fetch')) {
            alert('Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng hoặc liên hệ admin!');
          } else {
            alert(`Lỗi: ${error.message}`);
          }
        });
    },
    async buyNow() {
      if (!this.selectedDetail) {
        alert('Vui lòng chọn size và màu sắc sản phẩm!');
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
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            id: this.selectedDetail.id,
            quantity: this.selectedQuantity
          })
        });

        const result = await response.json();

        if (result.success) {
          // Lưu thông tin sản phẩm tạm thời vào localStorage
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
        alert('Có lỗi xảy ra trong quá trình kết nối đến máy chủ.');
      }
    },
    addToWishlist() {
      alert(`❤️ Đã thêm "${this.product.name}" vào danh sách yêu thích!`);
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
  /* Tăng chiều cao tối đa */
  object-fit: contain;
  margin: auto;
  display: block;
  border-radius: 10px;
}

select.form-select-sm,
input.form-control-sm {
  font-size: 0.75rem;
  /* nhỏ hơn nữa */
  padding: 0.2rem 0.4rem;
  /* padding mỏng hơn */
}

label.form-label.small {
  font-size: 0.75rem;
  /* nhãn nhỏ */
  margin-bottom: 0.25rem;
}
</style>