<template>
  <div class="p-6 bg-white rounded-xl shadow w-full mx-auto">
    <h2 class="text-2xl font-bold mb-6">📝 Quản lý đánh giá sản phẩm</h2>

    <!-- Bộ lọc -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
      <select v-model="filterProductId" class="border rounded px-3 py-2">
        <option value="">📦 Tất cả sản phẩm</option>
        <option v-for="product in products" :key="product.id" :value="product.id">
          {{ product.name }}
        </option>
      </select>

      <select v-model="filterRating" class="border rounded px-3 py-2">
        <option value="">⭐ Tất cả số sao</option>
        <option v-for="n in 5" :key="n" :value="n">{{ n }} sao</option>
      </select>

      <input
        type="text"
        v-model="searchKeyword"
        placeholder="🔍 Tìm theo người dùng hoặc nội dung"
        class="border rounded px-3 py-2"
      />

    </div>

    <div v-if="loading" class="text-center my-10">Đang tải đánh giá...</div>

    <!-- Bảng -->
    <table v-else class="w-full table-auto border text-sm">
      <thead class="bg-gray-100 text-left">
        <tr>
          <th class="p-2 border">#</th>
          <th class="p-2 border">Sản phẩm</th>
          <th class="p-2 border">Người dùng</th>
          <th class="p-2 border">Số sao</th>
          <th class="p-2 border">Nội dung</th>
          <th class="p-2 border">Ảnh</th>
          <th class="p-2 border">Ngày tạo</th>
          <th class="p-2 border text-center">Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(review, index) in filteredReviews"
          :key="review.id"
          class="hover:bg-gray-50"
        >
          <td class="p-2 border">{{ index + 1 }}</td>
          <td class="p-2 border">{{ review.productName }}</td>
          <td class="p-2 border">{{ review.userName }}</td>
          <td class="p-2 border text-center">{{ review.rating }} ⭐</td>
          <td class="p-2 border">{{ review.comment }}</td>
          <td class="p-2 border">
            <div class="flex gap-2 flex-wrap">
              <img
                v-for="img in review.images"
                :key="img.id"
                :src="img.imageUrl"
                class="w-14 h-14 object-cover rounded"
                alt="Ảnh đánh giá"
              />
            </div>
          </td>
          <td class="p-2 border">{{ formatDate(review.createdAt) }}</td>
          <td class="p-2 border text-center">
            <button
              @click="deleteReview(review.id)"
              class="text-red-500 hover:underline"
            >
              Xoá
            </button>
          </td>
        </tr>
        <tr v-if="filteredReviews.length === 0">
          <td colspan="8" class="p-4 text-center text-gray-500">Không tìm thấy đánh giá phù hợp.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const reviews = ref([])
const products = ref([])
const loading = ref(true)

const filterProductId = ref('')
const filterRating = ref('')
const searchKeyword = ref('')

const fetchReviews = async () => {
  try {
    const res = await axios.get('/api/reviews/admin')
    reviews.value = res.data
  } catch (err) {
    console.error('Lỗi khi tải đánh giá:', err)
  } finally {
    loading.value = false
  }
}

const fetchProducts = async () => {
  try {
    const res = await axios.get('/api/products')
    products.value = res.data
  } catch (err) {
    console.error('Lỗi khi tải sản phẩm:', err)
  }
}

const deleteReview = async (id) => {
  if (!confirm('Bạn có chắc muốn xoá đánh giá này?')) return
  try {
    await axios.delete(`/api/reviews/${id}`)
    reviews.value = reviews.value.filter((r) => r.id !== id)
  } catch (err) {
    console.error('Xoá thất bại:', err)
  }
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('vi-VN') + ' ' + date.toLocaleTimeString('vi-VN')
}

const resetFilters = () => {
  filterProductId.value = ''
  filterRating.value = ''
  searchKeyword.value = ''
}

const filteredReviews = computed(() => {
  return reviews.value.filter((review) => {
    const matchProduct = filterProductId.value === '' || review.productId == filterProductId.value
    const matchRating = filterRating.value === '' || review.rating == filterRating.value
    const keyword = searchKeyword.value.toLowerCase()
    const matchSearch =
      review.userName.toLowerCase().includes(keyword) ||
      review.comment.toLowerCase().includes(keyword)
    return matchProduct && matchRating && matchSearch
  })
})

onMounted(() => {
  fetchReviews()
  fetchProducts()
})
</script>

<style scoped>
table th,
table td {
  font-size: 14px;
}
</style>
