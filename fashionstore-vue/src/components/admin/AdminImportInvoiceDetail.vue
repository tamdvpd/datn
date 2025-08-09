<template>
  <div class="p-6 max-w-6xl mx-auto bg-white rounded-xl shadow-md mt-4">
    <h2 class="text-2xl font-bold mb-4 text-gray-800">📄 Chi tiết phiếu nhập: {{ invoiceId }}</h2>

    <div v-if="details.length > 0">
      <table class="table-auto w-full border border-gray-300">
        <thead class="bg-gray-100">
          <tr>
            <th class="border px-4 py-2">Tên sản phẩm</th>
            <th class="border px-4 py-2">Màu</th>
            <th class="border px-4 py-2">Size</th>
            <th class="border px-4 py-2">Số lượng</th>
            <th class="border px-4 py-2">Đơn giá</th>
            <th class="border px-4 py-2">Thành tiền</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in details" :key="item.id">
            <td class="border px-4 py-2">{{ item.productDetail.product.name }}</td>
            <td class="border px-4 py-2">{{ item.productDetail.color }}</td>
            <td class="border px-4 py-2">{{ item.productDetail.size }}</td>
            <td class="border px-4 py-2">{{ item.quantity }}</td>
            <td class="border px-4 py-2">{{ formatCurrency(item.unitPrice) }}</td>
            <td class="border px-4 py-2">{{ formatCurrency(item.unitPrice * item.quantity) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="text-gray-600 mt-4">Không có dữ liệu chi tiết.</div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ImportInvoiceDetail',
  data() {
    return {
      invoiceId: this.$route.params.id, // lấy từ router
      details: []
    }
  },
  methods: {
    loadDetails() {
      axios.get(`/api/import-invoice-details/invoice/${this.invoiceId}`)
        .then(res => {
          this.details = res.data
        })
        .catch(err => {
          console.error('Lỗi khi tải chi tiết phiếu nhập:', err)
        })
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(value)
    }
  },
  mounted() {
    this.loadDetails()
  }
}
</script>
