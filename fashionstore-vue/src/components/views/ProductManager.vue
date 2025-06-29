<template>
  <div class="container py-5">
    <h2 class="mb-4 text-primary">🛍️ Quản lý sản phẩm</h2>

    <!-- Form thêm mới -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">➕ Thêm sản phẩm mới</h5>
        <div class="row g-3">
          <div class="col-md-4">
            <img v-if="newProduct.imageUrl" :src="resolveImageUrl(newProduct.imageUrl)" class="img-thumbnail" />
          </div>
          <div class="col-md-8">
            <input v-model="newProduct.name" placeholder="📦 Tên sản phẩm" class="form-control mb-2" />
            <textarea v-model="newProduct.description" placeholder="📝 Mô tả" class="form-control mb-2"></textarea>
            <select v-model="newProduct.categoryId" class="form-control mb-2">
              <option disabled value="">📂 Chọn danh mục</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
            <input v-model="newProduct.imageUrl" placeholder="🖼 URL ảnh" class="form-control mb-2" />
            <div class="d-flex gap-2">
              <button @click="addProduct" class="btn btn-primary">💾 Thêm</button>
              <button @click="resetNewProduct" class="btn btn-secondary">❌ Huỷ</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div v-if="loading" class="text-muted">Đang tải sản phẩm...</div>
    <div v-else-if="products.length === 0" class="text-danger">Không có sản phẩm nào.</div>

    <div class="row g-4" v-else>
      <div class="col-md-6 col-xl-4" v-for="product in products" :key="product.id">
        <div class="card h-100">
          <img :src="resolveImageUrl(product.imageUrl)" class="card-img-top" style="height: 200px; object-fit: cover;" />
          <div class="card-body">
            <h5 class="card-title">{{ product.name }}</h5>
            <p class="card-text">{{ product.description }}</p>
            <p class="text-muted">📂 {{ product.category?.name }}</p>
            <p class="text-muted small">📅 {{ formatDate(product.createdAt) }}</p>
            <div class="d-flex gap-2">
              <button @click="startEdit(product)" class="btn btn-warning btn-sm">✏️ Sửa</button>
              <button @click="deleteProduct(product.id)" class="btn btn-danger btn-sm">🗑️ Xoá</button>
              <button @click="viewProduct(product.id)" class="btn btn-info btn-sm">🔍 Xem Chi Tiết</button> <!-- Nút xem chi tiết -->
            </div>
          </div>
        </div>
      </div>
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
      loading: true,
      editingId: null,
      editedProduct: null,
      newProduct: {
        name: '',
        description: '',
        imageUrl: '',
        categoryId: ''
      }
    };
  },
  methods: {
    async fetchProducts() {
      this.loading = true;
      try {
        const res = await axios.get('http://localhost:8080/products');
        this.products = res.data; // Gán dữ liệu
      } catch (err) {
        console.error('Lỗi khi tải sản phẩm:', err);
      } finally {
        this.loading = false;
      }
    },
    async fetchCategories() {
      try {
        const res = await axios.get('http://localhost:8080/categories');
        this.categories = res.data; // Gán danh sách danh mục
      } catch (err) {
        console.error('Lỗi khi tải danh mục:', err);
      }
    },
    resolveImageUrl(url) {
      return url && url.startsWith('http') ? url : `http://localhost:8080${url}`;
    },
    formatDate(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString('vi-VN') : '—';
    },
    async addProduct() {
      try {
        const payload = {
          name: this.newProduct.name,
          description: this.newProduct.description,
          imageUrl: this.newProduct.imageUrl,
          category: { id: parseInt(this.newProduct.categoryId) }
        };
        await axios.post('http://localhost:8080/products', payload);
        this.resetNewProduct();
        this.fetchProducts();
      } catch (err) {
        console.error('Lỗi khi thêm sản phẩm:', err);
        alert('Không thể thêm sản phẩm.');
      }
    },
    resetNewProduct() {
      this.newProduct = {
        name: '',
        description: '',
        imageUrl: '',
        categoryId: ''
      };
    },
    startEdit(product) {
      this.editingId = product.id;
      this.editedProduct = {
        id: product.id,
        name: product.name,
        description: product.description,
        imageUrl: product.imageUrl,
        categoryId: product.category?.id || ''
      };
    },
    cancelEdit() {
      this.editingId = null;
      this.editedProduct = null;
    },
    async saveProduct(id) {
      try {
        const payload = {
          id,
          name: this.editedProduct.name,
          description: this.editedProduct.description,
          imageUrl: this.editedProduct.imageUrl,
          category: { id: parseInt(this.editedProduct.categoryId) }
        };
        await axios.put(`http://localhost:8080/products/${id}`, payload);
        this.cancelEdit();
        this.fetchProducts();
      } catch (err) {
        console.error('Lỗi khi cập nhật:', err);
      }
    },
    async deleteProduct(id) {
      if (confirm('Bạn có chắc chắn muốn xoá sản phẩm này?')) {
        try {
          await axios.delete(`http://localhost:8080/products/${id}`);
          this.fetchProducts();
        } catch (err) {
          console.error('Lỗi khi xoá:', err);
        }
      }
    },
    viewProduct(id) {
      this.$router.push(`/product/${id}`); // Điều hướng đến trang chi tiết
    }
  },
  mounted() {
    this.fetchProducts();
    this.fetchCategories();
  }
};
</script>

<style scoped>
.card-img-top {
  border-bottom: 1px solid #ddd;
}
</style>