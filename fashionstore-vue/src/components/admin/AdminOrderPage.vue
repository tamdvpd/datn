<template>
  <div class="container my-4">
    <h2 class="text-center mb-4">📦 Quản lý đơn hàng</h2>

    <!-- Danh sách đơn hàng -->
    <div class="table-responsive">
      <table class="table table-bordered table-hover align-middle text-center">
        <thead class="table-light">
          <tr>
            <th>Mã đơn</th>
            <th>Khách hàng</th>
            <th>Ngày đặt</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Chi tiết</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.id }}</td>
            <td>{{ order.user?.fullName || '—' }}</td>
            <td>{{ formatDate(order.createdAt) }}</td>
            <td class="text-end">{{ formatCurrency(order.totalAmount) }}</td>
            <td>{{ order.status }}</td>
            <td>
              <button class="btn btn-sm btn-outline-primary" @click="viewOrderDetail(order.id)">
                Xem
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <nav v-if="totalPages > 1" class="mt-3">
      <ul class="pagination justify-content-center">
        <li class="page-item" :class="{ disabled: page === 0 }">
          <button class="page-link" @click="changePage(page - 1)">«</button>
        </li>
        <li v-for="p in totalPages" :key="p" class="page-item" :class="{ active: page === p - 1 }">
          <button class="page-link" @click="changePage(p - 1)">{{ p }}</button>
        </li>
        <li class="page-item" :class="{ disabled: page === totalPages - 1 }">
          <button class="page-link" @click="changePage(page + 1)">»</button>
        </li>
      </ul>
    </nav>

    <!-- Chi tiết đơn hàng -->
    <div v-if="selectedOrder" class="card mt-5 shadow">
      <div class="card-header d-flex justify-content-between align-items-center bg-primary text-white">
        <h5 class="mb-0">Chi tiết đơn hàng #{{ selectedOrder.id }}</h5>
        <button class="btn btn-sm btn-light" @click="selectedOrder = null">Đóng</button>
      </div>
      <div class="card-body">
        <p><strong>Người nhận:</strong> {{ selectedOrder.receiverName }} - {{ selectedOrder.receiverPhone }}</p>
        <p><strong>Địa chỉ:</strong> {{ selectedOrder.receiverAddress }}</p>
        <p><strong>Ghi chú:</strong> {{ selectedOrder.note || 'Không có' }}</p>
       <p><strong>Trạng thái hiện tại:</strong> {{ selectedOrder.statusVi }}</p>
        <!-- Form cập nhật trạng thái -->
        <div class="my-3">
          <label class="form-label fw-bold me-2">Cập nhật trạng thái:</label>
          <select class="form-select d-inline w-auto" v-model="updatedStatus">
            <option value="PENDING">Chờ xử lý</option>
            <option value="CONFIRMED">Đã xác nhận</option>
            <option value="PROCESSING">Đang xử lý</option>
            <option value="SHIPPED">Đang giao</option>
            <option value="DELIVERED">Đã giao</option>
            <option value="CANCELLED">Đã huỷ</option>
          </select>
          <button class="btn btn-sm btn-primary ms-2" @click="updateStatus">Lưu</button>
        </div>

        <h6 class="fw-bold">🛒 Danh sách sản phẩm</h6>
        <div class="table-responsive">
          <table class="table table-bordered table-sm">
            <thead class="table-light">
              <tr>
                <th>Tên</th>
                <th>Màu</th>
                <th>Size</th>
                <th>SL</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in selectedOrder.orderDetails" :key="item.id">
                <td>{{ item.productDetail?.product?.name }}</td>
                <td>{{ item.productDetail?.color }}</td>
                <td>{{ item.productDetail?.size }}</td>
                <td class="text-center">{{ item.quantity }}</td>
                <td class="text-end">{{ formatCurrency(item.unitPrice) }}</td>
                <td class="text-end">{{ formatCurrency(item.unitPrice * item.quantity) }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="text-end mt-3">
          <p><strong>Phí vận chuyển:</strong> {{ formatCurrency(selectedOrder.shippingFee) }}</p>
          <p><strong>Giảm giá:</strong> {{ formatCurrency(selectedOrder.discountAmount) }}</p>
          <h5><strong>Tổng tiền:</strong> {{ formatCurrency(selectedOrder.totalAmount) }}</h5>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
const API_BASE = 'http://localhost:8080';

export default {
  name: 'AdminOrderPage',
  data() {
    return {
      orders: [],
      page: 0,
      size: 10,
      totalPages: 0,
      selectedOrder: null,
      updatedStatus: ''
    };
  },
  methods: {
    async loadOrders() {
      try {
        const res = await axios.get(`${API_BASE}/orders/admin`, {
          params: { page: this.page, size: this.size }
        });
        this.orders = res.data.content;
        this.totalPages = res.data.totalPages;
      } catch (err) {
        console.error('Lỗi tải đơn hàng:', err);
      }
    },
    async viewOrderDetail(orderId) {
      try {
        const res = await axios.get(`${API_BASE}/orders/${orderId}`, {
          params: { admin: true }
        });
        this.selectedOrder = res.data;
        this.updatedStatus = res.data.status;
      } catch (err) {
        console.error('Lỗi tải chi tiết đơn hàng:', err);
      }
    },
    async updateStatus() {
      try {
        await axios.put(`${API_BASE}/orders/${this.selectedOrder.id}/status`, null, {
          params: { status: this.updatedStatus, admin: true }
        });
        alert('✅ Cập nhật trạng thái thành công!');
        this.viewOrderDetail(this.selectedOrder.id);
        this.loadOrders();
      } catch (err) {
        if (err.response && err.response.data) {
          alert(err.response.data);
        } else {
          alert('❌ Cập nhật trạng thái thất bại!');
        }
      }
    },
    changePage(newPage) {
      if (newPage >= 0 && newPage < this.totalPages) {
        this.page = newPage;
        this.loadOrders();
      }
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0);
    },
    formatDate(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleDateString('vi-VN') : '—';
    }
  },
  mounted() {
    this.loadOrders();
  }
};
</script>
