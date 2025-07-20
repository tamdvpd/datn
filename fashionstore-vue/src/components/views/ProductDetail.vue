<template>
  <div>
    <MainHeader />

    <div class="product-detail container py-4">
      <div class="row">
        <!-- Hình ảnh sản phẩm -->
        <div class="col-md-5">
          <img src="@/assets/img/img1.jpg" class="img-fluid rounded" alt="Áo sơ mi" />
          <div class="d-flex gap-2 mt-3">
            <img src="@/assets/img/img2.jpg" class="img-thumbnail" width="60" />
            <img src="@/assets/img/img3.jpg" class="img-thumbnail" width="60" />
            <img src="@/assets/img/img4.jpg" class="img-thumbnail" width="60" />
          </div>
        </div>

        <!-- Thông tin sản phẩm -->
        <div class="col-md-7">
          <h2>Áo sơ mi nam dài tay Café-Dris khử mùi – Xanh nhạt</h2>
          <p class="text-danger h4">390,000 VNĐ <span class="text-muted small">(FreeShip đơn hàng > 400k)</span></p>
          <p>Mã sản phẩm: TMS003 | Tình trạng: <span class="text-success">CÒN HÀNG</span></p>

          <div class="mb-2">
            <strong>Size:</strong>
            <button class="btn btn-outline-dark btn-sm mx-1">M</button>
            <button class="btn btn-outline-dark btn-sm mx-1">L</button>
            <button class="btn btn-outline-dark btn-sm mx-1">XL</button>
          </div>

          <div class="mb-2">
            <strong>Màu:</strong>
            <button class="btn btn-outline-dark btn-sm mx-1">Trắng</button>
            <button class="btn btn-outline-dark btn-sm mx-1">Xanh</button>
          </div>

          <div class="d-flex gap-2">
            <button class="btn btn-success">MUA NGAY</button>
            <button class="btn btn-danger">Thêm vào giỏ hàng</button>
          </div>
        </div>
      </div>

      <hr />

      <!-- Mô tả sản phẩm -->
      <div class="mt-4">
        <h4>MÔ TẢ SẢN PHẨM</h4>
        <p>
          Đây là chiếc áo sơ mi chất liệu tái chế, thân thiện với môi trường để diện trong tủ đồ của bạn.
          <br />
          - Café: Chất liệu được phát triển từ loại vải sợi từ bã cà phê.
          <br />
          - Dris: Công nghệ khử mùi vượt trội.
        </p>
        <img src="@/assets/img/banner.jpg" class="img-fluid rounded my-3" alt="Banner" />
      </div>

      <!-- Đánh giá sản phẩm -->
      <div class="mt-5">
        <h4>VIẾT ĐÁNH GIÁ SẢN PHẨM</h4>
        <div v-if="currentUser">
          <CreateReview :productDetailId="productDetailId" :userId="currentUser.id" />
        </div>
        <div v-else class="alert alert-info">
          Vui lòng <router-link to="/login">đăng nhập</router-link> để viết đánh giá.
        </div>
      </div>

      <!-- Sản phẩm liên quan -->
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
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';
import CreateReview from '@/components/views/CreateReview.vue';

export default {
  name: 'ProductDetail',
  components: {
    MainHeader,
    MainFooter,
    CreateReview,
  },
  data() {
    return {
      currentUser: JSON.parse(localStorage.getItem('user')) || null,
    };
  },
  computed: {
    productDetailId() {
      return this.$route.params.id; // Lấy id từ URL: /product/:id
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
