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
        <p><strong>Trạng thái hiện tại:</strong> {{ selectedOrder.status }}</p>

        <!-- Form cập nhật trạng thái -->
        <div class="my-3">
          <label class="form-label fw-bold me-2">Cập nhật trạng thái:</label>
          <select class="form-select d-inline w-auto" v-model="updatedStatus">
            <option value="PENDING">Chờ xử lý</option>
            <option value="CONFIRMED">Đã xác nhận</option>
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

export default {
  name: 'AdminOrderPage',
  data() {
    return {
      orders: [],
      selectedOrder: null,
      updatedStatus: ''
    };
  },
  methods: {
    loadOrders() {
      axios.get('http://localhost:8080/orders/admin')
        .then(res => this.orders = res.data)
        .catch(err => console.error('Lỗi tải đơn hàng:', err));
    },
    viewOrderDetail(orderId) {
      axios.get(`http://localhost:8080/orders/${orderId}?admin=true`)
        .then(res => {
          this.selectedOrder = res.data;
          this.updatedStatus = res.data.status;
        })
        .catch(err => console.error('Lỗi tải chi tiết đơn hàng:', err));
    },
    async updateStatus() {
      try {
        await axios.put(`http://localhost:8080/orders/${this.selectedOrder.id}/status`, {
          status: this.updatedStatus,
          admin: true
        });
        alert('✅ Cập nhật trạng thái thành công!');
        this.viewOrderDetail(this.selectedOrder.id); // refresh chi tiết
        this.loadOrders(); // refresh danh sách
      } catch (err) {
        alert('❌ Cập nhật trạng thái thất bại!');
        console.error(err);
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
