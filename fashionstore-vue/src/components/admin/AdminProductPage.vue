<template>
  <div>
    <h2 class="text-center d-block">📦 Quản lý sản phẩm</h2>
    <div class="table-responsive">
      <!-- Form thêm/sửa sản phẩm -->
      <transition name="fade-slide">
        <div v-if="showForm" class="card shadow-sm mx-auto mb-4" style="max-width: 800px;">
          <div class="card-header bg-warning text-dark">
            <h5 class="mb-0">
              <i class="bi bi-pencil-square me-2"></i>
              {{ formProduct.id ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm' }}
            </h5>
          </div>
          <div class="card-body">
            <form @submit.prevent="submitForm" class="needs-validation">
              <div class="row g-3 mb-3">
                <div class="col-md-6">
                  <label class="form-label">Tên sản phẩm</label>
                  <input v-model="formProduct.name" type="text" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Thương hiệu</label>
                  <input v-model="formProduct.brand" type="text" class="form-control" />
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea v-model="formProduct.description" class="form-control"></textarea>
              </div>
              <div class="mb-3">
                <label class="form-label">Danh mục</label>
                <select v-model="formProduct.categoryId" class="form-select" required>
                  <option disabled value="">Chọn danh mục</option>
                  <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                    {{ cat.name }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label d-block">Trạng thái</label>
                <div class="form-check form-switch">
                  <input class="form-check-input" type="checkbox" id="statusSwitch" v-model="formProduct.status"
                    :true-value="true" :false-value="false" />
                  <label class="form-check-label" for="statusSwitch">
                    {{ formProduct.status ? 'Hiển thị' : 'Ẩn' }}
                  </label>
                </div>
              </div>
              <div class="text-end mt-4">
                <button type="submit" class="btn btn-warning me-2">
                  <i class="bi bi-save me-1"></i>{{ formProduct.id ? 'Cập nhật' : 'Thêm' }}
                </button>
                <button type="button" class="btn btn-secondary" @click="resetForm">
                  <i class="bi bi-x-circle me-1"></i>Hủy
                </button>
              </div>
            </form>
          </div>
        </div>
      </transition>

      <!-- Bảng danh sách sản phẩm -->
      <table class="table table-hover table-bordered text-center align-middle mx-auto">
        <thead class="bg-gray-200">
          <tr>
            <th>ID</th>
            <th>Tên</th>
            <th>Thương hiệu</th>
            <th>Danh mục</th>
            <th>Trạng thái</th>
            <th>Ngày cập nhật</th>
            <th>Sửa</th>
            <th>Ngừng bán</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.id }}</td>
            <td>{{ product.name }}</td>
            <td>{{ product.brand }}</td>
            <td>{{ product.category?.name }}</td>
            <td>
              <span :class="product.status ? 'badge bg-success' : 'badge bg-danger'">
                {{ product.status ? 'Hiển thị' : 'Ẩn' }}
              </span>
            </td>
            <td>{{ formatDateTime(product.updatedAt) }}</td>
            <td>
              <button class="btn btn-outline-warning" @click="showEdit(product)">
                <i class="bi bi-pencil-square"></i>
              </button>
            </td>
            <td>
              <button class="btn btn-outline-danger" @click="deactivateProduct(product.id)">
                <i class="bi bi-x-circle"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showNotification" class="notification" :class="notificationType">
      {{ notificationMessage }}
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ProductManager',
  data() {
    return {
      products: [],
      categories: [],
      formProduct: { id: null, name: '', brand: '', description: '', categoryId: '', status: true },
      showForm: false,
      showNotification: false,
      notificationMessage: '',
      notificationType: 'success'
    };
  },
  methods: {
    fetchProducts() {
      axios.get('http://localhost:8080/products')
        .then(response => { this.products = response.data; })
        .catch(() => this.showNotify('Không thể tải sản phẩm', 'error'));
    },
    fetchCategories() {
      axios.get('http://localhost:8080/api/categories')
        .then(response => { this.categories = response.data; })
        .catch(() => this.showNotify('Không thể tải danh mục', 'error'));
    },
    showEdit(product) {
      this.formProduct = {
        id: product.id,
        name: product.name,
        brand: product.brand,
        description: product.description,
        categoryId: product.category?.id || '',
        status: product.status
      };
      this.showForm = true;
    },
    submitForm() {
      const url = this.formProduct.id
        ? `http://localhost:8080/products/${this.formProduct.id}`
        : 'http://localhost:8080/products';
      const method = this.formProduct.id ? 'put' : 'post';

      axios[method](url, this.formProduct)
        .then(() => {
          this.showNotify(this.formProduct.id ? 'Cập nhật sản phẩm thành công!' : 'Thêm sản phẩm thành công!');
          this.fetchProducts();
          this.resetForm();
        })
        .catch(() => this.showNotify('Lỗi khi lưu sản phẩm!', 'error'));
    },
    deactivateProduct(id) {
      if (confirm('Bạn có chắc muốn ngừng bán sản phẩm này?')) {
        axios.delete(`http://localhost:8080/products/${id}`)
          .then(() => {
            this.showNotify('Đã ngừng bán sản phẩm!');
            this.fetchProducts();
          })
          .catch(() => this.showNotify('Không thể ngừng bán sản phẩm!', 'error'));
      }
    },
    resetForm() {
      this.formProduct = { id: null, name: '', brand: '', description: '', categoryId: '', status: true };
      this.showForm = false;
    },
    showNotify(message, type = 'success', duration = 3000) {
      this.notificationMessage = message;
      this.notificationType = type;
      this.showNotification = true;
      setTimeout(() => { this.showNotification = false; }, duration);
    },
    formatDateTime(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString('vi-VN') : 'N/A';
    }
  },
  mounted() {
    this.fetchProducts();
    this.fetchCategories();
  }
};
</script>

<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.5s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.fade-slide-enter-to,
.fade-slide-leave-from {
  opacity: 1;
  transform: translateY(0);
}

.notification {
  position: fixed;
  bottom: 20px;
  right: 20px;
  padding: 15px 25px;
  border-radius: 5px;
  color: white;
  z-index: 1000;
  animation: fadeInOut 3s ease forwards;
}

.notification.success {
  background-color: #4CAF50;
}

.notification.error {
  background-color: #f44336;
}

@keyframes fadeInOut {
  0% { opacity: 0; transform: translateY(20px); }
  10% { opacity: 1; transform: translateY(0); }
  90% { opacity: 1; transform: translateY(0); }
  100% { opacity: 0; transform: translateY(20px); }
}
</style>
