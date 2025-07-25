<template>
  <div class="container">
    <!-- ======================= FORM NHẬP HÀNG ======================= -->
    <section class="form-section">
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
    </section>

    <hr />

    <!-- ======================= BẢNG KHO HÀNG ======================= -->
    <section class="stock-section">
      <h2>📦 Bảng kho hàng</h2>

      <!-- Filters (Dropdown + Input) -->
      <div class="filters">
        <input v-model="filters.product" placeholder="Tìm sản phẩm" />

        <select v-model="filters.color">
          <option value="">All Colors</option>
          <option v-for="color in colors" :key="color" :value="color">{{ color }}</option>
        </select>

        <select v-model="filters.size">
          <option value="">All Sizes</option>
          <option v-for="size in sizes" :key="size" :value="size">{{ size }}</option>
        </select>

        <!-- Stock Range Filter -->
        <select v-model="filters.stockRange">
          <option value="">All Stock</option>
          <option value="0-10">0 - 10</option>
          <option value="10-50">10 - 50</option>
          <option value="50-100">50 - 100</option>
          <option value="100-999999">> 100</option>
        </select>

        <select v-model="filters.priceRange">
          <option value="">All Prices</option>
          <option value="0-100000">0 - 100k</option>
          <option value="100000-500000">100k - 500k</option>
          <option value="500000-1000000">500k - 1M</option>
          <option value="1000000-999999999">> 1M</option>
        </select>

        <select v-model="filters.discountRange">
          <option value="">All Discount</option>
          <option value="0-10">0% - 10%</option>
          <option value="10-30">10% - 30%</option>
          <option value="30-50">30% - 50%</option>
          <option value="50-100">> 50%</option>
        </select>

        <button @click="applyFilters">Lọc</button>
        <button @click="resetFilters">Reset</button>
      </div>

      <table>
        <thead>
          <tr>
            <th>Product</th>
            <th>Color</th>
            <th>Size</th>
            <th>Stock</th>
            <th>Price</th>
            <th>Discount</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in stocks" :key="item.productDetailId">
            <td>{{ item.productName }}</td>
            <td>{{ item.color }}</td>
            <td>{{ item.size }}</td>
            <td :class="{ 'low-stock': item.currentStock <= 5 }">
              {{ item.currentStock }}
            </td>
            <td>{{ formatCurrency(item.price) }}</td>
            <td>{{ item.discountPrice ? formatCurrency(item.discountPrice) : '-' }}</td>
          </tr>
          <tr v-if="stocks.length === 0">
            <td colspan="6">Không có dữ liệu</td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination -->
      <div class="pagination">
        <button :disabled="currentPage === 0" @click="changePage(currentPage - 1)">
          Previous
        </button>
        <span>
          Page {{ currentPage + 1 }} / {{ totalPages }} ({{ totalItems }} items)
        </span>
        <button
          :disabled="currentPage >= totalPages - 1"
          @click="changePage(currentPage + 1)"
        >
          Next
        </button>

        <select v-model.number="sizePage" @change="fetchWarehouseStock(0)">
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
          <option :value="50">50</option>
        </select>
      </div>
    </section>

    <hr />

    <!-- ======================= LỊCH SỬ NHẬP XUẤT ======================= -->
    <section class="log-section">
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
            <td :class="{ export: log.quantity < 0, import: log.quantity > 0 }">
              {{ log.quantity }}
            </td>
            <td>{{ formatDate(log.createdAt) }}</td>
          </tr>
          <tr v-if="inventoryLogs.length === 0">
            <td colspan="5">Không có log</td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script>
export default {
  name: "InventoryWarehouse",
  data() {
    return {
      stocks: [],
      currentPage: 0,
      totalPages: 0,
      totalItems: 0,
      sizePage: 10,

      filters: {
        product: "",
        color: "",
        size: "",
        stockRange: "",
        priceRange: "",
        discountRange: "",
        stockMin: null,
        stockMax: null,
        priceMin: null,
        priceMax: null,
        discountMin: null,
        discountMax: null,
      },

      colors: [],
      sizes: [],

      inventoryLogs: [],
      newLog: {
        productDetailId: null,
        quantity: null,
        note: "",
      },
    };
  },
  mounted() {
    this.fetchWarehouseStock(0);
    this.fetchLogs();
    this.fetchFilters();
  },
  methods: {
    async fetchFilters() {
      try {
        const res = await fetch('http://localhost:8080/admin/inventoryLogs/warehouse/filters');
        if (!res.ok) throw new Error('Không thể tải filters');
        const data = await res.json();
        this.colors = data.colors || [];
        this.sizes = data.sizes || [];
      } catch (err) {
        console.error('Lỗi khi tải filters:', err);
      }
    },
    async fetchWarehouseStock(page = 0) {
      try {
        const query = new URLSearchParams({
          page,
          sizePage: this.sizePage,
          product: this.filters.product,
          color: this.filters.color,
          size: this.filters.size,
        });

        if (this.filters.stockMin !== null) query.append("stockMin", this.filters.stockMin);
        if (this.filters.stockMax !== null) query.append("stockMax", this.filters.stockMax);
        if (this.filters.priceMin !== null) query.append("priceMin", this.filters.priceMin);
        if (this.filters.priceMax !== null) query.append("priceMax", this.filters.priceMax);
        if (this.filters.discountMin !== null) query.append("discountMin", this.filters.discountMin);
        if (this.filters.discountMax !== null) query.append("discountMax", this.filters.discountMax);

        const res = await fetch(
          `http://localhost:8080/admin/inventoryLogs/warehouse?${query.toString()}`
        );
        if (!res.ok) throw new Error("Không thể tải dữ liệu kho hàng");

        const data = await res.json();
        this.stocks = data.data || [];
        this.currentPage = data.currentPage || 0;
        this.totalItems = data.totalItems || 0;
        this.totalPages = data.totalPages || 0;
      } catch (err) {
        console.error(err);
        alert("❌ Lỗi tải kho hàng: " + err.message);
      }
    },
    applyFilters() {
      if (this.filters.priceRange) {
        const [min, max] = this.filters.priceRange.split("-");
        this.filters.priceMin = Number(min);
        this.filters.priceMax = Number(max);
      } else {
        this.filters.priceMin = null;
        this.filters.priceMax = null;
      }

      if (this.filters.discountRange) {
        const [min, max] = this.filters.discountRange.split("-");
        this.filters.discountMin = Number(min);
        this.filters.discountMax = Number(max);
      } else {
        this.filters.discountMin = null;
        this.filters.discountMax = null;
      }

      if (this.filters.stockRange) {
        const [min, max] = this.filters.stockRange.split("-");
        this.filters.stockMin = Number(min);
        this.filters.stockMax = Number(max);
      } else {
        this.filters.stockMin = null;
        this.filters.stockMax = null;
      }

      this.fetchWarehouseStock(0);
    },
    changePage(page) {
      if (page < 0 || page >= this.totalPages) return;
      this.fetchWarehouseStock(page);
    },
    resetFilters() {
      this.filters = {
        product: "",
        color: "",
        size: "",
        stockRange: "",
        priceRange: "",
        discountRange: "",
        stockMin: null,
        stockMax: null,
        priceMin: null,
        priceMax: null,
        discountMin: null,
        discountMax: null,
      };
      this.fetchWarehouseStock(0);
    },
    async fetchLogs() {
      try {
        const res = await fetch("http://localhost:8080/admin/inventoryLogs");
        if (!res.ok) throw new Error("Không thể tải log");
        this.inventoryLogs = await res.json();
      } catch (err) {
        console.error("Lỗi khi tải log:", err);
      }
    },
    async submitImport() {
      const params = new URLSearchParams();
      params.append("productDetailId", this.newLog.productDetailId);
      params.append("quantity", this.newLog.quantity);
      if (this.newLog.note) params.append("note", this.newLog.note);

      try {
        const res = await fetch(
          "http://localhost:8080/admin/inventoryLogs/import",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            body: params,
          }
        );

        if (!res.ok) throw new Error("Lỗi khi gửi phiếu nhập");
        alert("✅ Nhập hàng thành công!");
        this.resetForm();
        this.fetchLogs();
        this.fetchWarehouseStock(this.currentPage);
      } catch (err) {
        alert("❌ Lỗi nhập hàng: " + err.message);
      }
    },
    resetForm() {
      this.newLog = {
        productDetailId: null,
        quantity: null,
        note: "",
      };
    },
    formatDate(dateStr) {
      const d = new Date(dateStr);
      return d.toLocaleDateString("vi-VN") + " " + d.toLocaleTimeString("vi-VN");
    },
    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value ?? 0);
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1100px;
  margin: auto;
  padding: 20px;
}
section {
  margin-bottom: 20px;
}
form {
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 10px;
}
input, select {
  width: 100%;
  padding: 6px;
  margin-top: 4px;
}
.filters {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
  margin-bottom: 12px;
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
  margin-top: 10px;
}
th,
td {
  padding: 8px;
  border: 1px solid #ccc;
  text-align: center;
}
.export {
  color: red;
  font-weight: bold;
}
.import {
  color: green;
  font-weight: bold;
}
.low-stock {
  color: orange;
  font-weight: bold;
}
.pagination {
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
}
</style>
