<template>
  <div class="p-4">
    <!-- Tiêu đề và nút Thêm -->
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
      <h2 class="fw-bold">📂 Quản lý danh mục</h2>
      <button
        @click="toggleForm"
        class="btn btn-primary"
      >
        {{ showForm ? 'Đóng' : 'Thêm mới' }}
      </button>
    </div>

    <!-- Form thêm/sửa danh mục -->
    <transition name="fade-slide">
      <div v-if="showForm" class="card shadow-sm mx-auto mb-4" style="max-width: 800px;">
        <div class="card-header bg-info text-white">
          <h5 class="mb-0">
            <i class="bi bi-pencil-square me-2"></i>{{ form.id ? 'Cập nhật danh mục' : 'Thêm danh mục mới' }}
          </h5>
        </div>
        <div class="card-body">
          <form @submit.prevent="handleSubmit">
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Tên danh mục</label>
                <input
                  v-model="form.name"
                  type="text"
                  class="form-control"
                  placeholder="Tên danh mục"
                  required
                />
              </div>
              <div class="col-md-6">
                <label class="form-label">Mô tả</label>
                <input
                  v-model="form.description"
                  type="text"
                  class="form-control"
                  placeholder="Mô tả"
                />
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Ảnh</label>
              <input
                type="file"
                accept="image/*"
                @change="handleImageChange"
                class="form-control"
              />
              <div class="mt-2">
                <img
                  v-if="imagePreview"
                  :src="imagePreview"
                  class="img-thumbnail"
                  style="width: 100px; height: 100px; object-fit: cover;"
                />
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Trạng thái</label>
              <div>
                <div class="form-check form-check-inline">
                  <input
                    class="form-check-input"
                    type="radio"
                    value="true"
                    v-model="form.status"
                    id="statusShow"
                  />
                  <label class="form-check-label" for="statusShow">Hiển thị</label>
                </div>
                <div class="form-check form-check-inline">
                  <input
                    class="form-check-input"
                    type="radio"
                    value="false"
                    v-model="form.status"
                    id="statusHide"
                  />
                  <label class="form-check-label" for="statusHide">Ẩn</label>
                </div>
              </div>
            </div>
            <div class="text-end">
              <button type="submit" class="btn btn-success me-2">
                <i class="bi bi-check-circle me-1"></i>{{ form.id ? 'Cập nhật' : 'Thêm' }}
              </button>
              <button type="button" class="btn btn-secondary" @click="resetForm">
                <i class="bi bi-x-circle me-1"></i>Huỷ
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- Bảng danh mục -->
    <div class="table-responsive">
      <table class="table table-hover table-bordered text-center align-middle">
        <thead class="table-light">
          <tr>
            <th>ID</th>
            <th>Tên danh mục</th>
            <th>Mô tả</th>
            <th>Ảnh</th>
            <th>Trạng thái</th>
            <th>Ngày tạo</th>
            <th>Ngày cập nhật</th>
            <th>Sửa</th>
            <th>Xoá</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="category in categories" :key="category.id">
            <td>{{ category.id }}</td>
            <td class="fw-semibold">{{ category.name }}</td>
            <td>{{ category.description }}</td>
            <td>
              <img
                v-if="category.imageUrl"
                :src="`http://localhost:8080${category.imageUrl}`"
                class="img-thumbnail"
                style="width: 50px; height: 50px; object-fit: cover;"
              />
            </td>
            <td>
              <span :class="category.status ? 'badge bg-success' : 'badge bg-danger'">
                {{ category.status ? 'Hiển thị' : 'Ẩn' }}
              </span>
            </td>
            <td>{{ formatDate(category.createdAt) }}</td>
            <td>{{ formatDate(category.updatedAt) }}</td>
            <td>
              <button
                class="btn btn-sm btn-outline-warning"
                @click="editCategory(category)"
              >
                ✏️
              </button>
            </td>
            <td>
              <button
                class="btn btn-sm btn-outline-danger"
                @click="deleteCategory(category.id)"
              >
                🗑️
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      categories: [],
      showForm: false,
      imagePreview: null,
      form: {
        id: null,
        name: '',
        description: '',
        status: 'true',
        image: null,
        imageUrl: ''
      }
    };
  },
  methods: {
    fetchCategories() {
      fetch('http://localhost:8080/api/categories')
        .then(res => res.json())
        .then(data => (this.categories = data))
        .catch(err => console.error('Fetch categories error:', err));
    },
    handleImageChange(event) {
      const file = event.target.files[0];
      if (!file) return;
      this.form.image = file;
      const reader = new FileReader();
      reader.onload = e => (this.imagePreview = e.target.result);
      reader.readAsDataURL(file);
    },
    handleSubmit() {
      const isUpdate = this.form.id !== null;
      const url = isUpdate
        ? `http://localhost:8080/api/categories/${this.form.id}`
        : 'http://localhost:8080/api/categories';
      const method = isUpdate ? 'PUT' : 'POST';
      const formData = new FormData();
      formData.append('name', this.form.name);
      formData.append('description', this.form.description);
      formData.append('status', this.form.status === 'true');
      if (this.form.image) {
        formData.append('image', this.form.image);
      }
      fetch(url, { method, body: formData })
        .then(() => {
          this.fetchCategories();
          this.resetForm();
          this.showForm = false;
        })
        .catch(err => console.error('Submit error:', err));
    },
    editCategory(category) {
      this.form = {
        id: category.id,
        name: category.name,
        description: category.description,
        status: category.status ? 'true' : 'false',
        image: null,
        imageUrl: category.imageUrl
      };
      this.imagePreview = category.imageUrl
        ? `http://localhost:8080${category.imageUrl}`
        : null;
      this.showForm = true;
    },
    deleteCategory(id) {
      if (confirm('Bạn có chắc chắn muốn xoá danh mục này?')) {
        fetch(`http://localhost:8080/api/categories/${id}`, { method: 'DELETE' })
          .then(() => this.fetchCategories())
          .catch(err => console.error('Delete error:', err));
      }
    },
    resetForm() {
      this.form = { id: null, name: '', description: '', status: 'true', image: null, imageUrl: '' };
      this.imagePreview = null;
    },
    toggleForm() {
      this.showForm = !this.showForm;
      if (!this.showForm) this.resetForm();
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      return new Date(dateStr).toLocaleString('vi-VN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  },
  mounted() {
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
</style>
