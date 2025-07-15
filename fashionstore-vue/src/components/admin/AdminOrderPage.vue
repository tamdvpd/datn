<<<<<<< Updated upstream

<template>  <div class="p-4">
    <h2>📦 Quản lý đơn hàng</h2>
    <p>Hiển thị các đơn hàng và trạng thái xử lý.</p>
  </div>
  <div class="container py-4">
    <h2 class="mb-4">Tạo đơn hàng mới</h2>

    <!-- THÔNG TIN NGƯỜI MUA -->
    <div class="mb-3">
      <label>User ID</label>
      <input v-model="form.userId" type="number" class="form-control" required />
    </div>

    <div class="mb-3">
      <label>Shipping Provider ID</label>
      <input v-model="form.shippingProviderId" type="number" class="form-control" required />
    </div>

    <div class="mb-3">
      <label>Payment Method ID</label>
      <input v-model="form.paymentMethodId" type="number" class="form-control" required />
    </div>

    <!-- NGƯỜI NHẬN HÀNG -->
    <div class="row mb-3">
      <div class="col">
        <label>Receiver Name</label>
        <input v-model="form.receiverName" class="form-control" required />
      </div>
      <div class="col">
        <label>Receiver Phone</label>
        <input v-model="form.receiverPhone" class="form-control" required />
      </div>
    </div>

    <div class="mb-3">
      <label>Receiver Address</label>
      <input v-model="form.receiverAddress" class="form-control" required />
    </div>

    <div class="mb-3">
      <label>Note (nếu có)</label>
      <textarea v-model="form.note" class="form-control"></textarea>
    </div>

    <!-- SẢN PHẨM -->
    <h4 class="mt-4">Danh sách sản phẩm</h4>
    <div
      v-for="(item, index) in form.products"
      :key="index"
      class="row align-items-end mb-2"
    >
      <div class="col-4">
        <label>Product Detail ID</label>
        <input v-model="item.productDetailId" type="number" class="form-control" required />
      </div>
      <div class="col-3">
        <label>Số lượng</label>
        <input v-model="item.quantity" type="number" min="1" class="form-control" required />
      </div>
      <div class="col-auto">
        <button type="button" @click="removeProduct(index)" class="btn btn-danger">Xoá</button>
      </div>
    </div>
    <button type="button" @click="addProduct" class="btn btn-secondary mb-3">
      + Thêm sản phẩm
    </button>

    <!-- TÍNH TỔNG TIỀN -->
    <div class="mb-3">
      <label>Coupon Code (nếu có)</label>
      <input v-model="form.couponCode" class="form-control" />
    </div>

    <div class="alert alert-info">
      <strong>Tổng số sản phẩm:</strong> {{ totalQuantity }} <br />
      <strong>Tổng tiền ước tính:</strong> {{ formattedTotal }} VNĐ
    </div>

    <!-- SUBMIT -->
    <button class="btn btn-primary" @click="submitOrder">Tạo đơn hàng</button>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'OrderManager',
  data() {
    return {
      form: {
        userId: '',
        shippingProviderId: '',
        paymentMethodId: '',
        receiverName: '',
        receiverPhone: '',
        receiverAddress: '',
        note: '',
        couponCode: '',
        products: [
          { productDetailId: '', quantity: 1 }
        ]
      },
      estimatedTotal: 0
    };
  },
  computed: {
    totalQuantity() {
      return this.form.products.reduce((sum, p) => sum + Number(p.quantity || 0), 0);
    },
    formattedTotal() {
      return new Intl.NumberFormat('vi-VN').format(this.estimatedTotal);
    }
  },
  watch: {
    form: {
      handler() {
        this.estimateTotal();
      },
      deep: true
    }
  },
  methods: {
    addProduct() {
      this.form.products.push({ productDetailId: '', quantity: 1 });
    },
    removeProduct(index) {
      this.form.products.splice(index, 1);
    },
    async estimateTotal() {
      // Gọi API nội bộ nếu muốn lấy giá từ server
      let total = 0;
      for (const item of this.form.products) {
        if (item.productDetailId && item.quantity) {
          try {
            const { data } = await axios.get(`/api/product-details/${item.productDetailId}`);
            const price = data.discountPrice || data.price;
            total += price * item.quantity;
          } catch (e) {
            // lỗi khi lấy giá (sản phẩm không tồn tại, v.v.)
          }
        }
      }
      this.estimatedTotal = total;
    },
    async submitOrder() {
      try {
        const res = await axios.post('/api/orders/create', this.form);
        alert(`Tạo đơn hàng thành công! Mã đơn: ${res.data.id}`);
        // Reset form nếu muốn:
        this.form.products = [{ productDetailId: '', quantity: 1 }];
      } catch (err) {
        console.error(err);
        alert('Tạo đơn hàng thất bại.');
      }
    }
  }
};

</script>

<style scoped>
.container {
  max-width: 800px;
}
</style>
=======
<template>
  <div class="p-4">
    <h2>📦 Quản lý đơn hàng</h2>
    <p>Hiển thị các đơn hàng và trạng thái xử lý.</p>
  </div>
</template>
<script>
export default {
  name: 'OrderManager',
};
</script>
>>>>>>> Stashed changes
