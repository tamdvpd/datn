<template>
  <div class="p-6 bg-white rounded-xl shadow-lg max-w-7xl mx-auto">
    <!-- Tiêu đề và nút Thêm -->
    <div class="flex justify-between items-center mb-6 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">Quản lý sản phẩm</h2>
      <button
        @click="showForm = !showForm"
        class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md font-semibold"
      >
        {{ showForm ? 'Đóng' : 'Thêm mới' }}
      </button>
    </div>

    <!-- Form thêm/sửa sản phẩm -->
    <div v-if="showForm" class="mb-10 bg-gray-50 p-6 rounded-xl shadow">
      <h3 class="text-xl font-semibold text-gray-800 mb-4">
        {{ form.id ? '✏️ Cập nhật sản phẩm' : '➕ Thêm sản phẩm mới' }}
      </h3>
      <form @submit.prevent="handleSubmit" class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input
          v-model="form.name"
          placeholder="Tên sản phẩm"
          required
          class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <input
          v-model="form.description"
          placeholder="Mô tả"
          class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <input
          v-model="form.brand"
          placeholder="Thương hiệu"
          class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <input
          v-model.number="form.price"
          type="number"
          min="0"
          placeholder="Giá (≥0)"
          class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <!-- Nhập URL ảnh -->
        <input
          v-model="form.imageUrl"
          placeholder="URL ảnh (ví dụ /upload/images/products/1.png)"
          class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 md:col-span-2"
        />
<!-- Ảnh xem trước -->
    <img v-if="imagePreview" :src="imagePreview" alt="Xem trước ảnh"
      class="w-32 h-32 object-cover rounded-lg border mt-1" />

        <!-- Trạng thái -->
        <div class="flex items-center gap-2">
          <label class="text-gray-700 font-medium">Trạng thái:</label>
          <label class="inline-flex items-center">
            <input
              type="radio"
              value="true"
              v-model="form.status"
              class="mr-1"
            /> Hiển thị
          </label>
          <label class="inline-flex items-center ml-4">
            <input
              type="radio"
              value="false"
              v-model="form.status"
              class="mr-1"
            /> Ẩn
          </label>
        </div>

        <!-- Chọn danh mục -->
        <select
          v-model="form.categoryId"
          required
          class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        >
          <option disabled value="">-- Chọn danh mục --</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">
            {{ cat.name }}
          </option>
        </select>

        <!-- Nút hành động -->
        <div class="md:col-span-2 flex gap-3 mt-2">
          <button
            type="submit"
            class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-semibold"
          >
            {{ form.id ? 'Cập nhật' : 'Thêm' }}
          </button>
          <button
            type="button"
            @click="resetForm"
            class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded-lg font-semibold"
          >
            Huỷ
          </button>
        </div>
      </form>
    </div>

    <!-- Bảng sản phẩm -->
    <div class="overflow-x-auto">
      <table class="min-w-full text-sm text-left border border-gray-200 rounded-lg shadow">
        <thead class="bg-blue-100 text-gray-700">
          <tr>
            <th class="px-4 py-2 border">ID</th>
            <th class="px-4 py-2 border">Tên</th>
            <th class="px-4 py-2 border">Mô tả</th>
            <th class="px-4 py-2 border">Thương hiệu</th>
            <th class="px-4 py-2 border">Giá</th>
            <th class="px-4 py-2 border">Ảnh</th>
            <th class="px-4 py-2 border">Trạng thái</th>
            <th class="px-4 py-2 border">Ngày tạo</th>
            <th class="px-4 py-2 border">Ngày cập nhật</th>
            <th class="px-4 py-2 border">Sửa</th>
            <th class="px-4 py-2 border">Xoá</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in products" :key="prod.id" class="hover:bg-gray-50">
            <td class="px-4 py-2 border">{{ prod.id }}</td>
            <td class="px-4 py-2 border font-medium">{{ prod.name }}</td>
            <td class="px-4 py-2 border">{{ prod.description }}</td>
            <td class="px-4 py-2 border">{{ prod.brand }}</td>
            <td class="px-4 py-2 border">{{ prod.price }}</td>
            <td class="px-4 py-2 border">
              <img
                v-if="prod.imageUrl"
                :src="getImageUrl(prod.imageUrl)"
                class="w-12 h-12 object-cover rounded border"
              />
            </td>
            <td class="px-4 py-2 border">
              <span :class="prod.status ? 'text-green-600' : 'text-red-600'">
                {{ prod.status ? 'Hiển thị' : 'Ẩn' }}
              </span>
            </td>
            <td class="px-4 py-2 border">{{ formatDate(prod.createdAt) }}</td>
            <td class="px-4 py-2 border">{{ formatDate(prod.updatedAt) }}</td>
            <td class="px-4 py-2 border text-center">
              <button
                @click="editProduct(prod)"
                class="text-yellow-600 hover:text-yellow-800 text-lg"
              >
                ✏️
              </button>
            </td>
            <td class="px-4 py-2 border text-center">
              <button
                @click="deleteProduct(prod.id)"
                class="text-red-600 hover:text-red-800 text-lg"
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
      products: [],
      categories: [],
      showForm: false,
      form: {
        id: null,
        name: '',
        description: '',
        brand: '',
        price: '',
        imageUrl: '',
        status: true,
        categoryId: ''
      }
    };
  },
  methods: {
    fetchProducts() {
      fetch('http://localhost:8080/products')
        .then(res => res.json())
        .then(data => this.products = data)
        .catch(err => console.error("Fetch products error:", err));
    },
    fetchCategories() {
      fetch('http://localhost:8080/api/categories')
        .then(res => res.json())
        .then(data => this.categories = data)
        .catch(err => console.error("Fetch categories error:", err));
    },
    handleSubmit() {
      if (this.form.price < 0) {
        alert("Giá không được nhỏ hơn 0");
        return;
      }
      const isUpdate = this.form.id !== null;
      const url = isUpdate
        ? `http://localhost:8080/products/${this.form.id}`
        : 'http://localhost:8080/products';
      const method = isUpdate ? 'PUT' : 'POST';
      const payload = {
        name: this.form.name,
        description: this.form.description,
        brand: this.form.brand,
        price: this.form.price,
        status: this.form.status === "true" || this.form.status === true,
        imageUrl: this.form.imageUrl,
        category: {
          id: this.form.categoryId
        }
      };
      fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })
        .then(res => {
          if (!res.ok) throw new Error("Lỗi khi lưu sản phẩm.");
          return res.json();
        })
        .then(() => {
          this.fetchProducts();
          this.resetForm();
          this.showForm = false;
        })
        .catch(err => {
          console.error("Submit error:", err);
          alert(err.message);
        });
    },
    getImageUrl(path) {
      if (!path) return '';
      if (path.startsWith('http')) return path;
      return `http://localhost:8080${path}`;
    },
    editProduct(prod) {
      this.form = {
        id: prod.id,
        name: prod.name,
        description: prod.description || '',
        brand: prod.brand || '',
        price: prod.price || '',
        imageUrl: prod.imageUrl || '',
        status: prod.status,
        categoryId: prod.category?.id || ''
      };
      this.showForm = true;
    },
    deleteProduct(id) {
      if (confirm("Bạn có chắc chắn muốn xoá sản phẩm này?")) {
        fetch(`http://localhost:8080/products/${id}`, { method: 'DELETE' })
          .then(() => this.fetchProducts())
          .catch(err => console.error("Delete error:", err));
      }
    },
    resetForm() {
      this.form = {
        id: null,
        name: '',
        description: '',
        brand: '',
        price: '',
        imageUrl: '',
        status: true,
        categoryId: ''
      };
    },
    formatDate(str) {
      if (!str) return '';
      return new Date(str).toLocaleString('vi-VN');
    }
  },
  mounted() {
    this.fetchProducts();
    this.fetchCategories();
  }
};
</script>

<style scoped>
/* Tuỳ chỉnh thêm style nếu cần */
</style>
