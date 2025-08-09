<template>
  <div class="p-6 bg-white rounded-xl shadow-lg max-w-7xl mx-auto">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">Quản lý sản phẩm</h2>
      <button @click="toggleForm" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md font-semibold">
        {{ showForm ? 'Đóng' : 'Thêm mới' }}
      </button>
    </div>

    <!-- Form -->
    <div v-if="showForm" class="mb-10 bg-gray-50 p-6 rounded-xl shadow">
      <h3 class="text-xl font-semibold text-gray-800 mb-4">
        {{ form.id ? '✏️ Cập nhật sản phẩm' : '➕ Thêm sản phẩm mới' }}
      </h3>
      <form @submit.prevent="handleSubmit" class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input name="name" v-model="form.name" placeholder="Tên sản phẩm" required class="border p-3 rounded-lg" />
        <input name="description" v-model="form.description" placeholder="Mô tả" class="border p-3 rounded-lg" />
        <input name="brand" v-model="form.brand" placeholder="Thương hiệu" class="border p-3 rounded-lg" />

        <input ref="imageInput" name="image" type="file" @change="onFileChange" accept="image/*" class="border p-3 rounded-lg" />
        <img v-if="imagePreview" :src="imagePreview" alt="Xem trước ảnh" class="w-20 h-20 object-cover mt-2 rounded-md border" />

        <!-- Trạng thái -->
        <div class="flex items-center gap-2">
          <label class="text-gray-700 font-medium">Trạng thái:</label>
          <label><input name="status" type="radio" value="true" v-model="form.status" class="mr-1" /> Hiển thị</label>
          <label class="ml-4"><input name="status" type="radio" value="false" v-model="form.status" class="mr-1" /> Ẩn</label>
        </div>

        <!-- Danh mục -->
        <select v-model="form.categoryId" required class="border p-3 rounded-lg">
          <option disabled value="">-- Chọn danh mục --</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>

        <!-- Buttons -->
        <div class="md:col-span-2 flex gap-3 mt-2">
          <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"> {{ form.id ? 'Cập nhật' : 'Thêm' }} </button>
          <button type="button" @click="resetForm" class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded-lg"> Huỷ </button>
        </div>
      </form>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="min-w-full text-sm text-left border border-gray-200 rounded-lg shadow">
        <thead class="bg-blue-100 text-gray-700">
          <tr>
            <th class="px-4 py-2 border">ID</th>
            <th class="px-4 py-2 border">Tên</th>
            <th class="px-4 py-2 border">Mô tả</th>
            <th class="px-4 py-2 border">Thương hiệu</th>
            <th class="px-4 py-2 border">Ảnh</th>
            <th class="px-4 py-2 border">Trạng thái</th>
            <th class="px-4 py-2 border">Ngày tạo</th>
            <th class="px-4 py-2 border">Ngày cập nhật</th>
            <th class="px-4 py-2 border">Sửa</th>
            <th class="px-4 py-2 border">Xoá</th>
            <th class="px-4 py-2 border">Chi tiết</th>

          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in products" :key="prod.id" class="hover:bg-gray-50">
            <td class="px-4 py-2 border">{{ prod.id }}</td>
            <td class="px-4 py-2 border">{{ prod.name }}</td>
            <td class="px-4 py-2 border">{{ prod.description }}</td>
            <td class="px-4 py-2 border">{{ prod.brand }}</td>
            <td class="px-4 py-2 border text-center">
             <img
              v-if="prod.imageUrl"
              :src="getImageUrl(prod.imageUrl)"
              class="w-6 h-6 object-contain mx-auto rounded border"
              style="width: 48px; height: 48px;"
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
              <button @click="editProduct(prod)" class="text-yellow-600 hover:text-yellow-800 text-lg">✏️</button>
            </td>
            <td class="px-4 py-2 border text-center">
              <button @click="deleteProduct(prod.id)" class="text-red-600 hover:text-red-800 text-lg">🗑️</button>
            </td>
            <td class="px-4 py-2 border text-center">
              <button @click="toggleProductDetailPage(prod.id)" class="text-red-600 hover:text-red-800 text-lg">+</button>
            </td>
          </tr>
        </tbody>
      </table>
        <AdminProductDetailPage v-if="showDetailComponent" :productId="selectedProductId" />
    </div>
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
    fetchProducts() {
      fetch('http://localhost:8080/products')
        .then(res => res.json())
        .then(data => this.products = data);
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
    deleteProduct(id) {
      if (confirm('Bạn có chắc chắn muốn xoá sản phẩm này?')) {
        fetch(`http://localhost:8080/products/${id}`, { method: 'DELETE' })
          .then(() => this.fetchProducts());
      }
    },
    getImageUrl(path) {
      return path?.startsWith('http') ? path : `http://localhost:8080/images/products/${path}`;
    },
    formatDate(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString('vi-VN') : '';
    }
  },
  mounted() {
    this.fetchProducts();
    this.fetchCategories();
  }
};
</script>

<style scoped>

</style>