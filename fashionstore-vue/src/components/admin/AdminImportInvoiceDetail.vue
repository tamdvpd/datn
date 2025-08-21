<template>
  <div class="p-6 max-w-6xl mx-auto bg-white rounded-xl shadow-md mt-4">
    <h2 class="text-2xl font-bold mb-4 text-gray-800">
      📄 Chi tiết phiếu nhập: {{ invoiceId }}
    </h2>

    <!-- Form thêm/sửa chi tiết -->
    <div class="card shadow-sm border-0 mb-6">
      <div class="card-header bg-primary text-white fw-bold">
        {{ isEdit ? '✏️ Cập nhật chi tiết nhập' : '➕ Thêm chi tiết nhập mới' }}
      </div>

      <div class="card-body">
        <form @submit.prevent="handleSubmit" class="row g-3">
          <!-- Chọn sản phẩm -->
          <div class="col-md-4">
            <label class="form-label">Sản phẩm</label>
            <select
              v-model="selectedProductId"
              @change="showOption(selectedProductId)"
              class="form-select form-select-sm"
            >
              <option disabled value="">-- Chọn sản phẩm --</option>
              <option v-for="p in productOptions" :key="p.id" :value="p.id">
                {{ p.name }}
              </option>
            </select>
          </div>

          <!-- Chọn chi tiết (màu/kích cỡ) -->
          <div class="col-md-4">
            <label class="form-label">Màu sắc / Kích cỡ</label>
            <select v-model="form.productDetailId" class="form-select form-select-sm">
              <option disabled value="">-- Chọn option --</option>
              <option v-for="o in options" :key="o.id" :value="o.id">
                {{ o.color }} / {{ o.size }}
              </option>
            </select>
          </div>

          <!-- Số lượng -->
          <div class="col-md-2">
            <label class="form-label">Số lượng</label>
            <input
              type="number"
              v-model.number="form.quantity"
              min="1"
              class="form-control form-control-sm"
            />
          </div>

          <!-- Đơn giá -->
          <div class="col-md-2">
            <label class="form-label">Đơn giá (VNĐ)</label>
            <input
              type="number"
              v-model.number="form.unitPrice"
              min="1000"
              step="1000"
              class="form-control form-control-sm"
            />
          </div>

          <!-- Nút hành động -->
          <div class="col-12 d-flex justify-content-end gap-2 mt-3">
            <button class="btn btn-sm btn-primary" type="submit">
              <i class="bi bi-check-circle me-1"></i>
              {{ isEdit ? 'Cập nhật' : 'Thêm mới' }}
            </button>
            <button
              v-if="isEdit"
              type="button"
              class="btn btn-sm btn-outline-secondary"
              @click="resetForm"
            >
              Hủy
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Bảng danh sách -->
    <div class="overflow-x-auto bg-white shadow rounded-xl border border-gray-200">
      <table class="min-w-full divide-y divide-gray-200 text-sm">
        <thead class="bg-blue-50 text-blue-700 uppercase text-xs font-semibold">
          <tr>
            <th class="px-4 py-2 text-left w-40">Sản phẩm</th>
            <th class="px-4 py-2 text-center">Màu sắc</th>
            <th class="px-4 py-2 text-center">Kích cỡ</th>
            <th class="px-4 py-2 text-center">Số lượng</th>
            <th class="px-4 py-2 text-center">Đơn giá</th>
            <th class="px-4 py-2 text-center">Thành tiền</th>
            <th class="px-4 py-2 text-center">Thao tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 bg-white">
          <tr v-for="d in details" :key="d.id" class="hover:bg-gray-50">
            <td class="px-4 py-2">
              {{ d.productDetail?.product?.name || d.productDetail?.productName || d.productName || 'N/A' }}
            </td>
            <td class="px-4 py-2 text-center">{{ d.productDetail?.color }}</td>
            <td class="px-4 py-2 text-center">{{ d.productDetail?.size }}</td>
            <td class="px-4 py-2 text-center">{{ d.quantity }}</td>
            <td class="px-4 py-2 text-center">{{ formatCurrency(d.unitPrice) }}</td>
            <td class="px-4 py-2 text-center font-semibold text-green-600">
              {{ formatCurrency(d.quantity * d.unitPrice) }}
            </td>
            <td class="px-4 py-2 text-center">
              <button
                @click="editDetail(d)"
                class="btn btn-sm btn-outline-primary me-2"
                title="Sửa"
              >
                <i class="bi bi-pencil-square"></i>
              </button>
              <button
                @click="deleteDetail(d.id)"
                class="btn btn-sm btn-outline-danger"
                title="Xóa"
              >
                <i class="bi bi-trash3-fill"></i>
              </button>
            </td>
          </tr>
          <!-- Dòng tổng -->
          <tr class="bg-gray-50 font-semibold text-blue-800">
            <td colspan="5" class="px-4 py-2 text-right">Tổng cộng:</td>
            <td colspan="2" class="px-4 py-2 text-center">
              {{ formatCurrency(totalAmount) }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ImportInvoiceDetail",
  props: {
    invoiceId: { type: Number, required: true }
  },
  data() {
    return {
      details: [],
      productOptions: [],
      options: [],
      selectedProductId: "",
      isEdit: false,
      form: {
        id: null,
        productDetailId: "",
        quantity: 1,
        unitPrice: 0
      }
    };
  },
  computed: {
    totalAmount() {
      return this.details.reduce((sum, d) => sum + d.quantity * d.unitPrice, 0);
    }
  },
  methods: {
    async showOption(productId) {
      if (!productId) return;
      try {
        const res = await axios.get(
          `http://localhost:8080/productdetails/product/${productId}`
        );
        this.options = res.data;
      } catch (error) {
        console.error("Lỗi load option:", error);
      }
    },
    async fetchDetails() {
      try {
        const res = await axios.get(
          `http://localhost:8080/api/import-invoice-details/by-invoice/${this.invoiceId}`
        );
        console.log("Chi tiết phiếu nhập:", res.data); // debug
        this.details = res.data;
      } catch (err) {
        console.error("Lỗi load chi tiết phiếu nhập:", err);
      }
    },
    async fetchProductOptions() {
      try {
        const res = await axios.get("http://localhost:8080/products");
        this.productOptions = res.data;
      } catch (err) {
        console.error("Lỗi load sản phẩm:", err);
      }
    },
    async handleSubmit() {
      if (!this.form.productDetailId || !this.form.quantity || !this.form.unitPrice) {
        alert("⚠️ Vui lòng nhập đầy đủ thông tin!");
        return;
      }

      const user = JSON.parse(localStorage.getItem("user"));
      if (!user || !user.id) {
        alert("⚠️ Vui lòng đăng nhập!");
        this.$router.push("/login");
        return;
      }

      try {
        if (this.isEdit) {
          // cập nhật
          const updatedDetail = {
            id: this.form.id,
            importInvoice: { id: this.invoiceId },
            productDetail: { id: this.form.productDetailId },
            user: { id: user.id },
            quantity: this.form.quantity,
            unitPrice: this.form.unitPrice
          };
          await axios.put(
            `http://localhost:8080/api/import-invoice-details/${this.form.id}`,
            updatedDetail
          );
          alert("✅ Cập nhật chi tiết thành công!");
        } else {
          // thêm mới hoặc cộng dồn
          const existing = this.details.find(
            d => d.productDetail?.id === this.form.productDetailId
          );
          if (existing) {
            const updated = {
              ...existing,
              quantity: existing.quantity + this.form.quantity,
              unitPrice: this.form.unitPrice
            };
            await axios.put(
              `http://localhost:8080/api/import-invoice-details/${existing.id}`,
              updated
            );
            alert("✅ Đã cộng dồn số lượng sản phẩm!");
          } else {
            const newDetail = {
              importInvoice: { id: this.invoiceId },
              productDetail: { id: this.form.productDetailId },
              user: { id: user.id },
              quantity: this.form.quantity,
              unitPrice: this.form.unitPrice
            };
            await axios.post("http://localhost:8080/api/import-invoice-details", newDetail);
            alert("✅ Thêm chi tiết thành công!");
          }
        }
        await this.fetchDetails();
        this.resetForm();
      } catch (err) {
        console.error("Lỗi khi lưu chi tiết:", err);
        alert("❌ Lỗi khi lưu chi tiết!");
      }
    },
    editDetail(detail) {
      this.isEdit = true;
      this.selectedProductId = detail.productDetail?.product?.id || "";
      this.showOption(this.selectedProductId);
      this.form = {
        id: detail.id,
        productDetailId: detail.productDetail?.id,
        quantity: detail.quantity,
        unitPrice: detail.unitPrice
      };
    },
    async deleteDetail(id) {
      if (confirm("Bạn có chắc muốn xóa?")) {
        try {
          await axios.delete(`http://localhost:8080/api/import-invoice-details/${id}`);
          await this.fetchDetails();
          alert("🗑️ Xóa thành công!");
        } catch (err) {
          console.error("Lỗi khi xóa:", err);
          alert("❌ Xóa thất bại!");
        }
      }
    },
    resetForm() {
      this.isEdit = false;
      this.selectedProductId = "";
      this.options = [];
      this.form = {
        id: null,
        productDetailId: "",
        quantity: 1,
        unitPrice: 0
      };
    },
    formatCurrency(value) {
      return (value || 0).toLocaleString("vi-VN") + "₫";
    }
  },
  watch: {
    invoiceId: {
      immediate: true,
      handler() {
        this.fetchDetails();
        this.fetchProductOptions();
      }
    }
  }
};
</script>