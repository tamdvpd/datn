<template>
  <div class="p-4" v-if="order">
    <h3>🧾 Chi tiết đơn hàng #{{ order.id }}</h3>

    <div class="mb-3">
      <strong>Khách hàng:</strong> {{ order.receiverName }} - {{ order.receiverPhone }}<br>
      <strong>Địa chỉ:</strong> {{ order.receiverAddress }}<br>
      <strong>Trạng thái:</strong>
      <span class="badge bg-info text-dark">{{ order.status }}</span><br>
      <strong>Mã vận đơn:</strong> {{ order.trackingCode || '(chưa có)' }}
    </div>

    <h5>Sản phẩm trong đơn</h5>
    <table class="table table-sm">
      <thead class="table-light">
        <tr>
          <th>Sản phẩm</th>
          <th>Màu</th>
          <th>Size</th>
          <th>SL</th>
          <th>Đơn giá</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in order.orderDetails" :key="item.id">
          <td>{{ item.productDetail.product.name }}</td>
          <td>{{ item.productDetail.color }}</td>
          <td>{{ item.productDetail.size }}</td>
          <td>{{ item.quantity }}</td>
          <td>{{ formatCurrency(item.unitPrice) }}</td>
        </tr>
      </tbody>
    </table>

    <h5 class="mt-4">🔄 Cập nhật trạng thái</h5>
    <div class="row g-2 align-items-center">
      <div class="col-md-4">
        <select v-model="newStatus" class="form-select">
          <option value="PENDING">PENDING</option>
          <option value="CONFIRMED">CONFIRMED</option>
          <option value="SHIPPED">SHIPPED</option>
          <option value="COMPLETED">COMPLETED</option>
          <option value="CANCELED">CANCELED</option>
        </select>
      </div>
      <div class="col-md-auto">
        <button @click="updateStatus" class="btn btn-success">Lưu trạng thái</button>
      </div>
    </div>

    <div class="mt-3">
      <label>Mã vận đơn:</label>
      <input v-model="trackingCode" class="form-control w-50 d-inline-block me-2" />
      <button @click="updateTracking" class="btn btn-primary">Cập nhật mã</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import api from '@/api';

const route = useRoute();
const order = ref(null);
const newStatus = ref('');
const trackingCode = ref('');

onMounted(async () => {
  const res = await api.get(`/orders/${route.params.id}`);
  order.value = res.data;
  newStatus.value = res.data.status;
  trackingCode.value = res.data.trackingCode || '';
});

const updateStatus = async () => {
  await api.put(`/orders/${order.value.id}/status`, { status: newStatus.value });
  alert('Đã cập nhật trạng thái');
};

const updateTracking = async () => {
  await api.put(`/orders/${order.value.id}/tracking`, { trackingCode: trackingCode.value });
  alert('Đã cập nhật mã vận đơn');
};

const formatCurrency = (v) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v);
</script>
