<template>
  <div>
    <MainHeader />
    <main class="container mt-4">
      <div v-if="product" class="row g-4">
        <!-- ... (giữ nguyên phần hiển thị sản phẩm, ảnh, size, màu, giỏ hàng) ... -->

        <!-- Mô tả sản phẩm -->
        <div class="col-12 mt-5">
          <div class="bg-light p-4 rounded text-center">
            <h4 class="fw-bold mb-3">Mô tả sản phẩm</h4>
            <p class="fs-5 mb-0">{{ product.description || 'Chưa có mô tả' }}</p>
          </div>
        </div>
      </div>

      <!-- Đánh giá sản phẩm -->
      <div class="bg-white p-4 rounded mt-4">
        <h4 class="fw-bold mb-3">⭐ Đánh giá sản phẩm</h4>

        <!-- Form viết đánh giá -->
        <div v-if="selectedDetail">
          <h6>Viết đánh giá</h6>
          <label>Đánh giá của bạn:</label>
          <div class="mb-2">
            <span
              v-for="star in 5"
              :key="star"
              class="star"
              :class="{ active: star <= newRating }"
              @click="newRating = star"
              style="font-size: 1.5rem; cursor: pointer; color: gold;"
            >★</span>
          </div>

          <textarea v-model="newComment" class="form-control mb-3" placeholder="Nhận xét của bạn..." rows="3" />

          <button class="btn btn-primary" @click="submitReview">Gửi đánh giá</button>
        </div>

        <!-- Danh sách đánh giá -->
        <div v-if="reviews.length > 0" class="mt-4">
          <div v-for="review in reviews" :key="review.id" class="border-bottom py-3">
            <div class="d-flex justify-content-between align-items-center">
              <strong class="text-primary">Đánh giá: {{ review.rating }} / 5 ⭐</strong>
              <small class="text-muted">{{ formatDate(review.createdAt) }}</small>
            </div>
            <div class="mt-1 fst-italic">{{ review.comment }}</div>
          </div>
        </div>
        <div v-else class="text-muted fst-italic mt-3">
          Chưa có đánh giá nào cho biến thể này.
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
      reviews: [],
      newRating: 0,
      newComment: ''
    };
  },
  computed: {
    uniqueSizes() {
      return [...new Set(this.productDetails.map(d => d.size))];
    },
    filteredColors() {
      return this.productDetails
        .filter(d => d.size === this.selectedSize)
        .map(d => d.color)
        .filter((v, i, arr) => arr.indexOf(v) === i);
    }
  },
  methods: {
    fetchProductDetails(productId) {
      fetch(`http://localhost:8080/products/${productId}`)
        .then(res => res.json())
        .then(data => {
          this.product = data;
          this.productDetails = data.productDetails || [];
          this.selectedImage = this.getImageUrl(data.productDetails[0]?.imageUrl || data.imageUrl);
          this.selectedSize = this.uniqueSizes[0] || '';
          this.onSizeChange();
        });
    },
    onSizeChange() {
      this.selectedColor = '';
      if (this.filteredColors.length > 0) {
        this.selectedColor = this.filteredColors[0];
        this.onColorChange();
      }
    },
    onColorChange() {
      this.selectedDetail = this.productDetails.find(
        d => d.size === this.selectedSize && d.color === this.selectedColor
      );
      if (this.selectedDetail?.imageUrl) {
        this.selectedImage = this.getImageUrl(this.selectedDetail.imageUrl);
      }
      if (this.selectedDetail?.id) {
        this.fetchReviews(this.selectedDetail.id);
      }
    },
    fetchReviews(detailId) {
      fetch(`http://localhost:8080/api/reviews/product-detail/${detailId}`)
        .then(res => res.json())
        .then(data => {
          this.reviews = data;
        });
    },
    submitReview() {
      if (!this.newRating || !this.newComment.trim()) {
        alert('Vui lòng nhập đầy đủ đánh giá và sao.');
        return;
      }
      const payload = {
        productDetailId: this.selectedDetail.id,
        userId: 1, // 🔒 giả định userId, nên thay bằng người dùng hiện tại
        rating: this.newRating,
        comment: this.newComment.trim()
      };
      fetch('http://localhost:8080/api/reviews', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      })
        .then(res => res.json())
        .then(() => {
          this.newRating = 0;
          this.newComment = '';
          this.fetchReviews(this.selectedDetail.id);
        })
        .catch(err => console.error(err));
    },
    getImageUrl(path) {
      if (!path) return require('@/assets/img/default-avatar.png');
      if (path.startsWith('http')) return path;
      if (path.includes('productDetails')) return `http://localhost:8080/images/productDetails/${path}`;
      return `http://localhost:8080/images/products/${path}`;
    },
    formatPrice(val) {
      return new Intl.NumberFormat('vi-VN').format(val) + ' VNĐ';
    },
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleDateString('vi-VN');
    }
  },
  mounted() {
    const productId = this.$route.params.id;
    this.fetchProductDetails(productId);
  }
};
</script>

<style scoped>
.star {
  transition: 0.3s;
}
.star:hover,
.star.active {
  transform: scale(1.2);
}
</style>
