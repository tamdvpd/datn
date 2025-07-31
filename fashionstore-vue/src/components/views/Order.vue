<template>
  <div class="p-4">
    <h2 class="text-xl font-bold mb-4">🧾 Đơn hàng của tôi</h2>

    <!-- Lọc trạng thái -->
    <div class="mb-4 flex flex-wrap gap-2">
      <select v-model="selectedStatus" @change="loadOrders" class="border rounded px-3 py-1 text-sm">
        <option value="">Tất cả trạng thái</option>
        <option value="PENDING">Chờ xử lý</option>
        <option value="CONFIRMED">Đã xác nhận</option>
        <option value="SHIPPED">Đang giao</option>
        <option value="DELIVERED">Đã giao</option>
        <option value="CANCELLED">Đã huỷ</option>
      </select>
    </div>

    <!-- Danh sách đơn -->
    <table class="w-full text-sm border mb-6">
      <thead class="bg-gray-100">
        <tr>
          <th class="border px-3 py-2">Mã đơn</th>
          <th class="border px-3 py-2">Ngày đặt</th>
          <th class="border px-3 py-2">Tổng tiền</th>
          <th class="border px-3 py-2">Trạng thái</th>
          <th class="border px-3 py-2">Chi tiết</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td class="border px-3 py-2">{{ order.id }}</td>
          <td class="border px-3 py-2">{{ formatDate(order.createdAt) }}</td>
          <td class="border px-3 py-2 text-right">{{ formatCurrency(order.totalAmount) }}</td>
          <td class="border px-3 py-2 text-center">{{ order.status }}</td>
          <td class="border px-3 py-2 text-center">
            <button @click="selectOrder(order)" class="text-blue-600 hover:underline">Xem</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Chi tiết đơn hàng -->
    <div v-if="selectedOrder" class="bg-white p-4 border rounded">
      <div class="flex justify-between items-center mb-2">
        <h3 class="text-lg font-semibold">Đơn hàng #{{ selectedOrder.id }}</h3>
        <button class="text-red-500 hover:underline" @click="selectedOrder = null">Đóng</button>
      </div>

      <p><strong>Người nhận:</strong> {{ selectedOrder.receiverName }} - {{ selectedOrder.receiverPhone }}</p>
      <p><strong>Địa chỉ:</strong> {{ selectedOrder.receiverAddress }}</p>
      <p><strong>Ghi chú:</strong> {{ selectedOrder.note || 'Không có' }}</p>
      <p><strong>Trạng thái:</strong> {{ selectedOrder.status }}</p>

      <h4 class="mt-4 font-semibold">🛒 Sản phẩm</h4>
      <table class="w-full border text-sm">
        <thead class="bg-gray-100">
          <tr>
            <th class="border px-2 py-1">Tên</th>
            <th class="border px-2 py-1">Màu</th>
            <th class="border px-2 py-1">Size</th>
            <th class="border px-2 py-1">SL</th>
            <th class="border px-2 py-1">Đơn giá</th>
            <th class="border px-2 py-1">Tổng</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in selectedOrder.orderDetails" :key="item.id">
            <td class="border px-2 py-1">{{ item.productDetail?.product?.name }}</td>
            <td class="border px-2 py-1">{{ item.productDetail?.color }}</td>
            <td class="border px-2 py-1">{{ item.productDetail?.size }}</td>
            <td class="border px-2 py-1 text-center">{{ item.quantity }}</td>
            <td class="border px-2 py-1 text-right">{{ formatCurrency(item.unitPrice) }}</td>
            <td class="border px-2 py-1 text-right">{{ formatCurrency(item.unitPrice * item.quantity) }}</td>
          </tr>
        </tbody>
      </table>

      <div class="text-right mt-3">
        <p><strong>Phí vận chuyển:</strong> {{ formatCurrency(selectedOrder.shippingFee) }}</p>
        <p><strong>Giảm giá:</strong> {{ formatCurrency(selectedOrder.discountAmount) }}</p>
        <p class="font-semibold text-lg">Tổng: {{ formatCurrency(selectedOrder.totalAmount) }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Order',
  data() {
    return {
      email: 'phucnvpd10600@gmail.com', // Thay bằng lấy từ token nếu có
      orders: [],
      selectedOrder: null,
      selectedStatus: ''
    };
  },
  methods: {
    async loadOrders() {
      const url = this.selectedStatus
        ? `http://localhost:8080/orders/user?email=${this.email}&status=${this.selectedStatus}`
        : `http://localhost:8080/orders/user?email=${this.email}`;

      try {
        const res = await axios.get(url);
        this.orders = res.data;
      } catch (e) {
        console.error(e);
        alert('Không thể tải đơn hàng');
      }
    },
    async selectOrder(order) {
      try {
        const res = await axios.get(`http://localhost:8080/orders/${order.id}?email=${this.email}`);
        this.selectedOrder = res.data;
      } catch (e) {
        console.error(e);
        alert('Không thể tải chi tiết đơn hàng');
      }
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0);
    },
    formatDate(dateString) {
      const d = new Date(dateString);
      return d.toLocaleDateString('vi-VN');
    }
  },
  mounted() {
    this.loadOrders();
  }
};
</script>

<style scoped>
table th,
table td {
  border: 1px solid #ddd;
}
</style>