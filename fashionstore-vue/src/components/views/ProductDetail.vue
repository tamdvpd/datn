<template>
  <div>
    <MainHeader />
<<<<<<< Updated upstream
    <div class="container py-5" v-if="product">
      <div class="row">
        <!-- Ảnh -->
        <div class="col-md-6">
          <img :src="selectedImage" class="img-fluid rounded shadow-sm" alt="Product image" />
          <div class="mt-3 d-flex gap-2">
            <img
              v-for="img in product.images"
              :key="img"
              :src="img"
              class="img-thumbnail"
              @click="selectedImage = img"
            />
          </div>
        </div>

        <!-- Thông tin -->
        <div class="col-md-6">
          <h2>{{ product.name }}</h2>
          <p class="text-muted">{{ product.brand }}</p>
          <p>
            <span class="text-danger fs-4 fw-bold">{{ formatPrice(product.discountPrice) }}</span>
            <span v-if="product.discountPrice < product.price" class="text-decoration-line-through ms-2">
              {{ formatPrice(product.price) }}
            </span>
          </p>

          <div class="my-2">
            <label class="form-label">Color:</label>
            <div class="d-flex gap-2">
              <button
                v-for="color in product.colors"
                :key="color"
                :class="['btn', selectedColor === color ? 'btn-dark' : 'btn-outline-secondary']"
                @click="selectedColor = color"
              >
                {{ color }}
              </button>
            </div>
          </div>

          <div class="my-2">
            <label class="form-label">Size:</label>
            <select class="form-select w-auto" v-model="selectedSize">
              <option v-for="size in product.sizes" :key="size" :value="size">{{ size }}</option>
            </select>
          </div>

          <div class="my-2">
            <label class="form-label">Quantity:</label>
            <input type="number" v-model="quantity" class="form-control w-auto" min="1" />
          </div>

          <div class="my-3 d-flex gap-3">
            <button class="btn btn-primary" @click="buyNow">Mua ngay</button>
            <button class="btn btn-outline-secondary" @click="addToCart">Thêm vào giỏ</button>
          </div>

          <p class="mt-4"><strong>Mô tả:</strong></p>
          <p>{{ product.description }}</p>
        </div>
      </div>

      <!-- Đánh giá -->
      <div class="mt-5">
        <h4>Đánh giá sản phẩm</h4>
        <div class="border p-3 mb-2" v-for="review in reviews" :key="review.id">
          <strong>{{ review.user }}</strong> -
          <span class="text-warning">★ {{ review.rating }}/5</span>
          <p>{{ review.comment }}</p>
        </div>
      </div>

      <!-- Gợi ý -->
      <div class="mt-5">
        <h4>Sản phẩm đề xuất</h4>
        <div class="row">
          <div class="col-md-3" v-for="p in suggestedProducts" :key="p.id">
            <div class="card mb-3">
              <img :src="p.image" class="card-img-top" style="height: 180px; object-fit: cover;" />
              <div class="card-body">
                <h6>{{ p.name }}</h6>
                <p class="text-danger fw-bold">{{ formatPrice(p.discountPrice) }}</p>
                <router-link :to="`/products/${p.id}`" class="btn btn-sm btn-outline-primary w-100">Xem chi tiết</router-link>
=======
    <div class="product-detail container py-4">
      <div class="row">
        <div class="col-md-5">
          <img src="@/assets/img/img1.jpg" class="img-fluid rounded" alt="Áo sơ mi" />
          <div class="d-flex gap-2 mt-3">
            <img src="@/assets/img/img2.jpg" class="img-thumbnail" width="60" />
            <img src="@/assets/img/img3.jpg" class="img-thumbnail" width="60" />
            <img src="@/assets/img/img4.jpg" class="img-thumbnail" width="60" />
          </div>
        </div>
        <div class="col-md-7">
          <h2>Áo sơ mi nam dài tay Café-Dris khử mùi – Xanh nhạt</h2>
          <p class="text-danger h4">390,000 VNĐ <span class="text-muted small">(FreeShip đơn hàng > 400k)</span></p>

          <p>Mã sản phẩm: TMS003 | Tình trạng: <span class="text-success">CÒN HÀNG</span></p>

          <div class="mb-2">
            <strong>Size:</strong>
            <button class="btn btn-outline-dark btn-sm mx-1">M</button>
            <button class="btn btn-outline-dark btn-sm mx-1">L</button>
            <button class="btn btn-outline-dark btn-sm mx-1">XL</button>
          </div>

          <div class="mb-2">
            <strong>Màu:</strong>
            <button class="btn btn-outline-dark btn-sm mx-1">Trắng</button>
            <button class="btn btn-outline-dark btn-sm mx-1">Xanh</button>
          </div>

          <div class="d-flex gap-2">
            <button class="btn btn-success">MUA NGAY</button>
            <button class="btn btn-danger">Thêm vào giỏ hàng</button>
          </div>
        </div>
      </div>

      <hr />

      <div class="mt-4">
        <h4>MÔ TẢ SẢN PHẨM</h4>
        <p>
          Đây là chiếc áo sơ mi chất liệu tái chế, thân thiện với môi trường để diện trong tủ đồ của bạn.
          <br />
          - Café: Chất liệu được phát triển từ loại vải sợi từ bã cà phê.
          <br />
          - Dris: Công nghệ khử mùi vượt trội.
        </p>
        <img src="@/assets/img/banner.jpg" class="img-fluid rounded my-3" alt="Banner" />
      </div>

      <div class="mt-5">
        <h4>SẢN PHẨM LIÊN QUAN</h4>
        <div class="row g-3">
          <div class="col-md-3" v-for="i in 4" :key="i">
            <div class="card">
              <img src="@/assets/img/img1.jpg" class="card-img-top" alt="Sản phẩm liên quan" />
              <div class="card-body">
                <p class="card-title">Áo sơ mi nam KDENIM</p>
                <p class="text-danger fw-bold">220,000 VNĐ</p>
>>>>>>> Stashed changes
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script>
<<<<<<< Updated upstream
import MainHeader from '@/components/MainHeader.vue'
import MainFooter from '@/components/MainFooter.vue'
import axios from 'axios'
import { useRoute } from 'vue-router'

export default {
  name: 'ProductDetail',
  components: { MainHeader, MainFooter },
  setup() {
    const route = useRoute()
    const productId = route.params.id

    const product = ref(null)
    const selectedColor = ref('')
    const selectedSize = ref('')
    const selectedImage = ref('')
    const quantity = ref(1)
    const reviews = ref([])
    const suggestedProducts = ref([])

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price)
    }

    const fetchData = async () => {
      const res = await axios.get(`/api/products/${productId}`)
      product.value = res.data
      selectedColor.value = res.data.colors[0]
      selectedSize.value = res.data.sizes[0]
      selectedImage.value = res.data.images[0]

      const reviewRes = await axios.get(`/api/products/${productId}/reviews`)
      reviews.value = reviewRes.data

      const suggestRes = await axios.get(`/api/products/${productId}/suggested`)
      suggestedProducts.value = suggestRes.data
    }

    const addToCart = async () => {
      if (!selectedColor.value || !selectedSize.value) {
        alert('Vui lòng chọn màu và size!')
        return
      }
      await axios.post('/api/cart', {
        productId: productId,
        color: selectedColor.value,
        size: selectedSize.value,
        quantity: quantity.value
      })
      alert('Đã thêm vào giỏ hàng!')
    }

    const buyNow = async () => {
      if (!selectedColor.value || !selectedSize.value) {
        alert('Vui lòng chọn màu và size!')
        return
      }
      await axios.post('/api/orders/quick-buy', {
        productId: productId,
        color: selectedColor.value,
        size: selectedSize.value,
        quantity: quantity.value
      })
      alert('Đặt hàng thành công!')
    }

    onMounted(() => {
      fetchData()
    })

    return {
      product,
      selectedColor,
      selectedSize,
      selectedImage,
      quantity,
      reviews,
      suggestedProducts,
      formatPrice,
      addToCart,
      buyNow
    }
  }
}
</script>

<style scoped>
=======
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

export default {
  name: 'ProductDetail',
  components: {
    MainHeader,
    MainFooter,
  },
};
</script>

<style scoped>
.product-detail h2 {
  font-weight: bold;
}
>>>>>>> Stashed changes
.img-thumbnail {
  cursor: pointer;
  width: 60px;
  height: 60px;
  object-fit: cover;
}
</style>
