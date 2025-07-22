<template>
  <div class="p-6 max-w-5xl mx-auto bg-white rounded shadow">
    <h2 class="text-2xl font-bold mb-4">📄 Chi tiết phiếu nhập #{{ invoiceId }}</h2>

    <div v-if="details.length">
      <table class="w-full border mt-4 text-sm">
        <thead class="bg-gray-100 text-left">
          <tr>
            <th class="p-2 border">STT</th>
            <th class="p-2 border">Sản phẩm</th>
            <th class="p-2 border">Số lượng</th>
            <th class="p-2 border">Đơn giá</th>
            <th class="p-2 border">Thành tiền</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in details" :key="item.id">
            <td class="p-2 border">{{ index + 1 }}</td>
            <td class="p-2 border">{{ item.productName }}</td>
            <td class="p-2 border">{{ item.quantity }}</td>
            <td class="p-2 border">{{ formatCurrency(item.unitPrice) }}</td>
            <td class="p-2 border">{{ formatCurrency(item.quantity * item.unitPrice) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <p v-else class="text-gray-500 mt-4">Không có sản phẩm nào trong phiếu nhập này.</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      invoiceId: this.$route.params.id,
      details: []
    };
  },
  mounted() {
    this.fetchDetails();
  },
  methods: {
    async fetchDetails() {
      try {
        const res = await axios.get(`http://localhost:8080/api/import-invoice-details/invoice/${this.invoiceId}`);
        this.details = res.data;
      } catch (error) {
        alert('Lỗi khi tải chi tiết phiếu nhập: ' + error.message);
      }
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(value);
    }
  }
};
</script>
