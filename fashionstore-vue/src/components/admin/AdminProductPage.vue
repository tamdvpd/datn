<template>
  <div class="container py-4">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
      <h2 class="fw-bold text-dark">📦 Quản lý sản phẩm</h2>
      <button @click="toggleForm" class="btn btn-primary">
        <i class="bi" :class="showForm ? 'bi-x-circle' : 'bi-plus-circle'"></i>
        {{ showForm ? 'Đóng' : 'Thêm mới' }}
      </button>
    </div>

    <!-- Form -->
    <div v-if="showForm" class="card shadow-sm mb-4">
      <div class="card-body">
        <h5 class="card-title mb-3">
          {{ form.id ? '✏️ Cập nhật sản phẩm' : '➕ Thêm sản phẩm mới' }}
        </h5>
        <form @submit.prevent="handleSubmit" class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Tên sản phẩm</label>
            <input v-model="form.name" type="text" class="form-control" required />
          </div>
          <div class="col-md-6">
            <label class="form-label">Thương hiệu</label>
            <input v-model="form.brand" type="text" class="form-control" />
          </div>
          <div class="col-md-12">
            <label class="form-label">Mô tả</label>
            <textarea v-model="form.description" class="form-control" rows="2"></textarea>
          </div>

          <div class="col-md-6">
            <label class="form-label">Ảnh sản phẩm</label>
            <input ref="imageInput" type="file" @change="onFileChange" accept="image/*" class="form-control" />
            <div v-if="imagePreview" class="mt-2">
              <img :src="imagePreview" alt="Xem trước ảnh" class="img-thumbnail" style="max-height: 200px" />
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label d-block">Trạng thái</label>
            <div class="form-check form-check-inline">
              <input v-model="form.status" class="form-check-input" type="radio" value="true" id="status1" />
              <label class="form-check-label" for="status1">Hiển thị</label>
            </div>
            <div class="form-check form-check-inline">
              <input v-model="form.status" class="form-check-input" type="radio" value="false" id="status2" />
              <label class="form-check-label" for="status2">Ẩn</label>
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label">Danh mục</label>
            <select v-model="form.categoryId" class="form-select" required>
              <option disabled value="">-- Chọn danh mục --</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>

          <div class="col-12 d-flex gap-2 mt-3">
            <button type="submit" class="btn btn-success">
              <i class="bi bi-check2-circle"></i> {{ form.id ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button type="button" @click="resetForm" class="btn btn-secondary">
              <i class="bi bi-arrow-counterclockwise"></i> Huỷ
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Table -->
    <div class="table-responsive">
      <table class="table table-bordered table-hover align-middle shadow-sm">
        <thead class="table-primary">
          <tr>
            <th>ID</th>
            <th>Tên</th>
            <th>Mô tả</th>
            <th>Thương hiệu</th>
            <th>Ảnh</th>
            <th>Trạng thái</th>
            <th>Ngày tạo</th>
            <th>Ngày cập nhật</th>
            <th colspan="3" class="text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in products" :key="prod.id">
            <td>{{ prod.id }}</td>
            <td>{{ prod.name }}</td>
            <td>{{ prod.description }}</td>
            <td>{{ prod.brand }}</td>
            <td class="text-center">
              <img v-if="prod.imageUrl" :src="getImageUrl(prod.imageUrl)" class="img-thumbnail" style="max-width: 60px" />
            </td>
            <td>
              <span :class="prod.status ? 'text-success fw-bold' : 'text-danger fw-bold'">
                {{ prod.status ? 'Hiển thị' : 'Ẩn' }}
              </span>
            </td>
            <td>{{ formatDate(prod.createdAt) }}</td>
            <td>{{ formatDate(prod.updatedAt) }}</td>
            <td class="text-center">
              <button @click="editProduct(prod)" class="btn btn-warning btn-sm"><i class="bi bi-pencil"></i></button>
            </td>
            <td class="text-center">
              <button @click="deleteProduct(prod.id)" class="btn btn-danger btn-sm"><i class="bi bi-trash"></i></button>
            </td>
            <td class="text-center">
              <button @click="toggleProductDetailPage(prod.id)" class="btn btn-info btn-sm"><i class="bi bi-plus-circle"></i></button>
            </td>
          </tr>
        </tbody>
      </table>
                  <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 0 }">
                    <a class="page-link" href="#"
                        @click.prevent="fetchProducts(currentPage - 1)">Previous</a>
                </li>
                <li v-for="page in visiblePages" :key="page" class="page-item"
                    :class="{ active: currentPage === page }">
                    <a class="page-link" href="#" @click.prevent="fetchProducts(page)">
                        {{ page + 1 }}
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages - 1 }">
                    <a class="page-link" href="#" @click.prevent="fetchProducts(currentPage + 1)">Next</a>
                </li>
            </ul>
        </nav>
    </div>

    <!-- Chi tiết sản phẩm -->
    <AdminProductDetailPage v-if="showDetailComponent" :productId="selectedProductId" />
  </div>
</template>


<script>
import AdminProductDetailPage from './AdminProductDetailPage.vue'
export default {
  components: {
    AdminProductDetailPage,
  },
  data() {
    return {
      products: [],
      categories: [],
      showForm: false,
      form: {
        id: null,
        name: '',
        description: '',
        brand: '',
        status: true,
        categoryId: ''
      },
      currentPage: 0,
      totalPages: 0,
      isUpdate: false,
      imageFile: null,
      imagePreview: null,
      showDetailComponent: false,
      selectedProductId: null,
    };
  },
  methods: {
    toggleProductDetailPage(productId) {
    this.selectedProductId = productId;
    this.showDetailComponent = true;
    },
    toggleForm() {
      this.showForm = !this.showForm;
    },
    fetchProducts(page) {
      const pageNumber = page !== undefined ? page : 0; 
      fetch(`http://localhost:8080/products/admin?page=${pageNumber}`)
        .then(res => res.json())
        .then(data => {
        this.products = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;});
    },
    fetchCategories() {
      fetch('http://localhost:8080/api/categories')
        .then(res => res.json())
        .then(data => this.categories = data);
    },
    onFileChange(e) {
      const file = e.target.files[0];
      if (file) {
        this.imageFile = file;
        this.imagePreview = URL.createObjectURL(file);
      }
    },
    
    // ✅ Đã fix lỗi Unexpected end of JSON input
    async handleSubmit() {
      try {
        const isUpdate = this.form.id != null;

        const formData = new FormData();
        formData.append('name', this.form.name);
        formData.append('description', this.form.description || '');
        formData.append('brand', this.form.brand || '');
        formData.append('status', this.form.status === 'true' || this.form.status === true);
        formData.append('categoryId', this.form.categoryId);
        if (this.imageFile) {
          formData.append('image', this.imageFile);
        }

        const url = isUpdate
          ? `http://localhost:8080/products/${this.form.id}`
          : 'http://localhost:8080/products';

        const method = isUpdate ? 'PUT' : 'POST';

        const response = await fetch(url, {
          method,
          body: formData,
        });

        // Xử lý lỗi backend nếu có
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || `Lỗi khi ${isUpdate ? 'cập nhật' : 'thêm mới'} sản phẩm`);
        }

        // Kiểm tra nếu có JSON trả về
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
          const result = await response.json();
          console.log("✅ Thành công:", result);
        } else {
          console.log("✅ Thành công (không có JSON trả về)");
        }

        await this.fetchProducts();
        this.resetForm();
        this.showForm = false;
      } catch (err) {
        console.error("❌ Lỗi:", err);
        alert(`❌ ${err.message}`);
      }
    },

    resetForm() {
      this.form = {
        id: null,
        name: '',
        description: '',
        brand: '',
        status: true,
        categoryId: '',
      };
      this.imageFile = null;
      this.imagePreview = null;
      if (this.$refs.imageInput) this.$refs.imageInput.value = null;
    },
    editProduct(prod) {
      this.isUpdate = true;
      this.form = {
        id: prod.id,
        name: prod.name,
        description: prod.description || '',
        brand: prod.brand || '',
        status: prod.status,
        categoryId: prod.category?.id || '',
      };
      this.imagePreview = prod.imageUrl ? this.getImageUrl(prod.imageUrl) : null;
      this.imageFile = null;
      this.showForm = true;
    },
    async deleteProduct(id) {
  if (confirm('Bạn có chắc chắn muốn xoá sản phẩm này?')) {
    try {
      const res = await fetch(`http://localhost:8080/products/${id}`, { method: 'DELETE' });
      if (!res.ok) {
        const errorText = await res.text();
        alert(`❌ Xoá thất bại: ${errorText || res.statusText}`);
        return;
      }
      alert('✅ Xoá sản phẩm thành công!');
      this.fetchProducts();
    } catch (err) {
      alert('❌ Có lỗi xảy ra khi xoá sản phẩm!');
      console.error(err);
    }
  }
},
    getImageUrl(path) {
      return path?.startsWith('http') ? path : `http://localhost:8080/images/products/${path}`;
    },
    formatDate(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString('vi-VN') : '';
    }
  },
      computed: {
        visiblePages() {
            const maxPagesToShow = 5;
            const pages = [];
            let start = Math.max(0, this.currentPage - Math.floor(maxPagesToShow / 2));
            let end = start + maxPagesToShow;

            if (end > this.totalPages) {
                end = this.totalPages;
                start = Math.max(0, end - maxPagesToShow);
            }

            for (let i = start; i < end; i++) {
                pages.push(i);
            }

            return pages;
        }
    },
  mounted() {
    this.fetchProducts();
    this.fetchCategories();
  }
};
</script>

<style scoped>
/* giữ nguyên style như bạn đang có */
</style>