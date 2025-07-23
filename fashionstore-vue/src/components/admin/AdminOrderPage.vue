<template>
  <div class="p-4">
    <h2>📦 Quản lý đơn hàng</h2>
    <hr />
    <!-- Bộ lọc trạng thái -->
    <div class="mb-3">
      <label>Trạng thái:</label>
      <select v-model="status" class="form-select w-25 d-inline-block" @change="filterByStatus">
        <option value="">-- Tất cả --</option>
        <option value="PENDING">PENDING</option>
        <option value="CONFIRMED">CONFIRMED</option>
        <option value="SHIPPED">SHIPPED</option>
        <option value="COMPLETED">COMPLETED</option>
        <option value="CANCELED">CANCELED</option>
      </select>
    </div>
    <hr />
    <!-- Bảng dữ liệu -->
    <table class="table table-bordered mt-3">
      <thead class="table-light">
        <tr>
          <th>Mã đơn</th>
          <th>Khách hàng</th>
          <th>Trạng thái</th>
          <th>Tổng tiền</th>
          <th>Ngày tạo</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>#{{ order.id }}</td>
          <td>{{ order.receiverName }}</td>
          <td>
            <select v-model="order.status" class="form-select form-select-sm" @change="updateStatus(order)">
              <option value="PENDING">PENDING</option>
              <option value="CONFIRMED">CONFIRMED</option>
              <option value="SHIPPED">SHIPPED</option>
              <option value="COMPLETED">COMPLETED</option>
              <option value="CANCELED">CANCELED</option>
            </select>
          </td>
          <td>{{ formatCurrency(order.totalAmount) }}</td>
          <td>{{ formatDate(order.createdAt) }}</td>
          <td>
            <router-link :to="`/admin/orders/${order.id}`" class="btn btn-sm btn-primary me-2">Chi tiết</router-link>
            <button class="btn btn-sm btn-danger" v-if="order.status !== 'CANCELED'" @click="cancelOrder(order.id)">
              Hủy
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Phân trang -->
    <nav v-if="totalPages > 1" class="mt-3">
      <ul class="pagination">
        <li v-for="p in totalPages" :key="p" class="page-item" :class="{ active: p - 1 === page }">
          <button class="page-link" @click="changePage(p - 1)">
            {{ p }}
          </button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script>
import api from '@/api';

export default {
  name: 'OrderManager',
  data() {
    return {
      orders: [],
      status: '',
      page: 0,
      totalPages: 1,
      size: 5
    };
  },
  mounted() {
    this.loadOrders();
  },
  methods: {
    async loadOrders() {
      try {
        const res = await api.get('/orders/page', {
          params: {
            status: this.status,
            page: this.page,
            size: this.size
          }
        });


        this.orders = res.data.content || [];
        this.totalPages = res.data.totalPages || 1;
      } catch (e) {
        console.error('Lỗi khi tải đơn hàng:', e);
      }
    },
    changePage(p) {
      this.page = p;
      this.loadOrders();
    },
    filterByStatus() {
      this.page = 0;
      this.loadOrders();
    },
    async updateStatus(order) {
      try {
        await api.put(`/orders/${order.id}/status`, { status: order.status });
        alert(`Đã cập nhật trạng thái đơn #${order.id} thành ${order.status}`);
      } catch (e) {
        alert('Lỗi cập nhật trạng thái');
        console.error(e);
      }
    },
    async cancelOrder(orderId) {
      const confirmCancel = confirm(`Bạn chắc chắn muốn hủy đơn hàng #${orderId}?`);
      if (!confirmCancel) return;

      try {
        await api.put(`/orders/${orderId}/status`, { status: 'CANCELED' });
        this.loadOrders();
        alert(`Đơn hàng #${orderId} đã bị hủy`);
      } catch (e) {
        alert('Lỗi khi hủy đơn hàng');
        console.error(e);
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleString('vi-VN');
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