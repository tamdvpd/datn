<template>
  <div>
    <MainHeader />
    <div class="product-detail container py-4" v-if="product">
      <div class="row">
        <div class="col-md-5">
          <img :src="product.imageUrl" class="img-fluid rounded" :alt="product.name" />
          <div class="d-flex gap-2 mt-3">
            <img v-for="(img, idx) in extraImages" :key="idx" :src="img" class="img-thumbnail" />
          </div>
        </div>
        <div class="col-md-7">
          <h2>{{ product.name }}</h2>
          <p class="text-danger h4">
            {{ formatPrice(product.price) }} VNĐ
            <span class="text-muted small">(FreeShip đơn hàng > 400k)</span>
          </p>

          <p>Mã sản phẩm: {{ product.id }} | Tình trạng: <span class="text-success">CÒN HÀNG</span></p>

          <div class="mb-2">
            <strong>Size:</strong>
            <button class="btn btn-outline-dark btn-sm mx-1" v-for="size in sizes" :key="size">{{ size }}</button>
          </div>

          <div class="mb-2">
            <strong>Màu:</strong>
            <button class="btn btn-outline-dark btn-sm mx-1" v-for="color in colors" :key="color">{{ color }}</button>
          </div>

          <div class="d-flex gap-2">
            <button class="btn btn-success">MUA NGAY</button>
            <button class="btn btn-danger">Thêm vào giỏ hàng</button>
          </div>
        </div>
      </div>

      <hr />

      <div class="mt-4">
        <h4>MÔ TẢ SẢN PHẨM</h4>
        <p>{{ product.description }}</p>
        <img src="@/assets/img/banner.jpg" class="img-fluid rounded my-3" alt="Banner" />
      </div>

      <div class="mt-5">
        <h4>SẢN PHẨM LIÊN QUAN</h4>
        <div class="row g-3">
          <div class="col-md-3" v-for="i in 4" :key="i">
            <div class="card">
              <img src="@/assets/img/img1.jpg" class="card-img-top" alt="Sản phẩm liên quan" />
              <div class="card-body">
                <p class="card-title">Áo sơ mi nam KDENIM</p>
                <p class="text-danger fw-bold">220,000 VNĐ</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script>
import axios from 'axios';
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

export default {
  name: 'ProductDetail',
  components: {
    MainHeader,
    MainFooter,
  },
  data() {
    return {
      product: null,
      sizes: ['S', 'M', 'L', 'XL'],
      colors: ['Trắng', 'Đen', 'Xanh'],
      extraImages: [
        '@/assets/img/img2.jpg',
        '@/assets/img/img3.jpg',
        '@/assets/img/img4.jpg'
      ]
    };
  },
  mounted() {
    const productId = this.$route.params.id;
    axios.get(`http://localhost:8080/products/${productId}`)
      .then(res => {
        this.product = res.data;
      })
      .catch(err => {
        console.error('Lỗi khi tải sản phẩm:', err);
      });
  },
  methods: {
    formatPrice(price) {
      return new Intl.NumberFormat('vi-VN').format(price);
    }
  }
};
</script>

<style scoped>
.product-detail h2 {
  font-weight: bold;
}
.img-thumbnail {
  cursor: pointer;
  width: 60px;
  height: 60px;
  object-fit: cover;
}
</style>
