<template>
  <div class="p-4">
    <h3>🛍 Đơn hàng của bạn</h3>

    <table class="table table-bordered mt-3" v-if="orders.length">
      <thead class="table-light">
        <tr>
          <th>Mã đơn</th>
          <th>Ngày tạo</th>
          <th>Trạng thái</th>
          <th>Tổng tiền</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>#{{ order.id }}</td>
          <td>{{ formatDate(order.createdAt) }}</td>
          <td><span class="badge bg-info">{{ order.status }}</span></td>
          <td>{{ formatCurrency(order.totalAmount) }}</td>
          <td>
            <router-link :to="`/my-orders/${order.id}`" class="btn btn-sm btn-outline-primary">
              Xem chi tiết
            </router-link>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else class="alert alert-info">Bạn chưa có đơn hàng nào.</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '@/api';

const orders = ref([]);

onMounted(async () => {
  try {
    const res = await api.get('/orders'); // ⚠️ lọc theo user nếu cần
    orders.value = res.data;
  } catch (e) {
    console.error('Lỗi tải đơn hàng:', e);
  }
});

const formatDate = (d) => new Date(d).toLocaleString('vi-VN');
const formatCurrency = (v) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v);
</script>
