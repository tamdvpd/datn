<template>
    <div class="max-w-6xl mx-auto bg-white shadow-lg rounded-2xl p-8 space-y-8">
      <h2 class="text-3xl font-bold text-blue-700">📥 Chi tiết Phiếu nhập #{{ importInvoiceId }}</h2>
  
      <!-- Form nhập dòng mới -->
      <div class="bg-gray-50 p-4 rounded-lg shadow-inner grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label class="block mb-1 text-sm font-medium">Mã biến thể SP</label>
          <input v-model="newDetail.productDetailId" type="number" class="input" placeholder="VD: 101" />
        </div>
        <div>
          <label class="block mb-1 text-sm font-medium">Số lượng</label>
          <input v-model="newDetail.quantity" type="number" class="input" placeholder="VD: 10" />
        </div>
        <div>
          <label class="block mb-1 text-sm font-medium">Đơn giá</label>
          <input v-model="newDetail.unitPrice" type="number" class="input" placeholder="VD: 150000" />
        </div>
      </div>
  
      <div class="flex justify-end">
        <button
          @click="addDetail"
          class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-xl text-sm font-medium transition"
        >
          ➕ Thêm dòng chi tiết
        </button>
      </div>
  
      <!-- Bảng danh sách -->
      <div class="overflow-x-auto">
        <table class="min-w-full border border-gray-300 text-sm text-gray-700">
          <thead class="bg-gray-100 text-gray-900">
            <tr>
              <th class="px-4 py-2 border">ID</th>
              <th class="px-4 py-2 border">Mã SP</th>
              <th class="px-4 py-2 border">SL</th>
              <th class="px-4 py-2 border">Đơn giá</th>
              <th class="px-4 py-2 border">Tổng</th>
              <th class="px-4 py-2 border">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="detail in details" :key="detail.id" class="hover:bg-gray-50 transition">
              <td class="px-4 py-2 border text-center">{{ detail.id }}</td>
              <td class="px-4 py-2 border text-center">{{ detail.productDetail?.id || '-' }}</td>
              <td class="px-4 py-2 border text-center">{{ detail.quantity }}</td>
              <td class="px-4 py-2 border text-center">{{ formatMoney(detail.unitPrice) }} ₫</td>
              <td class="px-4 py-2 border text-center">{{ formatMoney(detail.unitPrice * detail.quantity) }} ₫</td>
              <td class="px-4 py-2 border text-center">
                <button @click="deleteDetail(detail.id)" class="text-red-500 hover:underline">Xoá</button>
              </td>
            </tr>
            <tr v-if="details.length === 0">
              <td colspan="6" class="px-4 py-6 text-center text-gray-400">Chưa có dòng chi tiết nào.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, defineProps } from 'vue'
  import axios from 'axios'
  
  const props = defineProps({
    importInvoiceId: {
      type: Number,
      required: true
    }
  })
  
  const details = ref([])
  const newDetail = ref({
    productDetailId: '',
    quantity: '',
    unitPrice: ''
  })
  
  // Load chi tiết
  const fetchDetails = async () => {
    try {
      const res = await axios.get(`/api/import-invoice-details/invoice/${props.importInvoiceId}`)
      details.value = res.data
    } catch (error) {
      alert('Lỗi khi tải chi tiết!')
      console.error(error)
    }
  }
  
  // Thêm dòng chi tiết
  const addDetail = async () => {
    const { productDetailId, quantity, unitPrice } = newDetail.value
    if (!productDetailId || !quantity || !unitPrice) {
      alert('Vui lòng nhập đầy đủ thông tin!')
      return
    }
  
    const payload = {
      importInvoice: { id: props.importInvoiceId },
      productDetail: { id: productDetailId },
      quantity,
      unitPrice,
      user: { id: 1 } // Giả định người dùng đang login
    }
  
    try {
      await axios.post('/api/import-invoice-details', payload)
      await fetchDetails()
      newDetail.value = { productDetailId: '', quantity: '', unitPrice: '' }
    } catch (error) {
      alert('Lỗi khi thêm dòng nhập!')
      console.error(error)
    }
  }
  
  // Xoá dòng chi tiết
  const deleteDetail = async (id) => {
    if (!confirm('Bạn có chắc muốn xoá dòng này?')) return
    try {
      await axios.delete(`/api/import-invoice-details/${id}`)
      await fetchDetails()
    } catch (error) {
      alert('Lỗi khi xoá dòng chi tiết!')
      console.error(error)
    }
  }
  
  // Format tiền
  const formatMoney = (v) => {
    if (!v) return '0'
    return new Intl.NumberFormat('vi-VN').format(v)
  }
  
  onMounted(fetchDetails)
  </script>
  
  <style scoped>
  .input {
    @apply w-full border px-3 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400;
  }
  </style>
  