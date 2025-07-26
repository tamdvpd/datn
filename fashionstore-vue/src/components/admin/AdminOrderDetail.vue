<template>
  <div class="p-4" v-if="order">
    <h2>🧾 Đơn hàng #{{ order.id }}</h2>
    <hr />

    <div class="row">
      <div class="col-md-6">
        <h5>Thông tin người nhận</h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item"><b>Họ tên:</b> {{ order.receiverName }}</li>
          <li class="list-group-item"><b>Điện thoại:</b> {{ order.receiverPhone }}</li>
          <li class="list-group-item"><b>Địa chỉ:</b> {{ order.receiverAddress }}</li>
          <li class="list-group-item"><b>Ghi chú:</b> {{ order.note || '-' }}</li>
        </ul>
      </div>
      <div class="col-md-6">
        <h5>Thông tin đơn hàng</h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item">
            <b>Trạng thái:</b>
            <select v-model="order.status" class="form-select form-select-sm d-inline-block w-auto" @change="updateStatus">
              <option value="PENDING">PENDING</option>
              <option value="CONFIRMED">CONFIRMED</option>
              <option value="SHIPPED">SHIPPED</option>
              <option value="COMPLETED">COMPLETED</option>
              <option value="CANCELED">CANCELED</option>
            </select>
          </li>
          <li class="list-group-item">
            <b>Mã vận đơn:</b>
            <div class="input-group input-group-sm w-75">
              <input v-model="trackingCode" class="form-control" placeholder="Nhập mã vận đơn" />
              <button class="btn btn-outline-primary" @click="updateTracking">Cập nhật</button>
            </div>
          </li>
          <li class="list-group-item"><b>Tổng tiền:</b> {{ formatCurrency(order.totalAmount) }}</li>
          <li class="list-group-item"><b>Giảm giá:</b> {{ formatCurrency(order.discountAmount) }}</li>
          <li class="list-group-item"><b>Phí vận chuyển:</b> {{ formatCurrency(order.shippingFee) }}</li>
          <li class="list-group-item"><b>Ngày tạo:</b> {{ formatDate(order.createdAt) }}</li>
        </ul>
      </div>
    </div>

    <h5 class="mt-4">Chi tiết sản phẩm</h5>
    <table class="table table-sm table-striped">
      <thead>
        <tr>
          <th>#</th>
          <th>Sản phẩm</th>
          <th>Màu/Size</th>
          <th>Số lượng</th>
          <th>Đơn giá</th>
          <th>Thành tiền</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(it, idx) in order.items" :key="it.id || idx">
          <td>{{ idx + 1 }}</td>
          <td>{{ it.productName }}</td>
          <td>{{ it.color }} / {{ it.size }}</td>
          <td>{{ it.quantity }}</td>
          <td>{{ formatCurrency(it.unitPrice) }}</td>
          <td>{{ formatCurrency(it.unitPrice * it.quantity) }}</td>
        </tr>
        <tr v-if="!order.items || order.items.length === 0">
          <td colspan="6" class="text-center text-muted">Không có sản phẩm</td>
        </tr>
      </tbody>
    </table>

    <router-link to="/admin/orders" class="btn btn-secondary mt-3">⬅ Quay lại</router-link>
  </div>

  <div v-else class="p-4 text-center">
    <div class="spinner-border text-primary"></div>
  </div>
</template>

<script>
import api from '@/api';

export default {
  name: 'AdminOrderDetail',
  data() {
    return {
      id: Number(this.$route.params.id),
      order: null,
      trackingCode: '',
    };
  },
  async mounted() {
    await this.load();
  },
  methods: {
    async load() {
      try {
        const res = await api.get(`/admin/orders/${this.id}`);
        this.order = res.data;
        this.trackingCode = this.order.trackingCode || '';
      } catch (err) {
        console.error(err);
        alert('Không tải được chi tiết đơn hàng');
        this.$router.push('/admin/orders');
      }
    },
    async updateStatus() {
      try {
        await api.put(`/admin/orders/${this.id}/status`, {
          status: this.order.status,
        });
        alert('✅ Cập nhật trạng thái thành công');
      } catch (err) {
        alert('❌ Cập nhật trạng thái thất bại');
        console.error(err);
        this.load(); // revert
      }
    },
    async updateTracking() {
      try {
        await api.put(`/admin/orders/${this.id}/tracking`, {
          trackingCode: this.trackingCode,
        });
        alert('✅ Cập nhật trackingCode thành công');
        await this.load();
      } catch (err) {
        alert('❌ Không thể cập nhật trackingCode');
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
