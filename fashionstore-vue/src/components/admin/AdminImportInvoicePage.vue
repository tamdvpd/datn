<template>
  <div class="p-6 max-w-6xl mx-auto bg-white rounded-xl shadow">
    <!-- Header -->
    <div class="flex justify-between items-center mb-4 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">📦 Quản lý Phiếu nhập hàng</h2>
      <button @click="toggleForm" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        {{ showForm ? 'Đóng' : 'Tạo mới' }}
      </button>
    </div>

    <!-- Form -->
    <div v-if="showForm" class="bg-gray-100 p-4 rounded mb-6">
      <h3 class="text-lg font-semibold mb-2">📋 Thông tin phiếu nhập</h3>
      <form @submit.prevent="handleSubmit" class="grid md:grid-cols-2 gap-4">
        <select v-model="form.supplierId" required class="border p-2 rounded">
          <option disabled value="">-- Chọn nhà cung cấp --</option>
          <option v-for="s in suppliers" :key="s.id" :value="s.id">{{ s.name }}</option>
        </select>
        <input
         v-model.number="form.totalAmount"
         type="number"
         min="100000"   
         inputmode="decimal"
         placeholder="Tổng tiền (VNĐ)"
         class="border p-2 rounded appearance-none"
         required
         @wheel.prevent
/>
        <p v-if="errors.totalAmount" class="text-red-600 text-sm mt-1">
          {{ errors.totalAmount }}
        </p>

        <input v-model="form.note" placeholder="Ghi chú" class="border p-2 rounded" />

        <div class="md:col-span-2 flex gap-3 mt-2">
          <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
            {{ isEditing ? 'Cập nhật' : 'Thêm mới' }}
          </button>
          <button type="button" @click="resetForm" class="bg-gray-300 text-black px-4 py-2 rounded">
            Huỷ
          </button>
        </div>
      </form>
    </div>

    <!-- Table -->
    <table class="w-full border text-sm">
      <thead class="bg-gray-200 text-left">
        <tr>
          <th class="p-2 border">ID</th>
          <th class="p-2 border">Nhà cung cấp</th>
          <th class="p-2 border">Tổng tiền</th>
          <th class="p-2 border">Ngày nhập</th>
          <th class="p-2 border">Ghi chú</th>
          <th class="p-2 border text-center">Sửa</th>
          <th class="p-2 border text-center">Xoá</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="inv in importInvoices" :key="inv.id" class="hover:bg-gray-50">
          <td class="p-2 border text-center">{{ inv.id }}</td>
          <td class="p-2 border">{{ inv.supplier?.name || 'Không rõ' }}</td>
          <td class="p-2 border font-semibold">{{ formatCurrency(inv.totalAmount) }}</td>
          <td class="p-2 border">{{ formatDate(inv.importDate) }}</td>
          <td class="p-2 border">{{ inv.note || '' }}</td>
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
      importInvoices: [],
      suppliers: [],
      showForm: false,
      isEditing: false,
      errors: {},
      form: {
        id: null,
        supplierId: '',
        totalAmount: '',
        note: ''
      }
    };
  },
  computed: {
    totalAmount() {
      return this.importInvoices.reduce((sum, inv) => sum + (Number(inv.totalAmount) || 0), 0);
    },
    totalInvoicesCount() {
      return this.importInvoices.length;
    }
  },
  methods: {
    async fetchInvoices() {
      try {
        const res = await fetch('http://localhost:8080/api/import-invoices');
        const data = await res.json();
        this.importInvoices = data;
      } catch (err) {
        alert('❌ Lỗi khi tải phiếu nhập: ' + err.message);
      }
    },
    async fetchSuppliers() {
      try {
        const res = await fetch('http://localhost:8080/api/suppliers');
        const data = await res.json();
        this.suppliers = data;
      } catch (err) {
        alert('❌ Lỗi khi tải nhà cung cấp: ' + err.message);
      }
    },
    async handleSubmit() {
  this.errors = {};
  const amount = parseFloat(this.form.totalAmount);
  if (amount < 100000) {
  this.errors.totalAmount = 'Tổng tiền phải lớn hơn hoặc bằng 100.000 VNĐ';
  return;
}

  const today = new Date().toISOString().split('T')[0];

  const invoiceData = {
    supplier: { id: this.form.supplierId },
    totalAmount: amount,
    importDate: today,
    note: this.form.note
  };

  try {
    const url = this.isEditing
      ? `http://localhost:8080/api/import-invoices/${this.form.id}`
      : 'http://localhost:8080/api/import-invoices';

    const res = await fetch(url, {
      method: this.isEditing ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(invoiceData)
    });

    if (!res.ok) throw new Error('Lưu phiếu nhập thất bại');

    await this.fetchInvoices();
    this.resetForm();
    this.showForm = false;
  } catch (err) {
    alert('❌ ' + err.message);
  }
},
    editInvoice(inv) {
      this.form = {
        id: inv.id,
        supplierId: inv.supplier?.id || '',
        totalAmount: inv.totalAmount,
        note: inv.note
      };
      this.isEditing = true;
      this.showForm = true;
    },
    async deleteInvoice(id) {
      if (!confirm('Bạn có chắc chắn xoá phiếu này?')) return;

      try {
        const res = await fetch(`http://localhost:8080/api/import-invoices/${id}`, {
          method: 'DELETE'
        });

        if (!res.ok) throw new Error('Xoá thất bại');

        await this.fetchInvoices();
      } catch (err) {
        alert('❌ ' + err.message);
      }
    },
    resetForm() {
      this.form = {
        id: null,
        supplierId: '',
        totalAmount: '',
        note: ''
      };
      this.errors = {};
      this.isEditing = false;
    },
    toggleForm() {
      this.showForm = !this.showForm;
      if (!this.showForm) {
        this.resetForm();
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.toLocaleDateString('vi-VN');
    },
    formatCurrency(amount) {
      const num = Number(amount);
      return isNaN(num) ? '0₫' : num.toLocaleString('vi-VN') + '₫';
    }
  },
  mounted() {
    this.fetchInvoices();
    this.fetchSuppliers();
  }
};
</script>

<style scoped>
/* Loại bỏ nút tăng giảm cho input number */
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="number"] {
  -moz-appearance: textfield;
}
</style>