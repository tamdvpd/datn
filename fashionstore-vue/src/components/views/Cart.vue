<template>
  <div>
    <MainHeader />
    <div class="cart-page">
      <h1 class="cart-title">GIỎ HÀNG ({{ cart.length }} SẢN PHẨM)</h1>
      <div class="cart-box">
        <table class="cart-table">
          <thead>
            <tr>
              <th>Sản Phẩm</th>
              <th>Đơn Giá</th>
              <th>Số Lượng</th>
              <th>Thành Tiền</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in cart" :key="index">
              <td class="product-cell">
                <img :src="item.image" :alt="item.name" class="product-image" />
                <div class="product-name">{{ item.name }}</div>
              </td>
              <td class="price">{{ formatVND(item.price) }}</td>
              <td>
                <input
                  type="number"
                  min="1"
                  v-model.number="item.quantity"
                  @input="updateTotal"
                  class="qty-input"
                />
              </td>
              <td class="price">{{ formatVND(item.price * item.quantity) }}</td>
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
    </div>
    <MainFooter />
  </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue';
import MainFooter from '@/components/MainFooter.vue';

export default {
  name: "CartPage",
  components: {
    MainHeader,
    MainFooter
  },
  data() {
    return {
      cart: [
        {
          name: "Áo polo kẻ ngang bo tay kẻ GSTP040",
          price: 550000,
          quantity: 1,
          image: "https://cdn1.nhathuoclongchau.com.vn/unsafe/360x360/https://cms-prod.s3-sgn09.fptcloud.com/gstp040.jpg",
        },
        {
          name: "Áo polo kẻ ngang bo tay phối màu GSTP030",
          price: 320250,
          quantity: 1,
          image: "https://cdn1.nhathuoclongchau.com.vn/unsafe/360x360/https://cms-prod.s3-sgn09.fptcloud.com/gstp030.jpg",
        },
      ],
      total: 0,
    };
  },
  methods: {
    formatVND(val) {
      return val.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
    },
    updateTotal() {
      this.total = this.cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
    },
  },
  mounted() {
    this.updateTotal();
  },
};
</script>

<style scoped>
.cart-page {
  background: #f1f1f1;
  padding: 2rem 1rem;
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.cart-title {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
  text-transform: uppercase;
}

.cart-box {
  background: #fff;
  border: 1px solid #ccc;
  padding: 1rem;
  max-width: 1000px;
  width: 100%;
  box-shadow: 0 0 5px rgba(0,0,0,0.1);
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

.cart-table th {
  background-color: #cce5ff;
  color: #000;
  padding: 12px;
  border: 1px solid #ddd;
  text-align: center;
}

.cart-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: center;
}

.product-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.product-image {
  width: 100px;
  margin-bottom: 5px;
}

.product-name {
  font-weight: bold;
}

.price {
  color: red;
  font-weight: bold;
}

.qty-input {
  width: 60px;
  padding: 5px;
  font-size: 14px;
  text-align: center;
}

.cart-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  border-top: 1px solid #ddd;
  padding-top: 1rem;
}

.continue-shopping {
  color: #0074d9;
  text-decoration: underline;
  font-size: 14px;
}

.cart-summary {
  text-align: right;
  font-size: 15px;
}

.summary-row {
  margin-bottom: 4px;
}

.summary-total {
  font-size: 18px;
  font-weight: bold;
  color: red;
  margin-bottom: 1rem;
}

.checkout-btn {
  background-color: red;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 16px;
  font-weight: bold;
  border: none;
  cursor: pointer;
}

.checkout-btn:hover {
  background-color: darkred;
}
</style>