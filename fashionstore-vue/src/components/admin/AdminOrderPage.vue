<template>
  <div class="p-4">
    <h2>📦 Quản lý đơn hàng</h2>
    <hr />

    <!-- Bộ lọc -->
    <div class="row g-3 align-items-end">
      <div class="col-auto">
        <label class="form-label">Trạng thái</label>
        <select v-model="status" class="form-select" @change="filter">
          <option value="">-- Tất cả --</option>
          <option value="PENDING">PENDING</option>
          <option value="CONFIRMED">CONFIRMED</option>
          <option value="SHIPPED">SHIPPED</option>
          <option value="COMPLETED">COMPLETED</option>
          <option value="CANCELED">CANCELED</option>
        </select>
      </div>
      <div class="col-auto">
        <label class="form-label">Từ ngày</label>
        <input type="date" v-model="from" class="form-control" @change="filter" />
      </div>
      <div class="col-auto">
        <label class="form-label">Đến ngày</label>
        <input type="date" v-model="to" class="form-control" @change="filter" />
      </div>
      <div class="col-auto">
        <button class="btn btn-secondary" @click="resetFilter">Reset</button>
      </div>
    </div>

    <hr />

    <!-- Table -->
    <table class="table table-bordered mt-3" v-if="!loading">
      <thead class="table-light">
        <tr>
          <th>#</th>
        <th>Khách hàng</th>
        <th>Trạng thái</th>
        <th>Tổng tiền</th>
        <th>Ngày tạo</th>
        <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="o in orders" :key="o.id">
          <td>#{{ o.id }}</td>
          <td>{{ o.customerName || o.receiverName }}</td>
          <td style="min-width: 160px">
            <select v-model="o.status" class="form-select form-select-sm" @change="updateStatus(o)">
              <option value="PENDING">PENDING</option>
              <option value="CONFIRMED">CONFIRMED</option>
              <option value="SHIPPED">SHIPPED</option>
              <option value="COMPLETED">COMPLETED</option>
              <option value="CANCELED">CANCELED</option>
            </select>
          </td>
          <td>{{ formatCurrency(o.totalAmount) }}</td>
          <td>{{ formatDate(o.createdAt) }}</td>
          <td class="text-nowrap">
            <router-link :to="`/admin/orders/${o.id}`" class="btn btn-sm btn-primary me-2">
              Chi tiết
            </router-link>
            <button
              class="btn btn-sm btn-danger"
              v-if="o.status !== 'CANCELED'"
              @click="cancelOrder(o.id)"
            >
              Hủy
            </button>
          </td>
        </tr>
        <tr v-if="orders.length === 0">
          <td colspan="6" class="text-center text-muted">Không có dữ liệu</td>
        </tr>
      </tbody>
    </table>

    <div v-if="loading" class="text-center my-4">
      <div class="spinner-border text-primary" role="status"></div>
    </div>

    <!-- Pagination -->
    <nav v-if="totalPages > 1 && !loading" class="mt-3">
      <ul class="pagination">
        <li
          v-for="p in totalPages"
          :key="p"
          class="page-item"
          :class="{ active: p - 1 === page }"
        >
          <button class="page-link" @click="changePage(p - 1)">{{ p }}</button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script>
import api from '@/api';

export default {
  name: 'AdminOrderPage',
  data() {
    return {
      orders: [],
      page: 0,
      size: 10,
      totalPages: 1,
      status: '',
      from: '',
      to: '',
      loading: false,
    };
  },
  mounted() {
    this.loadOrders();
  },
  methods: {
    async loadOrders() {
      this.loading = true;
      try {
        const res = await api.get('/admin/orders/page', {
          params: {
            page: this.page,
            size: this.size,
            status: this.status || undefined,
            from: this.from || undefined,
            to: this.to || undefined,
          },
        });
        this.orders = res.data.content || [];
        this.totalPages = res.data.totalPages || 1;
      } catch (err) {
        console.error('❌ Lỗi khi tải đơn hàng:', {
          status: err.response?.status,
          data: err.response?.data,
          url: err.config?.url,
        });
        alert(err.response?.data?.message || 'Không thể tải đơn hàng');
      } finally {
        this.loading = false;
      }
    },
    changePage(p) {
      this.page = p;
      this.loadOrders();
    },
    filter() {
      this.page = 0;
      this.loadOrders();
    },
    resetFilter() {
      this.status = '';
      this.from = '';
      this.to = '';
      this.filter();
    },
    async updateStatus(order) {
      try {
        await api.put(`/admin/orders/${order.id}/status`, { status: order.status });
        alert(`✅ Cập nhật trạng thái thành công (#${order.id} → ${order.status})`);
      } catch (err) {
        alert('❌ Lỗi khi cập nhật trạng thái');
        console.error(err);
        this.loadOrders(); // revert lại UI
      }
    },
    async cancelOrder(orderId) {
      if (!confirm(`Bạn chắc chắn muốn huỷ đơn #${orderId}?`)) return;
      try {
        await api.put(`/admin/orders/${orderId}/status`, { status: 'CANCELED' });
        alert(`🗑 Đã huỷ đơn #${orderId}`);
        this.loadOrders();
      } catch (err) {
        alert('❌ Lỗi huỷ đơn');
        console.error(err);
      }
    },
    formatDate(d) {
      return new Date(d).toLocaleString('vi-VN');
    },
    formatCurrency(v) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v || 0);
    },
  },
};
</script>