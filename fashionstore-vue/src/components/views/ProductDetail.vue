<template>
  <div>
    <MainHeader />
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
import MainHeader from '@/components/MainHeader.vue'
import MainFooter from '@/components/MainFooter.vue'
import axios from 'axios'
import { ref, onMounted } from 'vue'
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
.img-thumbnail {
  cursor: pointer;
  width: 60px;
  height: 60px;
  object-fit: cover;
}
</style>
