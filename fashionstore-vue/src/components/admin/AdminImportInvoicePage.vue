<template>
    <div class="p-6 max-w-6xl mx-auto bg-white rounded-xl shadow">
      <div class="flex justify-between items-center mb-4 border-b pb-2">
        <h2 class="text-2xl font-bold text-gray-800">📦 Quản lý Phiếu nhập hàng</h2>
        <button @click="toggleForm" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          {{ showForm ? 'Đóng' : 'Tạo mới' }}
        </button>
      </div>
  
      <!-- Form tạo/cập nhật -->
      <div v-if="showForm" class="bg-gray-100 p-4 rounded mb-6">
        <h3 class="text-lg font-semibold mb-2">📋 Thông tin phiếu nhập</h3>
        <form @submit.prevent="handleSubmit" class="grid md:grid-cols-2 gap-4">
          <input v-model.number="form.supplierId" type="number" min =1 max=1000 placeholder="ID Nhà cung cấp" class="border p-2 rounded" required />
          <input v-model.number="form.totalAmount" type="number" step="0.01" placeholder="Tổng tiền" class="border p-2 rounded" required />
          <input v-model="form.importDate" type="date" class="border p-2 rounded" required />
          <input v-model="form.note" placeholder="Ghi chú" class="border p-2 rounded" />
          <div class="md:col-span-2 flex gap-3">
            <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
              {{ form.id ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button @click="resetForm" type="button" class="bg-gray-300 text-black px-4 py-2 rounded">Huỷ</button>
          </div>
        </form>
      </div>
  
      <!-- Danh sách -->
      <table class="w-full border text-sm">
        <thead class="bg-blue-100 text-left">
          <tr>
            <th class="p-2 border">ID</th>
            <th class="p-2 border">ID Nhà cung cấp</th>
            <th class="p-2 border">Tổng tiền</th>
            <th class="p-2 border">Ngày nhập</th>
            <th class="p-2 border">Ghi chú</th>
            <th class="p-2 border text-center">Sửa</th>
            <th class="p-2 border text-center">Xoá</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inv in importInvoices" :key="inv.id" class="hover:bg-gray-50">
            <td class="p-2 border">{{ inv.id }}</td>
            <td class="p-2 border">{{ inv.supplierId }}</td>
            <td class="p-2 border">{{ inv.totalAmount.toLocaleString() }} ₫</td>
            <td class="p-2 border">{{ formatDate(inv.importDate) }}</td>
            <td class="p-2 border">{{ inv.note }}</td>
            <td class="p-2 border text-center">
              <button @click="editInvoice(inv)" class="text-yellow-600 hover:text-yellow-800">✏️</button>
            </td>
            <td class="p-2 border text-center">
              <button @click="deleteInvoice(inv.id)" class="text-red-600 hover:text-red-800">🗑️</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        importInvoices: [],
        showForm: false,
        form: {
          id: null,
          supplierId: '',
          totalAmount: '',
          importDate: '',
          note: ''
        }
      };
    },
    methods: {
      fetchInvoices() {
        fetch('http://localhost:8080/api/import-invoices')
          .then(res => res.json())
          .then(data => this.importInvoices = data)
          .catch(err => console.error('Lỗi lấy phiếu nhập:', err));
      },
      handleSubmit() {
        const method = this.form.id ? 'PUT' : 'POST';
        const url = this.form.id
          ? `http://localhost:8080/api/import-invoices/${this.form.id}`
          : 'http://localhost:8080/api/import-invoices';
  
        fetch(url, {
          method,
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.form)
        })
          .then(res => {
            if (!res.ok) throw new Error('Lỗi gửi dữ liệu');
            return res.json();
          })
          .then(() => {
            this.fetchInvoices();
            this.resetForm();
            this.showForm = false;
          })
          .catch(err => alert('❌ Lỗi xử lý phiếu nhập: ' + err.message));
      },
      editInvoice(inv) {
        this.form = { ...inv };
        this.showForm = true;
      },
      deleteInvoice(id) {
        if (confirm("❗ Xác nhận xoá phiếu nhập?")) {
          fetch(`http://localhost:8080/api/import-invoices/${id}`, {
            method: 'DELETE'
          })
            .then(() => this.fetchInvoices())
            .catch(err => alert('❌ Không xoá được: ' + err.message));
        }
      },
      resetForm() {
        this.form = {
          id: null,
          supplierId: '',
          totalAmount: '',
          importDate: '',
          note: ''
        };
      },
      toggleForm() {
        this.showForm = !this.showForm;
        if (!this.showForm) this.resetForm();
      },
      formatDate(date) {
        if (!date) return '';
        return new Date(date).toLocaleDateString('vi-VN');
      }
    },
    mounted() {
      this.fetchInvoices();
    }
  };
  </script>
  