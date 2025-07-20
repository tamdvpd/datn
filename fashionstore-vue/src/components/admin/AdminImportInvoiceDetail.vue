<template>
  <div class="max-w-6xl mx-auto bg-white shadow-lg rounded-2xl p-8 space-y-8">
    <h2 class="text-3xl font-bold text-blue-700">📥 Chi tiết Phiếu nhập #{{ importInvoiceId }}</h2>

    <!-- Thêm dòng chi tiết -->
    <div class="grid md:grid-cols-3 gap-4">
      <select v-model="newDetail.productDetailId" class="border p-2 rounded" required>
        <option disabled value="">-- Chọn biến thể sản phẩm --</option>
        <option v-for="pd in productDetails" :key="pd.id" :value="pd.id">
          {{ pd.product.name }} - {{ pd.color }} - {{ pd.size }}
        </option>
      </select>
      <input v-model.number="newDetail.quantity" type="number" min="1" placeholder="Số lượng" class="border p-2 rounded" />
      <input v-model.number="newDetail.unitPrice" type="number" min="0" placeholder="Đơn giá" class="border p-2 rounded" />
    </div>
    <button @click="addDetail" class="mt-3 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
      ➕ Thêm dòng
    </button>

    <!-- Bảng chi tiết -->
    <table class="w-full border mt-6 text-sm">
      <thead class="bg-gray-200">
        <tr>
          <th class="p-2 border">ID</th>
          <th class="p-2 border">Sản phẩm</th>
          <th class="p-2 border text-right">Số lượng</th>
          <th class="p-2 border text-right">Đơn giá</th>
          <th class="p-2 border text-right">Thành tiền</th>
          <th class="p-2 border text-center">Xoá</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="d in details" :key="d.id">
          <td class="p-2 border text-center">#{{ d.id }}</td>
          <td class="p-2 border">
            {{ d.productDetail?.product?.name }} - {{ d.productDetail?.color }} - {{ d.productDetail?.size }}
          </td>
          <td class="p-2 border text-right">{{ d.quantity }}</td>
          <td class="p-2 border text-right">{{ formatCurrency(d.unitPrice) }}</td>
          <td class="p-2 border text-right">{{ formatCurrency(d.quantity * d.unitPrice) }}</td>
          <td class="p-2 border text-center">
            <button @click="deleteDetail(d.id)" class="text-red-600 hover:text-red-800">🗑️</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Tổng tiền -->
    <div class="flex justify-end text-lg font-semibold text-right mt-4">
      Tổng tiền phiếu nhập: <span class="ml-2 text-green-700">{{ formatCurrency(totalAmount) }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute();
const importInvoiceId = route.params.id;

const details = ref([]);
const productDetails = ref([]);
const newDetail = ref({
  productDetailId: '',
  quantity: 1,
  unitPrice: 0
});

const fetchDetails = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/import-invoice-details/invoice/${importInvoiceId}`);
    details.value = res.data;
  } catch (err) {
    alert('❌ Không tải được dữ liệu chi tiết');
  }
};

const fetchProductDetails = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/product-details');
    productDetails.value = res.data;
  } catch (err) {
    alert('❌ Không tải được biến thể sản phẩm');
  }
};

const addDetail = async () => {
  if (!newDetail.value.productDetailId || newDetail.value.quantity <= 0 || newDetail.value.unitPrice < 0) {
    alert('❌ Số lượng phải > 0 và đơn giá không được âm');
    return;
  }

  try {
    const payload = {
      importInvoice: { id: importInvoiceId },
      productDetail: { id: newDetail.value.productDetailId },
      quantity: newDetail.value.quantity,
      unitPrice: newDetail.value.unitPrice
    };

    await axios.post('http://localhost:8080/api/import-invoice-details', payload);
    await fetchDetails();
    newDetail.value = { productDetailId: '', quantity: 1, unitPrice: 0 };
  } catch (err) {
    alert('❌ Thêm dòng chi tiết thất bại');
  }
};

const deleteDetail = async (id) => {
  if (!confirm('Bạn có chắc muốn xoá dòng này?')) return;

  try {
    await axios.delete(`http://localhost:8080/api/import-invoice-details/${id}`);
    await fetchDetails();
  } catch (err) {
    alert('❌ Xoá thất bại');
  }
};

const formatCurrency = (num) => {
  return Number(num).toLocaleString('vi-VN') + '₫';
};

const totalAmount = computed(() => {
  return details.value.reduce((sum, d) => sum + d.quantity * d.unitPrice, 0);
});

onMounted(() => {
  fetchDetails();
  fetchProductDetails();
});
</script>

<style scoped>
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type="number"] {
  -moz-appearance: textfield;
}
</style>
