<template>
  <div class="container mt-4">
    <h2 class="fw-bold mb-3">Danh sách yêu thích ❤️</h2>
    <div v-if="wishlists.length > 0" class="row g-3">
      <div v-for="item in wishlists" :key="item.id" class="col-md-3 col-sm-6">
        <div class="card h-100 shadow-sm">
          <img
            :src="getImageUrl(item.product.imageUrl)"
            class="card-img-top"
            :alt="item.product.name"
            style="height: 200px; object-fit: contain;"
          />
          <div class="card-body">
            <h6 class="card-title">{{ item.product.name }}</h6>
            <p class="text-danger fw-bold">
              {{ formatPrice(item.product.price) }}
            </p>
            <button class="btn btn-sm btn-outline-danger" @click="removeFromWishlist(item.id)">
              ❌ Xóa
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center mt-5">
      <p>📭 Chưa có sản phẩm nào trong wishlist.</p>
      <router-link to="/" class="btn btn-primary">Tiếp tục mua sắm</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "Wishlist",
  data() {
    return {
      wishlists: [],
    };
  },
  methods: {
    async fetchWishlist() {
      const user = JSON.parse(localStorage.getItem("user"));
      if (!user || !user.id) {
        alert("Vui lòng đăng nhập để xem wishlist!");
        this.$router.push("/login");
        return;
      }
      try {
        const res = await fetch(`http://localhost:8080/api/wishlists/user/${user.id}`);
        if (!res.ok) throw new Error("Không thể lấy wishlist");
        this.wishlists = await res.json();
      } catch (err) {
        console.error(err);
      }
    },
    getImageUrl(path) {
      if (!path) return require("@/assets/img/default-avatar.png");
      if (path.startsWith("http")) return path;
      return `http://localhost:8080/images/products/${path}`;
    },
    formatPrice(value) {
      return new Intl.NumberFormat("vi-VN").format(value) + " VNĐ";
    },
    async removeFromWishlist(id) {
      try {
        const res = await fetch(`http://localhost:8080/api/wishlists/${id}`, {
          method: "DELETE",
        });
        if (res.ok) {
          this.wishlists = this.wishlists.filter(item => item.id !== id);
          alert("❌ Đã xóa sản phẩm khỏi wishlist!");
        }
      } catch (err) {
        console.error(err);
      }
    },
  },
  mounted() {
    this.fetchWishlist();
  },
};

</script>
