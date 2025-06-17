<template>
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
      <h3 class="text-xl font-semibold text-gray-800 mb-4">{{ form.id ? '✏️ Cập nhật danh mục' : '➕ Thêm danh mục mới' }}</h3>
      <form @submit.prevent="handleSubmit" class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input v-model="form.name" placeholder="Tên danh mục" class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400" required />
        <input v-model="form.description" placeholder="Mô tả" class="border p-3 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400" required />
        <div class="md:col-span-2 flex gap-3 mt-2">
          <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-semibold">{{ form.id ? 'Cập nhật' : 'Thêm' }}</button>
          <button type="button" @click="resetForm" class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded-lg font-semibold">Huỷ</button>
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
      form: {
        id: null,
        name: '',
        description: ''
      }
    };
  },
  methods: {
    fetchCategories() {
      fetch('http://localhost:8080/api/categories')
        .then(res => res.json())
        .then(data => this.categories = data);
    },
    handleSubmit() {
      const method = this.form.id ? 'PUT' : 'POST';
      const url = this.form.id
        ? `http://localhost:8080/api/categories/${this.form.id}`
        : 'http://localhost:8080/api/categories';

      fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.form)
      })
      .then(() => {
        this.fetchCategories();
        this.resetForm();
        this.showForm = false;
      });
    },
    editCategory(category) {
      this.form = { ...category };
      this.showForm = true;
    },
    deleteCategory(id) {
      if (confirm('Bạn có chắc chắn muốn xoá danh mục này?')) {
        fetch(`http://localhost:8080/api/categories/${id}`, { method: 'DELETE' })
          .then(() => this.fetchCategories());
      }
    },
    resetForm() {
      this.form = { id: null, name: '', description: '' };
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

<style scoped>
button {
  background-color: #0ea5e9; /* sky-500 */
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0284c7; /* hover: sky-600 */
}

</style>
