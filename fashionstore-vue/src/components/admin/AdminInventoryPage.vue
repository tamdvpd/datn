<template>
  <div class="container my-4">
    <!-- ======================= FORM NHẬP HÀNG ======================= -->
    <section class="mb-5">
   <div class="card shadow-sm">
  <div class="card-header bg-primary text-white">
    <h5 class="mb-0">➕ Nhập hàng</h5>
  </div>
  <div class="card-body">
    <form @submit.prevent="submitImport" class="row g-3">
      <div class="col-md-4">
        <label class="form-label">Tên sản phẩm</label>
        <select v-model.number="newLog.productDetailId" class="form-select" required>
          <option disabled value="">-- Chọn sản phẩm --</option>
          <option v-for="item in stocks" :key="item.productDetailId" :value="item.productDetailId">
            {{ item.productName }} - {{ item.color }} - {{ item.size }}
          </option>
        </select>
      </div>

      <div class="col-md-4">
        <label class="form-label">Số lượng</label>
        <input v-model.number="newLog.quantity" type="number" class="form-control" required min="1" />
      </div>

      <div class="col-md-4">
        <label class="form-label">Ghi chú</label>
        <input v-model="newLog.note" type="text" class="form-control" />
      </div>

      <div class="col-12 text-end">
        <button type="submit" class="btn btn-success">
          <i class="bi bi-box-arrow-in-down me-1"></i> Nhập hàng
        </button>
      </div>
    </form>
  </div>
</div>
    </section>

    <!-- ======================= BẢNG KHO HÀNG ======================= -->
    <section class="mb-5">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="fw-bold">📦 Bảng kho hàng</h5>
      </div>

      <!-- Bộ lọc -->
      <div class="row g-2 mb-3">
        <div class="col-md-2">
          <input v-model="filters.product" placeholder="Tìm sản phẩm" class="form-control" />
        </div>
        <div class="col-md-2">
          <select v-model="filters.color" class="form-select">
            <option value="">All Colors</option>
            <option v-for="color in colors" :key="color" :value="color">{{ color }}</option>
          </select>
        </div>
        <div class="col-md-2">
          <select v-model="filters.size" class="form-select">
            <option value="">All Sizes</option>
            <option v-for="size in sizes" :key="size" :value="size">{{ size }}</option>
          </select>
        </div>
        <div class="col-md-2">
          <select v-model="filters.stockRange" class="form-select">
            <option value="">All Stock</option>
            <option value="0-10">0 - 10</option>
            <option value="10-50">10 - 50</option>
            <option value="50-100">50 - 100</option>
            <option value="100-999999">> 100</option>
          </select>
        </div>
        <div class="col-md-2">
          <select v-model="filters.priceRange" class="form-select">
            <option value="">All Prices</option>
            <option value="0-100000">0 - 100k</option>
            <option value="100000-500000">100k - 500k</option>
            <option value="500000-1000000">500k - 1M</option>
            <option value="1000000-999999999">> 1M</option>
          </select>
        </div>
        <div class="col-md-2">
          <select v-model="filters.discountRange" class="form-select">
            <option value="">All Discount</option>
            <option value="0-10">0% - 10%</option>
            <option value="10-30">10% - 30%</option>
            <option value="30-50">30% - 50%</option>
            <option value="50-100">> 50%</option>
          </select>
        </div>
        <div class="col-12 d-flex gap-2 mt-2">
          <button @click="applyFilters" class="btn btn-primary btn-sm">
            <i class="bi bi-funnel me-1"></i> Lọc
          </button>
          <button @click="resetFilters" class="btn btn-secondary btn-sm">
            <i class="bi bi-arrow-clockwise me-1"></i> Reset
          </button>
        </div>
      </div>

      <!-- Bảng dữ liệu kho -->
      <div class="table-responsive">
        <table class="table table-striped table-bordered text-center align-middle">
          <thead class="table-light">
            <tr>
              <th>ProductDetail ID</th>
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
              <td>{{ item.productDetailId }}</td>
              <td>{{ item.productName }}</td>
              <td>{{ item.color }}</td>
              <td>{{ item.size }}</td>
              <td :class="{ 'text-warning fw-bold': item.currentStock <= 5 }">
                {{ item.currentStock }}
              </td>
              <td>{{ formatCurrency(item.price) }}</td>
              <td>{{ item.discountPrice ? formatCurrency(item.discountPrice) : '-' }}</td>
            </tr>
            <tr v-if="stocks.length === 0">
              <td colspan="7">Không có dữ liệu</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Phân trang -->
      <div class="d-flex justify-content-between align-items-center mt-3">
        <div>
          <button class="btn btn-outline-secondary btn-sm me-2" :disabled="currentPage === 0" @click="changePage(currentPage - 1)">
            Previous
          </button>
          <button class="btn btn-outline-secondary btn-sm" :disabled="currentPage >= totalPages - 1" @click="changePage(currentPage + 1)">
            Next
          </button>
        </div>
        <span>Page {{ currentPage + 1 }} / {{ totalPages }} ({{ totalItems }} items)</span>
        <select v-model.number="sizePage" @change="fetchWarehouseStock(0)" class="form-select form-select-sm" style="width: 100px;">
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
          <option :value="50">50</option>
        </select>
      </div>
    </section>
    <!-- ======================= LỊCH SỬ NHẬP XUẤT ======================= -->
    <section>
      <h5 class="fw-bold mb-3">📋 Lịch sử nhập / xuất kho</h5>
      <div class="table-responsive">
        <table class="table table-bordered text-center align-middle">
          <thead class="table-light">
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
              <td :class="{ 'text-danger fw-bold': log.quantity < 0, 'text-success fw-bold': log.quantity > 0 }">
                {{ log.quantity }}
              </td>
              <td>{{ formatDate(log.createdAt) }}</td>
            </tr>
            <tr v-if="inventoryLogs.length === 0">
              <td colspan="5">Không có log</td>
            </tr>
          </tbody>
        </table>
      </div>
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
      newLog: { productDetailId: null, quantity: null, note: "" },
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
        const res = await fetch(`http://localhost:8080/admin/inventoryLogs/warehouse?${query.toString()}`);
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
      } else { this.filters.priceMin = null; this.filters.priceMax = null; }
      if (this.filters.discountRange) {
        const [min, max] = this.filters.discountRange.split("-");
        this.filters.discountMin = Number(min);
        this.filters.discountMax = Number(max);
      } else { this.filters.discountMin = null; this.filters.discountMax = null; }
      if (this.filters.stockRange) {
        const [min, max] = this.filters.stockRange.split("-");
        this.filters.stockMin = Number(min);
        this.filters.stockMax = Number(max);
      } else { this.filters.stockMin = null; this.filters.stockMax = null; }
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
        const res = await fetch("http://localhost:8080/admin/inventoryLogs/import", {
          method: "POST",
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          body: params,
        });
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
      this.newLog = { productDetailId: null, quantity: null, note: "" };
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
.text-warning {
  color: orange !important;
}
</style>
