<template>
  <div class="p-6 max-w-6xl mx-auto bg-white rounded-xl shadow">

    <!-- Header -->
    <div class="flex justify-between items-center mb-4 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">📦 Quản lý Phiếu nhập hàng</h2>
      <button @click="toggleForm" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        {{ showForm ? 'Đóng' : 'Tạo mới' }}
      </button>
    </div>

    <!-- Form Thêm/Sửa -->
    <div v-if="showForm" class="bg-gray-100 p-4 rounded mb-6">
      <h3 class="text-lg font-semibold mb-2">📋 Thông tin phiếu nhập</h3>
      <form @submit.prevent="handleSubmit" class="grid md:grid-cols-2 gap-4">
        <select v-model="form.supplierId" required class="border p-2 rounded">
          <option disabled value="">-- Chọn nhà cung cấp --</option>
          <option v-for="s in suppliers" :key="s.id" :value="s.id">{{ s.name }}</option>
        </select>

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

    <!-- Table Phiếu nhập -->
    <table class="w-full border text-sm">
      <thead class="bg-gray-200 text-left">
        <tr>
          <th class="p-2 border">ID</th>
          <th class="p-2 border">Nhà cung cấp</th>
          <th class="p-2 border">Ngày nhập</th>
          <th class="p-2 border">Ghi chú</th>
          <th class="p-2 border">Trạng thái</th>
          <th class="p-2 border text-center">Chi tiết</th>
          <th class="p-2 border text-center">Nhập kho</th>
          <th class="p-2 border text-center">Sửa</th>
          <th class="p-2 border text-center">Xoá</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="inv in importInvoices" :key="inv.id" class="hover:bg-gray-50">
          <td class="p-2 border text-center">{{ inv.id }}</td>
          <td class="p-2 border">{{ inv.supplier?.name || 'Không rõ' }}</td>
          <td class="p-2 border">{{ formatDate(inv.importDate) }}</td>
          <td class="p-2 border">{{ inv.note || '' }}</td>
          <td class="p-2 border text-center">
            <span :class="inv.status === 'COMPLETED' ? 'text-green-600 font-semibold' : 'text-red-600 font-semibold'">
              {{ inv.status === 'COMPLETED' ? 'Đã nhập' : 'Chưa nhập' }}
            </span>
          </td>
          <td class="p-2 border text-center">
            <button @click="showFormInvoiceDetail(inv.id)" class="text-blue-600 hover:text-blue-800 underline">
              🔍
            </button>
          </td>
          <td class="p-2 border text-center">
            <button
              @click="importStock(inv.id)"
              :disabled="inv.status === 'COMPLETED'"
              class="bg-green-600 text-white px-2 py-1 rounded hover:bg-green-700 disabled:opacity-50">
              📥
            </button>
          </td>
          <td class="p-2 border text-center">
            <button @click="editInvoice(inv)" :disabled="inv.status === 'COMPLETED'" class="text-yellow-600 hover:text-yellow-800">✏️</button>
          </td>
          <td class="p-2 border text-center">
            <button @click="deleteInvoice(inv.id)" :disabled="inv.status === 'COMPLETED'" class="text-red-600 hover:text-red-800">🗑️</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Tổng phiếu -->
    <div class="flex justify-end mt-4">
      <div class="text-right">
        <p><strong>Tổng phiếu:</strong> {{ totalInvoicesCount }}</p>
      </div>
    </div>

    <!-- Chi tiết phiếu nhập -->
    <ImportInvoiceDetail
      v-if="showInvoiceDetail"
      :invoiceId="selectedInvoiceId"
      @updated="handleCloseInvoiceDetail">
    </ImportInvoiceDetail>

    <!-- Toast -->
    <div v-if="toast.show" :class="`fixed bottom-4 right-4 px-4 py-2 rounded shadow ${toast.type === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white'}`">
      {{ toast.message }}
    </div>

  </div>
</template>

<script>
import ImportInvoiceDetail from './AdminImportInvoiceDetail.vue'

export default {
  components: { ImportInvoiceDetail },
  data() {
    return {
      importInvoices: [],
      suppliers: [],
      showForm: false,
      isEditing: false,
      form: { id: null, supplierId: '', note: '' },
      showInvoiceDetail: false,
      selectedInvoiceId: null,
      toast: { show: false, message: '', type: 'success' }
    }
  },
  computed: {
    totalInvoicesCount() { return this.importInvoices.length; }
  },
  methods: {
    // ----------------- API -----------------
    async fetchInvoices() {
      try {
        const res = await fetch('http://localhost:8080/api/import-invoices');
        this.importInvoices = await res.json();
      } catch (err) {
        this.showToast('❌ Lỗi khi tải phiếu nhập: ' + err.message, 'error');
      }
    },
    async fetchSuppliers() {
      try {
        const res = await fetch('http://localhost:8080/api/suppliers/active');
        this.suppliers = await res.json();
      } catch (err) {
        this.showToast('❌ Lỗi khi tải nhà cung cấp: ' + err.message, 'error');
      }
    },
    async handleSubmit() {
      const invoiceData = this.isEditing
        ? { supplier: { id: this.form.supplierId }, note: this.form.note }
        : { supplier: { id: this.form.supplierId }, note: this.form.note };

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
        this.showToast('✅ Lưu phiếu nhập thành công', 'success');
      } catch (err) {
        this.showToast('❌ ' + err.message, 'error');
      }
    },
    editInvoice(inv) {
      this.form = { id: inv.id, supplierId: inv.supplier?.id || '', note: inv.note || '' };
      this.isEditing = true;
      this.showForm = true;
    },
    async deleteInvoice(id) {
      if (!confirm('Bạn có chắc chắn xoá phiếu này?')) return;
      try {
        const res = await fetch(`http://localhost:8080/api/import-invoices/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Xoá thất bại');
        await this.fetchInvoices();
        this.showToast('✅ Xoá phiếu nhập thành công', 'success');
      } catch (err) {
        this.showToast('❌ ' + err.message, 'error');
      }
    },
    async importStock(id) {
      try {
        const res = await fetch(`http://localhost:8080/api/import-invoices/${id}/import-stock`, { method: 'POST' });
        if (!res.ok) throw new Error('Nhập kho thất bại');
        await this.fetchInvoices();
        this.showToast('✅ Nhập kho thành công', 'success');
      } catch (err) {
        this.showToast('❌ ' + err.message, 'error');
      }
    },
    // ----------------- Form & UI -----------------
    resetForm() {
      this.form = { id: null, supplierId: '', note: '' };
      this.isEditing = false;
    },
    toggleForm() {
      this.showForm = !this.showForm;
      if (!this.showForm) this.resetForm();
    },
    formatDate(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleDateString('vi-VN') : '';
    },
    showFormInvoiceDetail(id) {
      this.showInvoiceDetail = true;
      this.selectedInvoiceId = id;
    },
    handleCloseInvoiceDetail() {
      this.showInvoiceDetail = false;
      this.selectedInvoiceId = null;
      this.fetchInvoices();
    },
    showToast(message, type = 'success') {
      this.toast = { show: true, message, type };
      setTimeout(() => { this.toast.show = false; }, 3000);
    }
  },
  mounted() {
    this.fetchInvoices();
    this.fetchSuppliers();
  }
}
</script>

<style scoped>
/* Tuỳ chỉnh thêm nếu cần */
</style>
