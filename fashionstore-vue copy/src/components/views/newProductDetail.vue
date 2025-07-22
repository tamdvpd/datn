<template>
  <div class="product-detail container my-5">
    <div class="row">
      <!-- Breadcrumb -->
      <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="#">Home</a></li>
          <li class="breadcrumb-item"><a href="#">Category</a></li>
          <li class="breadcrumb-item active" aria-current="page">{{ product.name }}</li>
        </ol>
      </nav>
      
      <!-- Product Images -->
      <div class="col-md-6">
        <div class="main-image mb-3">
          <img :src="mainImage" class="img-fluid rounded" alt="Main product image">
        </div>
        <div class="thumbnail-container d-flex flex-wrap gap-2">
          <img 
            v-for="(image, index) in product.images" 
            :key="index" 
            :src="image" 
            class="img-thumbnail" 
            width="80" 
            @click="changeMainImage(image)"
            :class="{ 'active-thumbnail': mainImage === image }"
          >
        </div>
      </div>
      
      <!-- Product Info -->
      <div class="col-md-6">
        <h1 class="product-title">{{ product.name }}</h1>
        
        <div class="d-flex align-items-center mb-3">
          <div class="rating me-2">
            <i class="bi bi-star-fill text-warning" v-for="star in 5" :key="star"></i>
            <span class="ms-2 text-muted">({{ product.reviewCount }} reviews)</span>
          </div>
          <span class="badge bg-success ms-3">
            <i class="bi bi-check-circle me-1"></i>Còn hàng
          </span>
        </div>
        
        <div class="price-section mb-4">
          <h3 class="current-price text-primary">₫{{ product.price }}</h3>
          <del class="original-price text-muted ms-2" v-if="product.originalPrice">₫{{ product.originalPrice }}</del>
          <span class="discount-badge badge bg-danger ms-2" v-if="product.discount">
            {{ product.discount }}%
          </span>
        </div>
        
        <div class="product-description mb-4">
          <p>{{ product.description }}</p>
          <ul class="features-list">
            <li v-for="(feature, index) in product.features" :key="index">{{ feature }}</li>
          </ul>
        </div>
        
        <div class="variants mb-4" v-if="product.variants">
          <h5 class="mb-3">Options:</h5>
          <div class="variant-options">
            <div class="mb-3" v-for="(variant, name) in product.variants" :key="name">
              <label class="form-label">{{ name }}</label>
              <select class="form-select" v-model="selectedVariants[name]">
                <option v-for="option in variant" :key="option" :value="option">{{ option }}</option>
              </select>
            </div>
          </div>
        </div>
        
        <div class="quantity-selector mb-4">
          <label class="form-label">Quantity</label>
          <div class="input-group" style="width: 150px;">
            <button class="btn btn-outline-secondary" @click="decreaseQuantity">-</button>
            <input type="number" class="form-control text-center" v-model="quantity" min="1">
            <button class="btn btn-outline-secondary" @click="increaseQuantity">+</button>
          </div>
        </div>
        
        <div class="action-buttons d-flex gap-3 mb-4">
          <button class="btn btn-primary flex-grow-1 py-3" @click="addToCart">
            <i class="bi bi-cart-plus me-2"></i>Thêm vào giỏ hàng
          </button>
          <button class="btn btn-outline-secondary py-3">
            <i class="bi bi-heart me-2"></i>Yêu thích
          </button>
        </div>
        
        <div class="delivery-info mb-4">
          <div class="d-flex align-items-center mb-2">
            <i class="bi bi-truck me-2"></i>
            <span>Giao hàng miễn phí cho đơn hàng trên 100k</span>
          </div>
          <div class="d-flex align-items-center mb-2">
            <i class="bi bi-arrow-left-right me-2"></i>
            <span>Chính sách đổi trả trong vòng 7 ngày</span>
          </div>
          <div class="d-flex align-items-center">
            <i class="bi bi-shield-check me-2"></i>
            <span>Bảo hành 3 tháng</span>
          </div>
        </div>
        
        <div class="share-buttons">
          <span class="me-2">Chia sẻ:</span>
          <button class="btn btn-sm btn-outline-primary me-2">
            <i class="bi bi-facebook"></i>
          </button>
          <button class="btn btn-sm btn-outline-info me-2">
            <i class="bi bi-twitter"></i>
          </button>
          <button class="btn btn-sm btn-outline-danger me-2">
            <i class="bi bi-instagram"></i>
          </button>
          <button class="btn btn-sm btn-outline-secondary">
            <i class="bi bi-link-45deg"></i>
          </button>
        </div>
      </div>
    </div>
    
    <!-- Product Tabs -->
    <div class="row mt-5">
      <div class="col-12">
        <ul class="nav nav-tabs" id="productTabs" role="tablist">
          <li class="nav-item" role="presentation">
            <button class="nav-link active" id="description-tab" data-bs-toggle="tab" data-bs-target="#description" type="button" role="tab">Mô tả</button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link" id="specifications-tab" data-bs-toggle="tab" data-bs-target="#specifications" type="button" role="tab">Chi tiết sản phẩm</button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link" id="reviews-tab" data-bs-toggle="tab" data-bs-target="#reviews" type="button" role="tab">Đánh giá ({{ product.reviewCount }})</button>
          </li>
        </ul>
        
        <div class="tab-content p-3 border border-top-0 rounded-bottom">
          <div class="tab-pane fade show active" id="description" role="tabpanel">
            <h4>MÔ TẢ SẢN PHẨM</h4>
            <p>{{ product.fullDescription }}</p>
          </div>
          
          <div class="tab-pane fade" id="specifications" role="tabpanel">
            <table class="table">
              <tbody>
                <tr v-for="(value, key) in product.specifications" :key="key">
                  <th scope="row" style="width: 30%;">{{ key }}</th>
                  <td>{{ value }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div class="tab-pane fade" id="reviews" role="tabpanel">
            <div class="row">
              <div class="col-md-4">
                <div class="text-center mb-4">
                  <h2 class="display-4">{{ product.averageRating }}</h2>
                  <div class="rating mb-2">
                    <i class="bi bi-star-fill text-warning" v-for="star in 5" :key="star"></i>
                  </div>
                  <p class="text-muted">Dựa trên {{ product.reviewCount }} đánh giá</p>
                </div>
                
                <div class="mb-4">
                  <h5>Viết đánh giá của bạn</h5>
                  <form @submit.prevent="submitReview">
                    <div class="mb-3">
                      <label class="form-label">Đánh giá</label>
                      <div class="rating-input">
                        <i class="bi bi-star" 
                           v-for="star in 5" 
                           :key="star" 
                           @click="setRating(star)"
                           :class="{ 'bi-star-fill text-warning': star <= newReview.rating }"
                        ></i>
                      </div>
                    </div>
                    <div class="mb-3">
                      <label class="form-label">Tên</label>
                      <input type="text" class="form-control" v-model="newReview.name" required>
                    </div>
                    <div class="mb-3">
                      <label class="form-label">Nội dung</label>
                      <textarea class="form-control" rows="3" v-model="newReview.text" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Gửi đánh giá</button>
                  </form>
                </div>
              </div>
              
              <div class="col-md-8">
                <div class="review-list">
                  <div class="review-item mb-4" v-for="review in product.reviews" :key="review.id">
                    <div class="d-flex justify-content-between mb-2">
                      <h5>{{ review.name }}</h5>
                      <small class="text-muted">{{ formatDate(review.date) }}</small>
                    </div>
                    <div class="rating mb-2">
                      <i class="bi bi-star-fill text-warning" v-for="star in Math.round(product.averageRating)" :key="star"></i>
                    </div>
                    <p>{{ review.text }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Related Products -->
    <div class="row mt-5">
      <div class="col-12">
        <h3 class="mb-4">Các sản phẩm khác</h3>
        <div class="row">
          <div class="col-md-3 mb-4" v-for="relatedProduct in relatedProducts" :key="relatedProduct.id">
            <div class="card h-100">
              <img :src="relatedProduct.image" class="card-img-top" alt="Related product">
              <div class="card-body">
                <h5 class="card-title">{{ relatedProduct.name }}</h5>
                <div class="price-section">
                  <span class="text-primary">₫{{ relatedProduct.price }}</span>
                  <del class="text-muted ms-2" v-if="relatedProduct.originalPrice">${{ relatedProduct.originalPrice }}</del>
                </div>
              </div>
              <div class="card-footer bg-transparent text-center">
  <button class="btn btn-sm btn-outline-primary">Thêm vào giỏ hàng</button>
</div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductDetail',
  data() {
    return {
      product: {
        id: 1,
        name: 'Áo Polo Cổ V Dệt Kim Cao Cấp Phong Cách Hàn Quốc SUGAL TOP 005',
        description: 'Chât vải dệt kim cao cấp cho độ dày dặn, co giãn tốt và quan trọng độ bền màu cao',
        fullDescription: ' Hướng dẫn sử dụng và bảo quản Áo Polo dệt gân nam tính, thanh lịch, sang trọng 2 màu - Giặt ở nhiệt độ bình thường, với đồ có màu tương tự. -  Không được dùng hóa chất tẩy.  -  Khi áo in giặt nhớ giũ thẳng áo để hình in ko bị dính vào nhau. -  Hạn chế sử dụng máy sấy và ủi ở nhiệt độ thích hợp.    -  Lộn mặt trái khi phơi tránh bị phai màu * Chính sách và điều kiện đổi trả của SUGAL : + Cam kết chất lượng và mẫu mã sản phẩm giống với hình ảnh. + Cam kết được đổi trả hàng trong vòng 7 ngày. + Hàng phải còn mới  và chưa qua sử dụng. + Sản phẩm bị lỗi do vận chuyển và do nhà sản xuất. 📌 LƯU Ý:  Khi bạn gặp bất kì vấn đề gì về sản phẩm đừng vội đánh giá  mà hãy liên hệ Shop để đc hỗ trợ một cách tốt nhất  nhé.',
        price: 169.99,
        originalPrice: 249.99,
        discount: 32,
        reviewCount: 2200,
        averageRating: 4.7,
        images: [
          'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lo0hd47z5myi2e.webp',
          'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lo0hd47yaqgq57.webp',
          'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lo0hd47yq6pmb6.webp',
          'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lo0hd47yaqjxd3.webp'
        ],
        features: [
          'Chât vải dệt kim cao cấp cho độ dày dặn, co giãn tốt và quan trọng độ bền màu cao',
          'Giặt ko đổ lông hay bay màu, thấm hút mồ hôi và thoải mái không gò bó khi hoạt động',
          'Thiệt kế cấu trúc lỗ thoáng, mắt vải mịn giúp tôn dáng cho người mặc ',
          'Màu sắc & kích cỡ Áo Polo trơn nam có cổ (bản nâng cấp) vải cotton dệt gân nam tính, thanh lịch, sang trọng 2 màu',
          'Form áo regular-fit thoải mái ko gò bó khi vận động tạo nên sự năng động, trẻ trung...'
        ],
        variants: {
          Color: ['Đen', 'Trắng'],
          Size: ['M', 'L', 'XL', 'XXL']
        },
        specifications: {
          'Phong cách':'Hàn Quốc',
          'Xuất xứ':'Việt Nam',
          'Chiều dài tay áo': 'Tay ngắn',
          'Chất liệu': 'Cotton',
          'Dáng kiểu áo': 'Slim',
          'Mẫu': 'Sọc'
        },
        reviews: [
          {
            id: 1,
            name: 'Nguyễn Thị Thảo Nguyên',
            rating: 5,
            text: 'Áo quá đẹp',
            date: '2023-05-15'
          },
          {
            id: 2,
            name: 'Nguyễn Thị Hoài Ngọc',
            rating: 4,
            text: 'Tôi năm nay hơn 20 tuổi mà lần đầu tiên tôi được thấy chiếc áo Polo đẹp như thế này',
            date: '2023-04-22'
          }
        ]
      },
      relatedProducts: [
        {
          id: 101,
          name: 'Áo Thun Cotton Basic Form Regular Tôn Dáng SUGAL Top 011',
          price: 179000,
          originalPrice: 249.000,
          image: 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m8u9wp5ubrt3bb@resize_w900_nl.webp'
        },
        {
          id: 102,
          name: 'Áo Polo Bản Phối Màu Đặc Biệt Form Regular SUGAL TOP 008',
          price: 129.000,
          originalPrice: 249.000,
          image: 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lsfuv0lqpww45a@resize_w900_nl.webp'
        },
        {
          id: 103,
          name: 'Áo len cardigan ngắn tay có nút phối màu hợp thời trang',
          price: 599.000,
          image: 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lmo55rk1kjq71d@resize_w900_nl.webp'
        },
        {
          id: 104,
          name: 'Áo Cadigan Nam Nữ Trơn Bassic SUGAL TOP 010 - Màu sắc dễ mặc, thoáng mát và thấm hút mồ hôi',
          price: 499.000,
          image: 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1qp020tey7zfd@resize_w900_nl.webp'
        }
      ],
      mainImage: 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lo0hd47z5myi2e.webp',
      selectedVariants: {
        Color: 'Black',
        Size: 'Standard'
      },
      quantity: 1,
      newReview: {
        name: '',
        rating: 0,
        text: ''
      }
    }
  },
  methods: {
    changeMainImage(image) {
      this.mainImage = image;
    },
    increaseQuantity() {
      this.quantity++;
    },
    decreaseQuantity() {
      if (this.quantity > 1) {
        this.quantity--;
      }
    },
    addToCart() {
      const cartItem = {
        productId: this.product.id,
        name: this.product.name,
        price: this.product.price,
        image: this.mainImage,
        quantity: this.quantity,
        selectedVariants: this.selectedVariants
      };
      // In a real app, you would dispatch this to a Vuex store or similar
      console.log('Added to cart:', cartItem);
      alert(`${this.quantity} ${this.product.name} added to cart!`);
    },
    setRating(rating) {
      this.newReview.rating = rating;
    },
    submitReview() {
      const newReview = {
        id: Date.now(),
        name: this.newReview.name,
        rating: this.newReview.rating,
        text: this.newReview.text,
        date: new Date().toISOString().split('T')[0]
      };
      
      this.product.reviews.unshift(newReview);
      this.product.reviewCount++;
      
      // Reset form
      this.newReview = {
        name: '',
        rating: 0,
        text: ''
      };
      
      // In a real app, you would send this to your backend
      console.log('New review submitted:', newReview);
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    }
  }
}
</script>

<style scoped>
.product-detail {
  max-width: 1200px;
}

.product-title {
  font-size: 2rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.current-price {
  font-size: 1.75rem;
  font-weight: 700;
}

.original-price {
  font-size: 1.25rem;
}

.discount-badge {
  font-size: 0.9rem;
  vertical-align: super;
}

.features-list {
  padding-left: 1.5rem;
}

.features-list li {
  margin-bottom: 0.5rem;
}

.img-thumbnail {
  cursor: pointer;
  transition: all 0.2s;
}

.img-thumbnail:hover {
  border-color: #0d6efd;
  transform: scale(1.05);
}

.active-thumbnail {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
}

.rating-input i {
  cursor: pointer;
  font-size: 1.5rem;
  margin-right: 0.25rem;
}

.review-item {
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.review-item:last-child {
  border-bottom: none;
}

.card {
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-5px);
}

.nav-tabs .nav-link {
  font-weight: 500;
}

.tab-content {
  min-height: 300px;
}
</style>