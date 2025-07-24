<template>
  <div class="container">
    <h2>➕ Nhập hàng</h2>
    <form @submit.prevent="submitImport">
      <div class="form-group">
        <label>ProductDetail ID:</label>
        <input v-model.number="newLog.productDetailId" type="number" required />
      </div>

      <div class="form-group">
        <label>Số lượng:</label>
        <input v-model.number="newLog.quantity" type="number" required min="1" />
      </div>

      <div class="form-group">
        <label>Ghi chú:</label>
        <input v-model="newLog.note" type="text" />
      </div>

      <button type="submit">Nhập hàng</button>
    </form>

    <hr />

    <h2>📋 Lịch sử nhập / xuất kho</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>ProductDetail ID</th>
          <th>Loại</th>
          <th>Số lượng</th>
          <th>Ngày</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="log in inventoryLogs" :key="log.id">
          <td>{{ log.id }}</td>
          <td>{{ log.productDetailId ?? '-' }}</td>
          <td>{{ log.action }}</td>
          <td>{{ log.quantity }}</td>
          <td>{{ formatDate(log.createdAt) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>


<script>
export default {
  name: 'InventoryManager',
  data() {
    return {
      inventoryLogs: [],
      newLog: {
        productDetailId: null,
        quantity: null,
      },
    };
  },
  mounted() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      try {
        const res = await fetch('http://localhost:8080/admin/inventoryLogs');
        if (!res.ok) throw new Error('Không thể tải log');
        this.inventoryLogs = await res.json();
      } catch (err) {
        console.error('Lỗi khi tải log:', err);
      }
    },
    async submitImport() {
      const params = new URLSearchParams();
      params.append('productDetailId', this.newLog.productDetailId);
      params.append('quantity', this.newLog.quantity);
      if (this.newLog.note) params.append('note', this.newLog.note);

      try {
        const res = await fetch('http://localhost:8080/admin/inventoryLogs/import', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: params,
        });

        if (!res.ok) throw new Error('Lỗi khi gửi phiếu nhập');
        alert('✅ Nhập hàng thành công!');
        this.resetForm();
        this.fetchLogs();
      } catch (err) {
        alert('❌ Lỗi nhập hàng: ' + err.message);
      }
    },
    resetForm() {
      this.newLog = {
        productDetailId: null,
        quantity: null,
        note: '',
      };
    },
    formatDate(dateStr) {
      const d = new Date(dateStr);
      return d.toLocaleDateString('vi-VN') + ' ' + d.toLocaleTimeString('vi-VN');
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: auto;
  padding: 20px;
}
form {
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 10px;
}
input {
  width: 100%;
  padding: 6px;
  margin-top: 4px;
}
button {
  padding: 8px 16px;
  background-color: #2d8cf0;
  color: white;
  border: none;
  cursor: pointer;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  padding: 8px;
  border: 1px solid #ccc;
}
</style>
