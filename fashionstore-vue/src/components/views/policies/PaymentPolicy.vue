<template>
  <PolicyLayout title="Chính sách thanh toán" subtitle="Các hình thức thanh toán được hỗ trợ">
    <h2>Hình thức thanh toán</h2>
    <ul>
      <li>Thanh toán khi nhận hàng (COD).</li>
      <li>Chuyển khoản ngân hàng (ghi rõ nội dung đơn hàng/SDT).</li>
      <li>Ví điện tử/gateway (nếu có tích hợp).</li>
    </ul>

    <h2>Hóa đơn</h2>
    <p>Cung cấp hóa đơn điện tử theo yêu cầu trong vòng 03 ngày làm việc.</p>
  </PolicyLayout>
</template>

<script setup>
import PolicyLayout from './PolicyLayout.vue'
</script>
