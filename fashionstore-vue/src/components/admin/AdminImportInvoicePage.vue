<template>
  <div class="p-6 max-w-6xl mx-auto bg-white rounded-xl shadow">
    <!-- Header -->
    <div class="flex justify-between items-center mb-4 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">📦 Quản lý Phiếu nhập hàng</h2>
      <button @click="toggleForm" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        {{ showForm ? 'Đóng' : 'Tạo mới' }}
      </button>
    </div>

    <!-- Form nhập phiếu -->
    <div v-if="showForm" class="bg-gray-100 p-4 rounded mb-6">
      <h3 class="text-lg font-semibold mb-2">📋 Thông tin phiếu nhập</h3>
      <form @submit.prevent="handleSubmit" class="grid md:grid-cols-2 gap-4">
        <input
          v-model.number="form.supplierId"
          type="number"
          min="1"
          placeholder="Nhà cung cấp"
          class="border p-2 rounded"
          required
        />
        <input
          v-model.number="form.totalAmount"
          type="number"
          step="0.01"
          min="0"
          placeholder="Tổng tiền (VNĐ)"
          class="border p-2 rounded"
          required
        />
        <input
          v-model="form.importDate"
          type="date"
          class="border p-2 rounded"
          required
        />
        <input
          v-model="form.note"
          placeholder="Ghi chú"
          class="border p-2 rounded"
        />
        <div class="md:col-span-2 flex gap-3 mt-2">
          <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
            {{ form.id ? 'Cập nhật' : 'Thêm mới' }}
          </button>
          <button type="button" @click="resetForm" class="bg-gray-300 text-black px-4 py-2 rounded">
            Huỷ
          </button>
        </div>
      </form>
    </div>

    <!-- Danh sách phiếu nhập -->
    <table class="w-full border text-sm">
      <thead class="bg-gray-200 text-left">
        <tr>
          <th class="p-2 border" style="width: 80px;">ID</th>
          <th class="p-2 border" style="width: 180px;">Nhà cung cấp</th>
          <th class="p-2 border" style="width: 150px;">Tổng tiền</th>
          <th class="p-2 border" style="width: 150px;">Ngày nhập</th>
          <th class="p-2 border" style="width: 150px;">Ghi chú</th>
          <th class="p-2 border text-center" style="width: 50px;">Sửa</th>
          <th class="p-2 border text-center" style="width: 50px;">Xoá</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="inv in importInvoices" :key="inv.id" class="hover:bg-gray-50">
          <td class="p-2 border text-center">{{ inv.id }}</td>
          <td class="p-2 border">{{ getSupplierName(inv.supplierId) }}</td>
          <td class="p-2 border font-semibold">{{ formatCurrency(inv.totalAmount) }}</td>
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

    <!-- Tổng cộng -->
    <div class="flex justify-end mt-4">
      <div class="text-right">
        <p><strong>Tổng hóa đơn:</strong> {{ totalInvoicesCount }} phiếu</p>
        <p><strong>Tổng tiền:</strong> {{ formatCurrency(totalAmount) }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      importInvoices: [
      ],
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
  computed: {
    totalAmount() {
      // tính tổng tất cả các phiếu
      return this.importInvoices.reduce((sum, inv) => sum + Number(inv.totalAmount), 0);
    },
    totalInvoicesCount() {
      return this.importInvoices.length;
    }
  },
  methods: {
    async fetchInvoices() {
      try {
        const res = await fetch('http://localhost:8080/api/import-invoices');
        this.importInvoices = await res.json();
      } catch (err) {
        alert('❌ Lỗi khi tải danh sách phiếu nhập: ' + err.message);
      }
    },
    handleSubmit() {
      // Logic thêm, cập nhật phiếu
    },
    editInvoice(inv) {
      this.form = { ...inv };
      this.showForm = true;
    },
    deleteInvoice(id) {
      // Xóa phiếu
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
    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.toLocaleDateString('vi-VN');
    },
    formatCurrency(amount) {
      return Number(amount).toLocaleString('vi-VN') + '₫';
    },
    getSupplierName(supplierId) {
      // chuyển mã nhà cung cấp thành tên
      const suppliers = {
        1: 'S.H.E Store',
        2: 'Cô Ba Lá'
        // Thêm danh sách nhà cung cấp nếu có
      };
      return suppliers[supplierId] || 'Nhà cung cấp khác';
    }
  },
  mounted() {
    this.fetchInvoices();
  }
};
</script>