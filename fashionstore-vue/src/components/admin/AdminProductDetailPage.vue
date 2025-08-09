<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-50 to-white py-12 px-6 md:px-12 space-y-12 font-sans">
    <!-- Header -->
    <div class="flex items-center justify-between bg-white p-6 rounded-2xl shadow-xl border border-gray-100">
      <h1 class="text-3xl font-extrabold text-gray-800 flex items-center space-x-3">
        <span>🛍️</span>
        <span>Quản lý Chi tiết Sản phẩm</span>
      </h1>
      <span class="text-blue-600 font-semibold text-lg bg-blue-100 px-4 py-2 rounded-full shadow">
        Product ID: {{ productId }}
      </span>
    </div>

<!-- Minimal Product Form -->
<div class="card shadow-sm border-0">
  <div class="card-header bg-primary text-white fw-bold">
    {{ isEdit ? 'Cập nhật chi tiết sản phẩm' : 'Thêm chi tiết sản phẩm mới' }}
  </div>

  <div class="card-body">
    <form @submit.prevent="handleSubmit" class="row g-3">

      <!-- Màu & Size -->
      <div class="col-md-6">
        <label class="form-label">Màu sắc</label>
        <input type="text" class="form-control form-control-sm" v-model="form.color" placeholder="VD: Đỏ, Xanh" />
      </div>
      <div class="col-md-6">
        <label class="form-label">Kích cỡ</label>
        <input type="text" class="form-control form-control-sm" v-model="form.size" placeholder="VD: M, L, XL,..." />
      </div>

      <!-- Số lượng & Giá gốc -->
      <div class="col-md-6">
        <label class="form-label">Số lượng</label>
        <input type="number" class="form-control form-control-sm" v-model="form.quantity" placeholder="VD: 10" />
      </div>
      <div class="col-md-6">
        <label class="form-label">Giá gốc (VNĐ)</label>
        <input type="number" class="form-control form-control-sm" v-model="form.price" placeholder="VD: 250000" />
      </div>

      <!-- Giá giảm & Trọng lượng -->
      <div class="col-md-6">
        <label class="form-label">Giá giảm (VNĐ)</label>
        <input type="number" class="form-control form-control-sm" v-model="form.discountPrice" placeholder="VD: 200000" />
      </div>
      <div class="col-md-6">
        <label class="form-label">Khối lượng (gram)</label>
        <input type="number" class="form-control form-control-sm" v-model="form.weight" placeholder="VD: 500" />
      </div>

      <!-- Ảnh -->
      <div class="col-12">
        <label class="form-label">Ảnh sản phẩm</label>
        <input
          type="file"
          class="form-control form-control-sm"
          accept="image/*"
          @change="onFileChange"
          ref="imageUrl"
        />
      </div>

      <!-- Preview ảnh -->
      <div v-if="form.imageUrl" class="col-12 text-center mt-2">
        <img :src="form.imageUrl" alt="Preview" class="img-thumbnail" style="max-width: 160px;">
      </div>

      <!-- Nút hành động -->
      <div class="col-12 d-flex justify-content-end gap-2 mt-3">
        <button type="submit" class="btn btn-sm btn-primary">
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


  <!-- Table Section -->
<div class="overflow-x-auto bg-white shadow-xl rounded-3xl border border-gray-200 max-w-7xl mx-auto">
  <table class="min-w-full divide-y divide-gray-200 text-sm">
    <thead class="bg-blue-50 text-blue-700 uppercase text-xs font-semibold sticky top-0 z-10">
      <tr>
        <th
          v-for="header in ['Màu sắc','Kích cỡ','Số lượng','Giá gốc','Giảm giá','Ảnh','Khối lượng','Tạo lúc','Cập nhật','Hành động']"
          :key="header"
          :class="['px-4 py-3 border-b border-gray-200 text-left', (header === 'ID' || header === 'Product ID') ? 'hidden' : '']"
          >
          {{ header }}
        </th>

      </tr>
    </thead>
    <tbody class="divide-y divide-gray-100 bg-white">
      <tr v-for="detail in productDetails" :key="detail.id" class="hover:bg-gray-50 transition-all duration-150">

        <td class="px-4 py-2 border-b border-gray-200 capitalize">{{ detail.color }}</td>
        <td class="px-4 py-2 border-b border-gray-200 uppercase">{{ detail.size }}</td>
        <td class="px-4 py-2 border-b border-gray-200 text-center">{{ detail.quantity }}</td>
        <td class="px-4 py-2 border-b border-gray-200 text-green-600 font-medium">{{ detail.price.toLocaleString() }}₫</td>
        <td class="px-4 py-2 border-b border-gray-200 text-red-500 font-medium">{{ detail.discountPrice.toLocaleString() }}₫</td>
        <td class="px-4 py-2 border-b border-gray-200 text-center">
          <img v-if="detail.imageUrl" :src="detail.imageUrl" class="w-6 h-6 object-cover rounded-lg border shadow-md mx-auto" style="width: 48px; height: 48px;"/>
        </td>
        <td class="px-4 py-2 border-b border-gray-200 text-center">{{ detail.weight }}g</td>
        <td class="px-4 py-2 border-b border-gray-200">{{ formatDate(detail.createdAt) }}</td>
        <td class="px-4 py-2 border-b border-gray-200">{{ formatDate(detail.updatedAt) }}</td>
        <td class="px-4 py-2 border-b border-gray-200 text-center">
          <div class="flex justify-center gap-2">
            <button @click="editDetail(detail)"
                    class="text-blue-600 hover:text-white hover:bg-blue-600 border border-blue-600 px-2.5 py-1 rounded-md text-xs transition flex items-center gap-1">
              <i class="bi bi-pencil-square"></i>
              <span>Sửa</span>
            </button>
            <button @click="deleteDetail(detail.id)"
                    class="text-red-600 hover:text-white hover:bg-red-600 border border-red-600 px-2.5 py-1 rounded-md text-xs transition flex items-center gap-1">
              <i class="bi bi-trash3-fill"></i>
              <span>Xóa</span>
            </button>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</div>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AdminProductDetailPage',
  props: {
  productId: {
    type: Number,
    required: true
  }
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
        quantity: '',
        price: '',
        discountPrice: null,
        imageUrl: '',
        weight: null,
      },
      imageFile: null,
    };
  },
  methods: {
  async fetchProductDetails() {
    try {
      const response = await axios.get(`http://localhost:8080/productdetails/product/${this.productId}`);
      console.log('Dữ liệu trả về:', response.data);
      this.productDetails = response.data;
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
    formData.append('quantity', this.form.quantity || '');
    formData.append('price', this.form.price || '');
    formData.append('discountPrice', this.form.discountPrice || '');
    formData.append('weight', this.form.weight || '');
    formData.append('productId', this.form.productId);

    if (this.imageFile) {
      formData.append('image', this.imageFile);
    }

    if (isUpdate) {
      await axios.put(`http://localhost:8080/productdetails/${this.form.id}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
    } else {
      await axios.post('http://localhost:8080/productdetails', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
    }

    await this.fetchProductDetails();

    //  Sau khi cập nhật hoặc thêm mới đều reset lại
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
      quantity: detail.quantity,
      price: detail.price,
      discountPrice: detail.discountPrice,
      weight: detail.weight,
      imageUrl: detail.imageUrl
    };
    this.imageFile = null; // Không gán ảnh cũ để buộc user chọn lại nếu cần
  },

  async deleteDetail(id) {
    if (confirm('Bạn có chắc muốn xóa?')) {
      try {
        await axios.delete(`http://localhost:8080/productdetails/${id}`);
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
      discountPrice: null,
      imageUrl: '',
      weight: null,
    };
    this.imageFile = null;
    if (this.$refs.imageUrl) {
      this.$refs.imageUrl.value = null;
    }
  },

  formatDate(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleString();
  },

  onFileChange(e) {
    const file = e.target.files[0];
    if (file) {
      this.imageFile = file;
      this.form.imageUrl = URL.createObjectURL(file); // Hiển thị preview
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
.file-input {
  @apply w-full py-2 px-4 rounded-lg border border-gray-300 cursor-pointer bg-gray-50 hover:bg-gray-100 transition;
}
.btn-primary {
  @apply bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-2 rounded-lg shadow-md transition;
}
.btn-secondary {
  @apply bg-gray-400 hover:bg-gray-500 text-white font-semibold px-6 py-2 rounded-lg shadow-md transition;
}
/* Bổ sung cho bảng đẹp hơn, tối ưu hơn */
table th, table td {
  padding: 12px 8px;
  line-height: 1.5;
}
table th {
  font-weight: 600;
}
/* Hovered row */
tr:hover {
  background-color: #f9fafb;
}
</style>