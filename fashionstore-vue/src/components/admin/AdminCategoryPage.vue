<template>
  <div class="container mt-4">
    <h2 class="mb-3">Danh sách danh mục</h2>

    <div v-if="loading" class="text-center my-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <div v-else>
      <table class="table table-bordered table-hover">
        <thead class="table-light">
          <tr>
            <th>ID</th>
            <th>Tên danh mục</th>
            <th>Mô tả</th>
            <th>Ngày tạo</th>
            <th>Ngày cập nhật</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="category in categories" :key="category.id">
            <td>{{ category.id }}</td>
            <td>{{ category.name }}</td>
            <td>{{ category.description }}</td>
            <td>{{ formatDate(category.createdAt) }}</td>
            <td>{{ formatDate(category.updatedAt) }}</td>
          </tr>
        </tbody>
      </table>
      <div v-if="categories.length === 0" class="text-center text-muted">
        Không có danh mục nào.
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios'

const categories = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories')
    categories.value = response.data
  } catch (error) {
    console.error('Lỗi khi tải danh mục:', error)
  } finally {
    loading.value = false
  }
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>
