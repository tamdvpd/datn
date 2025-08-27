<template>
  <div class="container py-4">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center bg-white p-4 rounded shadow-sm mb-4">
      <h1 class="h4 fw-bold mb-0 d-flex align-items-center gap-2">
        🛍️ Quản lý Chi tiết Sản phẩm
      </h1>
      <span class="badge bg-primary fs-6">
        Product ID: {{ productId }}
      </span>
    </div>

    <!-- Form -->
    <div class="card shadow-sm mb-4">
      <div class="card-header bg-primary text-white fw-bold">
        {{ isEdit ? '✏️ Cập nhật chi tiết sản phẩm' : '➕ Thêm chi tiết sản phẩm mới' }}
      </div>
      <div class="card-body">
        <form @submit.prevent="handleSubmit" class="row g-3">
          <!-- Color / Size -->
          <div class="col-md-6">
            <label class="form-label">Màu sắc</label>
            <input type="text" class="form-control form-control-sm" v-model="form.color" placeholder="VD: Đỏ, Xanh">
          </div>
          <div class="col-md-6">
            <label class="form-label">Kích cỡ</label>
            <input type="text" class="form-control form-control-sm" v-model="form.size" placeholder="VD: M, L, XL...">
          </div>

          <!-- Quantity / Price -->
          <div class="col-md-6">
            <label class="form-label">Số lượng</label>
            <input type="number" min="0" class="form-control form-control-sm" v-model.number="form.quantity">
          </div>
          <div class="col-md-6">
            <label class="form-label">Giá gốc (VNĐ)</label>
            <input type="number" min="0" class="form-control form-control-sm" v-model.number="form.price">
          </div>

          <!-- Discount / Weight -->
          <div class="col-md-6">
            <label class="form-label">Giá giảm (VNĐ)</label>
            <input type="number" min="0" class="form-control form-control-sm" v-model.number="form.discountPrice">
          </div>
          <div class="col-md-6">
            <label class="form-label">Khối lượng (gram)</label>
            <input type="number" min="0" class="form-control form-control-sm" v-model.number="form.weight">
          </div>

          <!-- Image -->
          <div class="col-12">
            <label class="form-label">Ảnh sản phẩm</label>
            <input type="file" class="form-control form-control-sm" accept="image/*" @change="onFileChange" ref="imageUrl">
          </div>

          <!-- Preview -->
          <div v-if="form.imageUrl" class="col-12 text-center">
            <img :src="form.imageUrl" alt="Preview" class="img-thumbnail mt-2" style="max-width: 160px;">
          </div>

          <!-- Actions -->
          <div class="col-12 d-flex justify-content-end gap-2 mt-3">
            <button type="submit" class="btn btn-sm btn-primary">
              <i class="bi bi-check-circle me-1"></i>{{ isEdit ? 'Cập nhật' : 'Thêm mới' }}
            </button>
            <button v-if="isEdit" type="button" class="btn btn-sm btn-secondary" @click="resetForm">
              <i class="bi bi-x-circle me-1"></i>Hủy
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Table -->
    <div class="card shadow-sm">
      <div class="card-header bg-light fw-semibold">Danh sách chi tiết</div>
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-primary text-center">
            <tr>
              <th>Màu sắc</th>
              <th>Kích cỡ</th>
              <th>Số lượng</th>
              <th>Giá gốc</th>
              <th>Giảm giá</th>
              <th>Ảnh</th>
              <th>Khối lượng</th>
              <th>Tạo lúc</th>
              <th>Cập nhật</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="detail in productDetails" :key="detail.id">
              <td>{{ detail.color }}</td>
              <td class="text-uppercase">{{ detail.size }}</td>
              <td class="text-center">{{ formatNumber(detail.quantity) }}</td>
              <td class="text-success fw-semibold">{{ formatMoney(detail.price) }}</td>
              <td class="text-danger fw-semibold">{{ detail.discountPrice == null ? '—' : formatMoney(detail.discountPrice) }}</td>
              <td class="text-center">
                <img v-if="detail.imageUrl" :src="detail.imageUrl" class="img-thumbnail" style="max-width: 48px;">
              </td>
              <td class="text-center">{{ formatNumber(detail.weight) }} g</td>
              <td>{{ formatDateSafe(detail.createdAt) }}</td>
              <td>{{ formatDateSafe(detail.updatedAt) }}</td>
              <td class="text-center">
                <div class="btn-group btn-group-sm">
                  <button class="btn btn-outline-primary" @click="editDetail(detail)">
                    <i class="bi bi-pencil-square"></i>
                  </button>
                  <button class="btn btn-outline-danger" @click="deleteDetail(detail.id)">
                    <i class="bi bi-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!productDetails.length">
              <td colspan="10" class="text-center text-muted py-4">Chưa có chi tiết nào.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios';

const API_BASE = 'http://localhost:8080';

export default {
  name: 'AdminProductDetailPage',
  props: {
    productId: { type: Number, required: true }
  },
  data() {
    return {
      productDetails: [],
      isEdit: false,
      form: {
        id: null,
        productId: this.productId,
        color: '',
        size: '',
        quantity: 0,
        price: 0,
        discountPrice: 0,
        imageUrl: '',
        weight: 0,
      },
      imageFile: null,
    };
  },
  methods: {
    // -------- helpers an toàn null ----------
    toNum(v) {
      return (v == null || v === '' || Number.isNaN(Number(v))) ? 0 : Number(v);
    },
    formatNumber(v) {
      return this.toNum(v).toLocaleString('vi-VN');
    },
    formatMoney(v) {
      return this.toNum(v).toLocaleString('vi-VN') + '₫';
    },
    formatDateSafe(v) {
      if (!v) return '—';
      const d = new Date(v);
      return isNaN(d.getTime()) ? '—' : d.toLocaleString('vi-VN');
    },
    // ----------------------------------------

    async fetchProductDetails() {
      try {
        const { data } = await axios.get(`${API_BASE}/productdetails/product/${this.productId}`);
        // Chuẩn hoá để tránh null
        this.productDetails = (data || []).map(d => ({
          ...d,
          quantity: d.quantity ?? 0,
          price: d.price ?? 0,
          discountPrice: d.discountPrice ?? null,
          weight: d.weight ?? 0,
        }));
      } catch (error) {
        console.error('Lỗi khi load chi tiết sản phẩm:', error);
      }
    },

    async handleSubmit() {
      try {
        const isUpdate = this.form.id != null;
        const formData = new FormData();
        formData.append('color', this.form.color || '');
        formData.append('size', this.form.size || '');
        formData.append('quantity', this.toNum(this.form.quantity));
        formData.append('price', this.toNum(this.form.price));
        // Nếu muốn BE hiểu "không có discount" → gửi rỗng
        if (this.form.discountPrice == null || this.form.discountPrice === '') {
          formData.append('discountPrice', '');
        } else {
          formData.append('discountPrice', this.toNum(this.form.discountPrice));
        }
        formData.append('weight', this.toNum(this.form.weight));
        formData.append('productId', this.form.productId);

        if (this.imageFile) {
          formData.append('image', this.imageFile);
        }

        if (isUpdate) {
          await axios.put(`${API_BASE}/productdetails/${this.form.id}`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          });
        } else {
          await axios.post(`${API_BASE}/productdetails`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          });
        }

        await this.fetchProductDetails();
        this.resetForm();
      } catch (error) {
        console.error('Lỗi khi lưu chi tiết:', error);
      }
    },

    editDetail(detail) {
      this.isEdit = true;
      this.form = {
        id: detail.id,
        productId: detail.product?.id || this.productId,
        color: detail.color,
        size: detail.size,
        quantity: this.toNum(detail.quantity),
        price: this.toNum(detail.price),
        discountPrice: (detail.discountPrice == null ? 0 : this.toNum(detail.discountPrice)),
        weight: this.toNum(detail.weight),
        imageUrl: detail.imageUrl || ''
      };
      this.imageFile = null; // không gán ảnh cũ
    },

    async deleteDetail(id) {
      if (confirm('Bạn có chắc muốn xóa?')) {
        try {
          await axios.delete(`${API_BASE}/productdetails/${id}`);
          this.fetchProductDetails();
        } catch (error) {
          console.error('Lỗi khi xóa:', error);
        }
      }
    },

    resetForm() {
      this.isEdit = false;
      this.form = {
        id: null,
        productId: this.productId,
        color: '',
        size: '',
        quantity: 0,
        price: 0,
        discountPrice: 0,
        imageUrl: '',
        weight: 0,
      };
      this.imageFile = null;
      if (this.$refs.imageUrl) this.$refs.imageUrl.value = null;
    },

    onFileChange(e) {
      const file = e.target.files[0];
      if (file) {
        this.imageFile = file;
        this.form.imageUrl = URL.createObjectURL(file); // preview
      }
    }
  },
  watch: {
    productId: {
      immediate: true,
      handler() {
        this.fetchProductDetails();
        this.resetForm();
      }
    }
  }
};
</script>

<style scoped>
.input-style {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #d1d5db; /* gray-300 */
  border-radius: 8px;
  background-color: #f9fafb; /* gray-50 */
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}
.input-style:focus {
  border-color: #3b82f6; /* blue-500 */
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}
.file-input { @apply w-full py-2 px-4 rounded-lg border border-gray-300 cursor-pointer bg-gray-50 hover:bg-gray-100 transition; }
.btn-primary { @apply bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-2 rounded-lg shadow-md transition; }
.btn-secondary { @apply bg-gray-400 hover:bg-gray-500 text-white font-semibold px-6 py-2 rounded-lg shadow-md transition; }

/* Bảng đẹp hơn */
table th, table td { padding: 12px 8px; line-height: 1.5; }
table th { font-weight: 600; }
tr:hover { background-color: #f9fafb; }
</style>
