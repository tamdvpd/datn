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
      <img
        :src="getImageUrl(selectedImage)"
        class="product-image-large"
        :alt="product.name"
      />
    </div>
    <!-- Ảnh nhỏ bên dưới -->
    <div class="d-flex justify-content-center gap-2 flex-wrap">
      <img
        v-for="(detail, index) in productDetails.slice(0, 4)"
        :key="index"
        :src="getImageUrl(detail.imageUrl)"
        @click="selectedImage = getImageUrl(detail.imageUrl)"
        class="rounded border"
        :class="{ 'border-primary': selectedImage === getImageUrl(detail.imageUrl) }"
        style="width: 60px; height: 60px; object-fit: cover; cursor: pointer;"
      />
    </div>
  </div>
</div>
  <!-- Thông tin sản phẩm -->
  <div class="col-lg-6 col-md-12">
    <div class="bg-white p-4 rounded shadow-sm h-100">
      <h2 class="fw-bold mb-2">{{ product.name }}</h2><br>
      <p class="text-muted mb-1">Thương hiệu: {{ product.brand || 'Đang cập nhật' }}</p><br>
      <p class="fs-4 mb-4">
  <span v-if="selectedDetail?.discountPrice && selectedDetail.discountPrice < selectedDetail.price">
    <span class="text-decoration-line-through text-muted me-2">
      {{ formatPrice(selectedDetail.price) }}
    </span>
    <span class="text-danger fw-bold">
      {{ formatPrice(selectedDetail.discountPrice) }}
    </span>
  </span>
  <span v-else class="text-danger fw-bold">
    {{ formatPrice(selectedDetail?.price || 0) }}
  </span>
</p>
<div class="row mb-3">
<!-- Chọn Màu -->
  <div class="col-md-3 col-6">
    <label class="form-label fw-semibold small">Chọn Màu:</label>
    <select
      v-model="selectedColor"
      class="form-select form-select-sm"
      @change="onColorChange"
      :disabled="!selectedSize"
    >
      <option disabled value="">-- Chọn Màu --</option>
      <option v-for="color in filteredColors" :key="color">{{ color }}</option>
    </select>
  </div>

  <!-- Chọn Size -->
  <div class="col-md-3 col-6">
    <label class="form-label fw-semibold small">Chọn Size:</label>
    <select v-model="selectedSize" class="form-select form-select-sm" @change="onSizeChange">
      <option disabled value="">-- Chọn Size --</option>
      <option v-for="size in uniqueSizes" :key="size">{{ size }}</option>
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
        <button class="btn btn-outline-primary" @click="addToCart" :disabled="!selectedDetail">
          🛒 Thêm vào giỏ
        </button>
        <button class="btn btn-success" @click="buyNow" :disabled="!selectedDetail">
          ✅ Chọn và Mua ngay
        </button>
        <button class="btn btn-outline-secondary" @click="addToWishlist">
          ❤️ Yêu thích
        </button>
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
    selectedSize: '',
    selectedColor: '',
    selectedDetail: null,
    selectedImage: '',
    imageFromProduct: true, // ✅ đánh dấu ảnh đang dùng là từ product gốc
  };
}

,
  computed: {
    uniqueSizes() {
  if (!this.selectedColor) {
    // Nếu chưa chọn màu => trả về tất cả size
    const sizes = this.productDetails.map(d => d.size);
    return [...new Set(sizes)];
  }

  // Nếu đã chọn màu => lọc theo màu
  const sizes = this.productDetails
    .filter(d => d.color === this.selectedColor)
    .map(d => d.size);
  return [...new Set(sizes)];
}
,
    filteredColors() {
  const colors = this.productDetails.map(d => d.color);
  return [...new Set(colors)];
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

      // ✅ mặc định hiển thị ảnh từ sản phẩm gốc
      this.selectedImage = this.getImageUrl(data.imageUrl);
      this.imageFromProduct = true;

      this.selectedSize = this.uniqueSizes[0] || '';
      this.onSizeChange(); // chỉ update detail, không đổi ảnh
    })
    .catch(err => {
      console.error(err);
      alert('Lỗi khi tải dữ liệu sản phẩm.');
    });
    
},
  selectImage(imagePath) {
  this.selectedImage = this.getImageUrl(imagePath);
  this.imageFromProduct = false;
}
,
  onColorChange() {
  if (!this.selectedSize) {
    const sizesByColor = this.productDetails
      .filter(d => d.color === this.selectedColor)
      .map(d => d.size);
    this.selectedSize = sizesByColor[0] || '';
  }

  this.selectedDetail = this.productDetails.find(
    d => d.size === this.selectedSize && d.color === this.selectedColor
  );

  // ✅ Chỉ cập nhật ảnh nếu người dùng chưa click thumbnail
  if (this.selectedDetail?.imageUrl && this.imageFromProduct) {
    this.selectedImage = this.getImageUrl(this.selectedDetail.imageUrl);
  }
},

onSizeChange() {
  if (!this.selectedColor) {
    const colorsBySize = this.productDetails
      .filter(d => d.size === this.selectedSize)
      .map(d => d.color);
    this.selectedColor = colorsBySize[0] || '';
  }

  this.selectedDetail = this.productDetails.find(
    d => d.size === this.selectedSize && d.color === this.selectedColor
  );

  if (this.selectedDetail?.imageUrl && this.imageFromProduct) {
    this.selectedImage = this.getImageUrl(this.selectedDetail.imageUrl);
  }
}
,

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
    if (!this.selectedDetail) return;
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    const existing = cart.find(item => item.detailId === this.selectedDetail.id);
    if (existing) {
      existing.quantity += this.selectedQuantity;
    } else {
      cart.push({
        productId: this.product.id,
        detailId: this.selectedDetail.id,
        name: this.product.name,
        imageUrl: this.selectedDetail.imageUrl,
        price: this.selectedDetail.price,
        quantity: this.selectedQuantity,
        size: this.selectedDetail.size,
        color: this.selectedDetail.color,
      });
    }
    localStorage.setItem('cart', JSON.stringify(cart));
    alert(`🛒 Đã thêm "${this.product.name}" vào giỏ hàng!`);
  },

  buyNow() {
    this.addToCart();
    this.$router.push('/cart');
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
  max-height: 500px; /* Tăng chiều cao tối đa */
  object-fit: contain;
  margin: auto;
  display: block;
  border-radius: 10px;
}
select.form-select-sm,
input.form-control-sm {
  font-size: 0.75rem;         /* nhỏ hơn nữa */
  padding: 0.2rem 0.4rem;     /* padding mỏng hơn */
}

label.form-label.small {
  font-size: 0.75rem;         /* nhãn nhỏ */
  margin-bottom: 0.25rem;
}

</style>
