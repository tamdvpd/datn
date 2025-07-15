<template>
<<<<<<< Updated upstream
  <div class="p-6 bg-white rounded-xl shadow-lg max-w-7xl mx-auto">
    <!-- Tiêu đề và nút Thêm -->
    <div class="flex justify-between items-center mb-6 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">Quản lý danh mục</h2>
      <button @click="showForm = !showForm" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md font-semibold">
        {{ showForm ? 'Đóng' : 'Thêm mới' }}
      </button>
    </div>
 <!-- Form thêm/sửa danh mục -->
<div v-if="showForm" class="mb-10 bg-gray-50 p-6 rounded-xl shadow">
  <h3 class="text-xl font-semibold text-gray-800 mb-4">
    {{ form.id ? '✏️ Cập nhật danh mục' : '➕ Thêm danh mục mới' }}
  </h3>
  <form @submit.prevent="handleSubmit" class="grid grid-cols-1 md:grid-cols-2 gap-4">
    <input name="name" v-model="form.name" placeholder="Tên danh mục" required
      class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400" />

    <input name="description" v-model="form.description" placeholder="Mô tả"
      class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400" />

    <!-- Input chọn file ảnh -->
    <input name="image" type="file" accept="image/*" @change="handleImageChange"
      class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400" />

    <!-- Ảnh xem trước -->
    <img v-if="imagePreview" :src="imagePreview" alt="Xem trước ảnh"
      class="w-32 h-32 object-cover rounded-lg border mt-1" />

    <!-- Trạng thái -->
    <div class="flex items-center gap-2">
      <label class="text-gray-700 font-medium">Trạng thái:</label>
      <label class="inline-flex items-center">
        <input type="radio" name="status" value="true" v-model="form.status" class="mr-1" /> Hiển thị
      </label>
      <label class="inline-flex items-center ml-4">
        <input type="radio" name="status" value="false" v-model="form.status" class="mr-1" /> Ẩn
      </label>
    </div>

    <!-- Nút hành động -->
    <div class="md:col-span-2 flex gap-3 mt-2">
      <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-semibold">
        {{ form.id ? 'Cập nhật' : 'Thêm' }}
      </button>
      <button type="button" @click="resetForm"
        class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded-lg font-semibold">Huỷ</button>
    </div>
  </form>
</div>

    <!-- Bảng danh mục -->
    <div class="overflow-x-auto">
      <table class="min-w-full text-sm text-left border border-gray-200 rounded-lg shadow">
        <thead class="bg-blue-100 text-gray-700">
          <tr>
            <th class="px-4 py-2 border">ID</th>
            <th class="px-4 py-2 border">Tên danh mục</th>
            <th class="px-4 py-2 border">Mô tả</th>
            <th class="px-4 py-2 border">Ảnh</th>
            <th class="px-4 py-2 border">Trạng thái</th>
            <th class="px-4 py-2 border">Ngày tạo</th>
            <th class="px-4 py-2 border">Ngày cập nhật</th>
            <th class="px-4 py-2 border">Sửa</th>
            <th class="px-4 py-2 border">Xoá</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="category in categories" :key="category.id" class="hover:bg-gray-50">
            <td class="px-4 py-2 border">{{ category.id }}</td>
            <td class="px-4 py-2 border font-medium">{{ category.name }}</td>
            <td class="px-4 py-2 border">{{ category.description }}</td>
            <td class="px-4 py-2 border">
              <img v-if="category.imageUrl" :src="category.imageUrl" class="w-12 h-12 rounded object-cover" style="max-width: 48px; max-height: 48px"/>
            </td>
            <td class="px-4 py-2 border">
              <span :class="category.status ? 'text-green-600' : 'text-red-600'">
                {{ category.status ? 'Hiển thị' : 'Ẩn' }}
              </span>
            </td>
            <td class="px-4 py-2 border">{{ formatDate(category.createdAt) }}</td>
            <td class="px-4 py-2 border">{{ formatDate(category.updatedAt) }}</td>
            <td class="px-4 py-2 border text-center">
              <button @click="editCategory(category)" class="text-yellow-600 hover:text-yellow-800 text-lg">✏️</button>
            </td>
            <td class="px-4 py-2 border text-center">
              <button @click="deleteCategory(category.id)" class="text-red-600 hover:text-red-800 text-lg">🗑️</button>
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
        imageUrl: '',
        status: true
      }
    };
  },
  methods: {
    fetchCategories() {
      fetch('http://localhost:8080/api/categories')
        .then(res => res.json())
        .then(data => this.categories = data)
        .catch(err => console.error("Fetch categories error:", err));
    },
    handleImageChange(event) {
      const file = event.target.files[0];
      if (!file) return;

      this.form.image = file; // lưu tạm file ảnh

      const reader = new FileReader();
      reader.onload = e => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    handleSubmit() {
        const isUpdate = this.form.id !== null;
        const url = isUpdate
          ? `http://localhost:8080/api/categories/${this.form.id}`
          : 'http://localhost:8080/api/categories';
        const method = isUpdate ? 'PUT' : 'POST';
        const formData = new FormData();
        formData.append("name", this.form.name);
        formData.append("description", this.form.description);
        formData.append("status", this.form.status);
        if (this.form.image) {
          formData.append("image", this.form.image);
        }

        fetch(url, {
          method,
          body: formData
        })
          .then(() => {
            this.fetchCategories();
            this.resetForm();
            this.showForm = false;
          })
          .catch(err => console.error("Submit error:", err));
      },
    editCategory(category) {
      this.form = { ...category };
      this.imagePreview = category.imageUrl || null;
      this.showForm = true;
    },
    deleteCategory(id) {
      if (confirm('Bạn có chắc chắn muốn xoá danh mục này?')) {
        fetch(`http://localhost:8080/api/categories/${id}`, { method: 'DELETE' })
          .then(() => this.fetchCategories())
          .catch(err => console.error("Delete error:", err));
      }
    },
    resetForm() {
      this.form = {
        id: null,
        name: '',
        description: '',
        imageUrl: '',
        status: true,
        image: null,
        imageUrl: ''
      };
      this.imagePreview = null;
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      return new Date(dateStr).toLocaleString('vi-VN');
    }
  },
  mounted() {
    this.fetchCategories();
  }
};
</script>


=======
  <div class="p-4">
    <h2>📂 Quản lý danh mục</h2>
    <p>Quản lý danh mục sản phẩm tại đây.</p>
  </div>
</template>
<script>
export default {
  name: 'AdminCategoryPage',
};
</script>
>>>>>>> Stashed changes
