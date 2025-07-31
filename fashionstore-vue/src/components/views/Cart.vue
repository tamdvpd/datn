<template>
  <div>
    <MainHeader />
    <div class="cart-page">
      <h1 class="cart-title">GIỎ HÀNG ({{ cart.length }} SẢN PHẨM)</h1>
      
      <!-- Loading state -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Đang tải...</span>
        </div>
        <p class="mt-3">Đang tải giỏ hàng...</p>
      </div>
      
      <div class="cart-box" v-else-if="cart.length">
        <table class="cart-table">
          <thead>
            <tr>
              <th>Sản Phẩm</th>
              <th>Phân Loại</th>
              <th>Đơn Giá</th>
              <th>Số Lượng</th>
              <th>Thành Tiền</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in cart" :key="item.id">
              <td class="product-cell">
                <img :src="getImageUrl(item.productDetail.product.imageUrl)"
                :alt="item.productDetail.product.name" class="product-image"
/>
                <div class="product-name">{{ item.productDetail.product.name }}</div>
              </td>
              <td>
                <div>Size: {{ item.productDetail.size }}</div>
                <div>Màu sắc: {{ item.productDetail.color }}</div>
              </td>
              <td class="price">
                {{ formatVND(item.productDetail.discountPrice || item.productDetail.price) }}
              </td>
              <td>
                <input type="number" min="1" v-model.number="item.quantity" @input="updateQuantity(item)" class="qty-input"/>
              </td>
              <td class="price">
                {{
                  formatVND(
                    (item.productDetail.discountPrice || item.productDetail.price) *
                      item.quantity
                  )
                }}
              </td>
              <td class="price">
                <button @click="removeItem(item.id)" class="delete-btn">
                  <i class="bi bi-trash3-fill"></i> Xoá
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="cart-actions">
          <a href="/" class="continue-shopping">Mua Thêm Sản Phẩm Khác</a>
          <div class="cart-summary">
            <p class="summary-row">Tiền hàng: {{ formatVND(total) }}</p>
            <p class="summary-total">Tổng cộng: <span>{{ formatVND(total) }}</span></p>
            <button class="checkout-btn">TIẾN HÀNH ĐẶT HÀNG</button>
          </div>
        </div>
      </div>
      <div v-else-if="!loading" class="empty-cart">
        <div class="text-center py-5">
          <i class="bi bi-cart-x" style="font-size: 4rem; color: #ccc;"></i>
          <h3 class="mt-3 text-muted">Giỏ hàng của bạn đang trống</h3>
          <p class="text-muted">Hãy thêm sản phẩm vào giỏ hàng để bắt đầu mua sắm!</p>
          <router-link to="/" class="btn btn-primary">
            <i class="bi bi-house"></i> Quay lại trang chủ
          </router-link>
        </div>
      </div>
    </div>
    <MainFooter />
  </div>
</template>

<script>
import axios from "axios";
import MainHeader from "@/components/MainHeader.vue";
import MainFooter from "@/components/MainFooter.vue";
import router from "@/router";

export default {
  name: "CartPage",
  components: {
    MainHeader,
    MainFooter,
  },
  data() {
    return {
      user: null,
      cart: [],
      total: 0,
      loading: false,
    };
  },
  methods: {
    getImageUrl(path) {
      return `http://localhost:8080/images/products/${path}`;
    },
    formatVND(val) {
      return val.toLocaleString("vi-VN", {
        style: "currency",
        currency: "VND",
      });
    },
    updateTotal() {
      this.total = this.cart.reduce((sum, item) => {
        const price = item.productDetail.discountPrice || item.productDetail.price;
        return sum + price * item.quantity;
      }, 0);
    },
    async fetchCart(userId) {
      this.loading = true;
      try {
        const response = await axios.get(`http://localhost:8080/api/cart/${userId}`);
        this.cart = response.data;
        this.updateTotal();
      } catch (error) {
        console.error("Lỗi khi lấy giỏ hàng:", error);
        this.cart = [];
      } finally {
        this.loading = false;
      }
    },
    async removeItem(cartId) {
  try {
    await axios.delete(`http://localhost:8080/api/cart/${cartId}`);
    this.cart = this.cart.filter(item => item.id !== cartId);
    this.updateTotal();
    // Thông báo cập nhật giỏ hàng
    localStorage.setItem('cartUpdated', Date.now().toString());
  } catch (error) {
    console.error("Lỗi khi xoá sản phẩm:", error);
    alert("Không thể xoá sản phẩm khỏi giỏ hàng.");
  }
},
async updateQuantity(item) {
  const quantity = item.quantity;

  if (quantity < 1) {
    alert("Số lượng phải lớn hơn 0");
    return;
  }

  try {
    await axios.put(`http://localhost:8080/api/cart/${item.id}`, {
      quantity: quantity
    });

    this.updateTotal(); // cập nhật tổng sau khi update thành công
    // Thông báo cập nhật giỏ hàng
    localStorage.setItem('cartUpdated', Date.now().toString());
  } catch (error) {
    console.error("Lỗi khi cập nhật số lượng:", error);
    alert("Không thể cập nhật số lượng sản phẩm.");
  }
},
  },
  async mounted() {
    const storedUser = localStorage.getItem("user");
    if (!storedUser) {
      router.push("/login");
      return;
    }

    this.user = JSON.parse(storedUser);

    // Nếu không có user.id thì cũng chuyển sang login
    if (!this.user.id) {
      router.push("/login");
      return;
    }

    await this.fetchCart(this.user.id);
  },
};
</script>

<style scoped>
.cart-page {
  background: #f9f9f9;
  padding: 2rem 1rem;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.cart-title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 24px;
  text-transform: uppercase;
  color: #333;
}

.cart-box {
  background: #ffffff;
  border-radius: 12px;
  padding: 1.5rem;
  width: 100%;
  max-width: 1100px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1.5rem;
}

.cart-table th {
  background-color: #007bff;
  color: #fff;
  padding: 14px;
  border: none;
  text-align: center;
  font-weight: 500;
  text-transform: uppercase;
}

.cart-table td {
  padding: 12px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #eee;
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-image {
  width: 80px;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.product-name {
  font-weight: 600;
  color: #333;
}

.price {
  color: #e53935;
  font-weight: 600;
}

.qty-input {
  width: 60px;
  padding: 6px;
  border-radius: 6px;
  border: 1px solid #ccc;
  text-align: center;
}

.qty-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.15);
}

.delete-btn {
  background-color: #ff4d4d;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-btn:hover {
  background-color: #d93636;
}

.cart-actions {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 1rem;
  padding-top: 1.5rem;
  border-top: 1px solid #ddd;
}

.continue-shopping {
  color: #007bff;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: color 0.3s;
}

.continue-shopping:hover {
  color: #0056b3;
}

.cart-summary {
  text-align: right;
  font-size: 15px;
}

.summary-row {
  margin-bottom: 6px;
}

.summary-total {
  font-size: 20px;
  font-weight: bold;
  color: #e53935;
  margin-bottom: 1rem;
}

.checkout-btn {
  background-color: #28a745;
  color: white;
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.checkout-btn:hover {
  background-color: #218838;
}

.empty-cart {
  text-align: center;
  margin-top: 3rem;
  font-size: 18px;
  color: #777;
}
</style>