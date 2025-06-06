<template>
  <div>
    <MainHeader />
    <main>
      <div class="container mt-4">
        <div class="row">
          <div class="col-4">
            <ul class="list-group">
              <button><li class="list-group-item">Deal sốc hội viên</li></button>
              <button><li class="list-group-item">Áo</li></button>
              <button><li class="list-group-item">Set bộ</li></button>
              <button><li class="list-group-item">Quần</li></button>
              <button><li class="list-group-item">Phụ kiện</li></button>
              <button><li class="list-group-item">Sản phẩm khác</li></button>
            </ul>
          </div>
          <div class="col-8">
            <img
              src="@/assets/img/banner.jpg"
              width="100%"
              class="img-fluid rounded shadow"
              alt="Banner"
            />
          </div>
        </div>
        <div class="section-title">SẢN PHẨM BÁN CHẠY</div>

        <div class="row g-3">
          <div class="col-md-3" v-for="n in 4" :key="'top-' + n">
            <div class="position-relative product-card">
              <img :src="require(`@/assets/img/img${n}.jpg`)" class="img-fluid" alt="Product Image" />
              <div class="mt-2">
                <div class="product-name">Sản phẩm {{ n }}</div>
                <div class="product-price">Giá: {{ 200000 + n * 50000 }} VNĐ</div>
              </div>
            </div>
          </div>
        </div>

        <div class="section-title">SẢN PHẨM SALE</div>

        <div class="row g-3">
          <div class="col-md-3" v-for="n in 4" :key="'sale-' + n">
            <div class="position-relative product-card">
              <img :src="require(`@/assets/img/img${5 - n}.jpg`)" class="img-fluid" alt="Product Image" />
              <div class="mt-2">
                <div class="product-name">Sản phẩm sale {{ n }}</div>
                <div class="product-price">Giá: {{ 150000 + n * 30000 }} VNĐ</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <MainFooter />
  </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

export default {
  name: 'HomePage',
  components: {
    MainHeader,
    MainFooter,
  },
};
</script>

<style scoped>
.product-card img {
  width: 300px;
  height: 400px;
}
.product-name {
  font-weight: bold;
}
.product-price {
  color: red;
  font-weight: bold;
}
.section-title {
  background-color: #00b5e2;
  color: white;
  margin-top: 20px;
  padding: 8px 16px;
  font-size: 1.2rem;
  font-weight: bold;
  display: inline-block;
  margin-bottom: 20px;
}
</style>
