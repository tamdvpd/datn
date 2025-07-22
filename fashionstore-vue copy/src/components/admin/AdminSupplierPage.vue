<template>
  <div class="p-6 bg-white rounded-xl shadow-lg max-w-[1400px] w-full mx-auto">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">🏭 Quản lý Nhà cung cấp</h2>
      <button
        @click="toggleForm"
        class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md font-semibold"
      >
        {{ showForm ? 'Đóng' : 'Thêm mới' }}
      </button>
    </div>

    <!-- Form -->
    <div v-if="showForm" class="mb-10 bg-white p-8 rounded-xl shadow-md w-full max-w-none mx-auto">
      <h3 class="text-2xl font-semibold text-gray-800 mb-6">
        {{ form.id ? '✏️ Cập nhật nhà cung cấp' : '➕ Thêm nhà cung cấp mới' }}
      </h3>

      <form @submit.prevent="handleSubmit" class="space-y-5">
        <div class="flex items-start gap-4">
          <label class="w-48 pt-2 text-gray-700 font-medium">Tên nhà cung cấp</label>
          <div class="flex-1">
            <input
              v-model="form.name"
              placeholder="Nhập tên nhà cung cấp"
              :class="['w-full border rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400', errors.name ? 'border-red-500' : 'border-gray-300']"
              required
            />
            <p v-if="errors.name" class="text-red-600 text-sm mt-1">{{ errors.name }}</p>
          </div>
        </div>

        <div class="flex items-start gap-4">
  <label class="w-48 pt-2 text-gray-700 font-medium">Email</label>
  <div class="flex-1">
    <input
      v-model="form.email"
      placeholder="Nhập email"
      :class="[
        'w-full border rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400',
        errors.email ? 'border-red-500' : 'border-gray-300'
      ]"
    />
    
    <p v-if="errors.email" class="text-red-600 text-sm mt-1">{{ errors.email }}</p>
  </div>
</div>

        <div class="flex items-start gap-4">
          <label class="w-48 pt-2 text-gray-700 font-medium">Số điện thoại</label>
          <div class="flex-1">
            <input
              v-model="form.phoneNumber"
              placeholder="Nhập số điện thoại"
              :class="['w-full border rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400', errors.phoneNumber ? 'border-red-500' : 'border-gray-300']"
            />
            <p v-if="errors.phoneNumber" class="error-message">{{ errors.phoneNumber }}</p>
          </div>
        </div>

        <div class="flex items-start gap-4">
          <label class="w-48 pt-2 text-gray-700 font-medium">Địa chỉ</label>
          <div class="flex-1">
            <input
              v-model="form.address"
              placeholder="Nhập địa chỉ"
              class="w-full border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>
        </div>

        <div class="flex items-start gap-4">
          <label class="w-48 pt-2 text-gray-700 font-medium">Trạng thái</label>
          <div class="flex-1">
            <select
              v-model="form.status"
              class="w-full border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            >
              <option :value="true">Hoạt động</option>
              <option :value="false">Ngừng hoạt động</option>
            </select>
          </div>
        </div>

        <p v-if="errors.general" class="text-red-600 text-sm">{{ errors.general }}</p>

        <div class="pt-6 border-t mt-6">
          <div class="flex justify-between items-center w-full">
            <button
              type="button"
              @click="resetForm"
              class="bg-gray-400 hover:bg-gray-500 text-white px-6 py-2 rounded font-semibold"
            >
              Quay lại
            </button>
            <button
              type="submit"
              class="bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded font-semibold shadow-md"
            >
              {{ form.id ? 'Cập nhật' : 'Thêm' }}
            </button>
          </div>
        </div>
      </form>
    </div>

    <!-- Bảng dữ liệu -->
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

    <!-- Toast Notification -->
    <div v-if="showNotification" class="notification" :class="notificationType">
      {{ notificationMessage }}
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
      },
      errors: {},
      showNotification: false,
      notificationMessage: '',
      notificationType: 'success'
    };
  },
  methods: {
    showNotify(message, type = 'success', duration = 3000) {
      this.notificationMessage = message;
      this.notificationType = type;
      this.showNotification = true;
      setTimeout(() => {
        this.showNotification = false;
      }, duration);
    },
    fetchSuppliers() {
      fetch('http://localhost:8080/api/suppliers')
        .then(res => res.json())
        .then(data => this.suppliers = data)
        .catch(err => {
          console.error('Lỗi tải nhà cung cấp:', err);
          this.showNotify('Không thể tải danh sách nhà cung cấp!', 'error');
        });
    },
    async handleSubmit() {
      this.errors = {};
      const method = this.form.id ? 'PUT' : 'POST';
      const url = this.form.id
        ? `http://localhost:8080/api/suppliers/${this.form.id}`
        : `http://localhost:8080/api/suppliers`;

        try {
  const response = await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(this.form)
  });

  const text = await response.text();

  if (!response.ok) {
    try {
      const json = JSON.parse(text);
      this.errors = {
        email: json.email || '',
        phoneNumber: json.phoneNumber || '',
        name: json.name || '',
        address: json.address || '',
        general: json.general || json.error || ''
      };
    } catch {
      this.errors = { general: text || 'Đã xảy ra lỗi không xác định.' };
    }

    this.showNotify('Thêm hoặc cập nhật thất bại!', 'error');
    throw new Error(text);
  }

  this.fetchSuppliers();
  this.resetForm();
  this.showForm = false;
  this.showNotify(this.form.id ? 'Cập nhật thành công!' : 'Thêm mới thành công!');
} catch (err) {
  console.error('❌ Lỗi xử lý:', err.message);
}
    },
    editSupplier(supplier) {
      this.form = { ...supplier };
      this.showForm = true;
      this.errors = {};
    },
    deleteSupplier(id) {
      if (confirm('Bạn có chắc muốn xoá nhà cung cấp này?')) {
        fetch(`http://localhost:8080/api/suppliers/${id}`, { method: 'DELETE' })
          .then(() => {
            this.fetchSuppliers();
            this.showNotify('Xoá nhà cung cấp thành công!');
          })
          .catch(err => {
            console.error('Lỗi xoá nhà cung cấp:', err);
            this.showNotify('Xoá thất bại!', 'error');
          });
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
      this.errors = {};
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

<style scoped>
input:invalid {
  border-color: red;
}
input, select {
  min-width: 90%;
  max-width: 90%;
}
button {
  background-color: white !important;
  color: black !important;
}
.error-message {
  color: #dc2626;
  font-weight: bold;
  font-size: 0.95rem;
  margin-top: 0.25rem;
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
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  10% {
    opacity: 1;
    transform: translateY(0);
  }
  90% {
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(20px);
  }
}
</style>
