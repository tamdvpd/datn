<template>
  <div class="p-4 border rounded-xl shadow-md bg-white max-w-md mx-auto">
    <h2 class="text-xl font-semibold mb-4">Viết đánh giá sản phẩm</h2>

    <form @submit.prevent="submitReview">
      <!-- Rating -->
      <div class="mb-4">
        <label class="block text-sm font-medium">Chọn số sao:</label>
        <select v-model="form.rating" required class="mt-1 w-full border rounded p-2">
          <option disabled value="">-- Chọn điểm --</option>
          <option v-for="i in 5" :key="i" :value="i">{{ i }} sao</option>
        </select>
      </div>

      <!-- Comment -->
      <div class="mb-4">
        <label class="block text-sm font-medium">Nhận xét (tùy chọn):</label>
        <textarea v-model="form.comment" rows="4" class="mt-1 w-full border rounded p-2" placeholder="Nói rõ ấn tượng của bạn"></textarea>
      </div>

      <!-- File Upload -->
      <div class="mb-4">
        <label class="block text-sm font-medium">Hình ảnh đính kèm (nếu có)</label>
        <input type="file" multiple accept="image/*" @change="handleFileChange" class="mt-1 w-full border rounded p-2" />
      </div>

      <!-- Preview -->
      <div v-if="previewImages.length > 0" class="mb-4 flex flex-wrap gap-4">
        <div v-for="(img, index) in previewImages" :key="index" class="relative w-24 h-24">
          <img :src="img" class="object-cover w-full h-full rounded border" />
          <button @click="removeImage(index)" type="button" class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-5 h-5 text-xs flex items-center justify-center hover:bg-red-700">×</button>
        </div>
      </div>

      <!-- Submit -->
      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        Gửi đánh giá
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const props = defineProps({
  productDetailId: Number,
  userId: Number,
})

const form = ref({
  rating: '',
  comment: '',
  images: [],
})

const previewImages = ref([])

const handleFileChange = (event) => {
  const files = Array.from(event.target.files)

  form.value.images = files

  // Cập nhật preview
  previewImages.value = files.map((file) => URL.createObjectURL(file))
}

const removeImage = (index) => {
  form.value.images.splice(index, 1)
  previewImages.value.splice(index, 1)
}

const submitReview = async () => {
  try {
    // 1. Gửi review
    const reviewPayload = {
      rating: form.value.rating,
      comment: form.value.comment,
      productDetail: { id: props.productDetailId },
      user: { id: props.userId },
    }

    const res = await axios.post('/api/reviews', reviewPayload)
    const reviewId = res.data.id

    // 2. Gửi ảnh nếu có
    if (form.value.images.length > 0) {
      const formData = new FormData()
      form.value.images.forEach((file) => {
        formData.append('files', file)
      })
      await axios.post(`/api/reviews/${reviewId}/images`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
    }

    alert('Gửi đánh giá thành công!')
    form.value = { rating: '', comment: '', images: [] }
    previewImages.value = []
  } catch (error) {
    console.error(error)
    alert('Gửi đánh giá thất bại.')
  }
}
</script>

<style scoped>
/* Optional: Customize preview style */
</style>
