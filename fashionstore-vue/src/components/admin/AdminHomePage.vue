<template>
  <div class="overview">
    <h2>🎯 Tổng quan quản trị</h2>
    <div class="row mt-4">
      <div class="col-md-3" v-for="(card, i) in cards" :key="i">
        <div class="card text-center shadow">
          <div class="card-body">
            <div class="h3 mb-2">{{ card.count }}</div>
            <p class="card-title">{{ card.title }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminHomePage',
  data() {
    return {
      cards: [
        { title: 'Sản phẩm', count: 120 },
        { title: 'Đơn hàng hôm nay', count: 30 },
        { title: 'Khách hàng mới', count: 8 },
        { title: 'Đơn đang giao', count: 15 },
      ],
    };
  },
};
</script>

<style scoped>
.card {
  background-color: #f9fafb;
  border-radius: 10px;
  padding: 20px;
}
</style>
