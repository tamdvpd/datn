<template>
  <div class="p-4">
    <h2>📦 Đơn hàng của tôi</h2>
    <hr />

    <!-- Bộ lọc trạng thái -->
    <div class="mb-3">
      <label>Trạng thái:</label>
      <select v-model="status" class="form-select w-auto d-inline-block" @change="filterByStatus">
        <option value="">-- Tất cả --</option>
        <option value="PENDING">PENDING</option>
        <option value="CONFIRMED">CONFIRMED</option>
        <option value="SHIPPED">SHIPPED</option>
        <option value="COMPLETED">COMPLETED</option>
        <option value="CANCELED">CANCELED</option>
      </select>
    </div>

    <!-- Bảng dữ liệu -->
    <table class="table table-hover mt-3" v-if="orders.length > 0">
      <thead class="table-light">
        <tr>
          <th>Mã đơn</th>
          <th>Người nhận</th>
          <th>Trạng thái</th>
          <th>Tổng tiền</th>
          <th>Ngày đặt</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>#{{ order.id }}</td>
          <td>{{ order.receiverName }}</td>
          <td>
            <span :class="getStatusBadgeClass(order.status)">
              {{ order.status }}
            </span>
          </td>
          <td>{{ formatCurrency(order.totalAmount) }}</td>
          <td>{{ formatDate(order.createdAt) }}</td>
          <td>
            <router-link
              :to="`/customer/orders/${order.id}`"
              class="btn btn-sm btn-outline-primary me-2"
            >
              Xem
            </router-link>
            <button
              v-if="order.status === 'PENDING'"
              class="btn btn-sm btn-outline-danger"
              @click="cancelOrder(order.id)"
            >
              Hủy
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else class="text-center text-muted">Không có đơn hàng nào.</div>

    <!-- Phân trang -->
    <nav v-if="totalPages > 1" class="mt-4">
      <ul class="pagination">
        <li
          class="page-item"
          :class="{ disabled: page === 0 }"
          @click="page > 0 && changePage(page - 1)"
        >
          <span class="page-link">‹</span>
        </li>
        <li
          v-for="p in totalPages"
          :key="p"
          class="page-item"
          :class="{ active: p - 1 === page }"
          @click="changePage(p - 1)"
        >
          <span class="page-link">{{ p }}</span>
        </li>
        <li
          class="page-item"
          :class="{ disabled: page >= totalPages - 1 }"
          @click="page < totalPages - 1 && changePage(page + 1)"
        >
          <span class="page-link">›</span>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script>
import api from '@/api';

export default {
  name: 'CustomerOrderPage',
  data() {
    return {
      orders: [],
      page: 0,
      size: 5,
      totalPages: 1,
      status: ''
    };
  },
  mounted() {
    this.loadOrders();
  },
  methods: {
    async loadOrders() {
      try {
        const res = await api.get('/customer/orders/page', {
          params: {
            page: this.page,
            size: this.size,
            status: this.status
          }
        });
        this.orders = res.data.content || [];
        this.totalPages = res.data.totalPages || 1;
      } catch (err) {
        console.error('Lỗi tải đơn hàng:', err);
        alert('Không thể tải đơn hàng của bạn.');
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
    async cancelOrder(orderId) {
      const confirmCancel = confirm(`Bạn có chắc chắn muốn hủy đơn hàng #${orderId}?`);
      if (!confirmCancel) return;

      try {
        await api.delete(`/customer/orders/${orderId}`);
        alert(`Đã hủy đơn hàng #${orderId}`);
        this.loadOrders();
      } catch (err) {
        alert('Không thể hủy đơn hàng.');
        console.error(err);
      }
    },
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleString('vi-VN');
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(value || 0);
    },
    getStatusBadgeClass(status) {
      switch (status) {
        case 'PENDING':
          return 'badge bg-warning text-dark';
        case 'CONFIRMED':
          return 'badge bg-info text-dark';
        case 'SHIPPED':
          return 'badge bg-primary';
        case 'COMPLETED':
          return 'badge bg-success';
        case 'CANCELED':
          return 'badge bg-danger';
        default:
          return 'badge bg-secondary';
      }
    }
  }
};
</script>
