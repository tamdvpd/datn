<template>
    <div class="p-6 bg-white rounded-xl shadow-lg max-w-7xl mx-auto">
      <div class="flex justify-between items-center mb-6 border-b pb-2">
        <h2 class="text-2xl font-bold text-gray-800">🏭 Quản lý Nhà cung cấp</h2>
        <button @click="toggleForm" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md font-semibold">
          {{ showForm ? 'Đóng' : 'Thêm mới' }}
        </button>
      </div>
  
      <!-- Form thêm/sửa nhà cung cấp -->
      <div v-if="showForm" class="mb-10 bg-gray-50 p-6 rounded-xl shadow">
        <h3 class="text-xl font-semibold text-gray-800 mb-4">
          {{ form.id ? '✏️ Cập nhật nhà cung cấp' : '➕ Thêm nhà cung cấp mới' }}
        </h3>
        <form @submit.prevent="handleSubmit" class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <input v-model="form.name" placeholder="Tên nhà cung cấp" class="border p-3 rounded-lg" required />
          <input v-model="form.email" placeholder="Email" class="border p-3 rounded-lg" />
          <input v-model="form.phoneNumber" placeholder="Số điện thoại" class="border p-3 rounded-lg" />
          <input v-model="form.address" placeholder="Địa chỉ" class="border p-3 rounded-lg" />
          <div class="md:col-span-2 flex gap-3 mt-2">
            <button type="submit" class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg font-semibold">
              {{ form.id ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button type="button" @click="resetForm" class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-6 py-2 rounded-lg font-semibold">
              Huỷ
            </button>
          </div>
        </form>
      </div>
  
      <!-- Danh sách nhà cung cấp -->
      <div class="overflow-x-auto">
        <table class="min-w-full text-sm text-left border border-gray-200 rounded-lg shadow">
          <thead class="bg-blue-100 text-gray-700">
            <tr>
              <th class="px-4 py-2 border">ID</th>
              <th class="px-4 py-2 border">Tên</th>
              <th class="px-4 py-2 border">Email</th>
              <th class="px-4 py-2 border">SĐT</th>
              <th class="px-4 py-2 border">Địa chỉ</th>
              <th class="px-4 py-2 border">Trạng thái</th>
              <th class="px-4 py-2 border">Sửa</th>
              <th class="px-4 py-2 border">Xoá</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="sup in suppliers" :key="sup.id" class="hover:bg-gray-50">
              <td class="px-4 py-2 border">{{ sup.id }}</td>
              <td class="px-4 py-2 border font-medium">{{ sup.name }}</td>
              <td class="px-4 py-2 border">{{ sup.email }}</td>
              <td class="px-4 py-2 border">{{ sup.phoneNumber }}</td>
              <td class="px-4 py-2 border">{{ sup.address }}</td>
              <td class="px-4 py-2 border">
                <span :class="sup.status ? 'text-green-600' : 'text-red-600'">
                  {{ sup.status ? 'Hoạt động' : 'Ngừng' }}
                </span>
              </td>
              <td class="px-4 py-2 border text-center">
                <button @click="editSupplier(sup)" class="text-yellow-600 hover:text-yellow-800 text-lg">✏️</button>
              </td>
              <td class="px-4 py-2 border text-center">
                <button @click="deleteSupplier(sup.id)" class="text-red-600 hover:text-red-800 text-lg">🗑️</button>
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
        suppliers: [],
        showForm: false,
        form: {
          id: null,
          name: '',
          email: '',
          phoneNumber: '',
          address: '',
          status: true
        }
      };
    },
    methods: {
      fetchSuppliers() {
        fetch('http://localhost:8080/api/suppliers')
          .then(res => res.json())
          .then(data => this.suppliers = data)
          .catch(err => console.error('Lỗi tải nhà cung cấp:', err));
      },
      handleSubmit() {
        const method = this.form.id ? 'PUT' : 'POST';
        const url = this.form.id
          ? `http://localhost:8080/api/suppliers/${this.form.id}`
          : 'http://localhost:8080/api/suppliers';
  
        fetch(url, {
          method,
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.form)
        })
          .then(() => {
            this.fetchSuppliers();
            this.resetForm();
            this.showForm = false;
          });
      },
      editSupplier(supplier) {
        this.form = { ...supplier };
        this.showForm = true;
      },
      deleteSupplier(id) {
        if (confirm('Bạn có chắc muốn xoá nhà cung cấp này?')) {
          fetch(`http://localhost:8080/api/suppliers/${id}`, { method: 'DELETE' })
            .then(() => this.fetchSuppliers());
        }
      },
      resetForm() {
        this.form = {
          id: null,
          name: '',
          email: '',
          phoneNumber: '',
          address: '',
          status: true
        };
      },
      toggleForm() {
        this.showForm = !this.showForm;
        if (!this.showForm) this.resetForm();
      }
    },
    mounted() {
      this.fetchSuppliers();
    }
  };
  </script>
  