<template>
  <div class="p-16 bg-gradient-to-br from-blue-100 to-purple-200 rounded-4xl shadow-4xl max-w-[1800px] w-full mx-auto border-4 border-blue-300 backdrop-filter backdrop-blur-sm">
    <!-- Header Section -->
    <div class="flex flex-col sm:flex-row justify-between items-center mb-16 pb-8 border-b-4 border-purple-400">
      <h2 class="text-7xl font-black text-gray-900 leading-tight mb-8 sm:mb-0 drop-shadow-xl flex items-center">
        <span class="inline-block mr-6 text-blue-800 transform rotate-3 scale-125">📦</span> Quản lý Kho hàng
      </h2>
      <button
        @click="fetchData"
        class="bg-blue-700 hover:bg-blue-800 text-white px-14 py-6 rounded-full font-extrabold text-2xl shadow-3xl transition-all duration-400 ease-in-out transform hover:-translate-y-2 hover:scale-105 focus:outline-none focus:ring-6 focus:ring-blue-500 focus:ring-opacity-80 active:bg-blue-900 animate-bounce-subtle"
      >
        Tải lại dữ liệu
      </button>
    </div>

    <!-- Inventory Logs Section -->
    <div class="mb-20 bg-white p-14 rounded-4xl shadow-2xl border-2 border-gray-200 animate-fade-in-up">
      <h3 class="text-6xl font-extrabold text-gray-800 mb-14 text-center leading-tight">Nhật ký tồn kho gần đây</h3>
      <div v-if="loading" class="text-gray-700 text-center text-3xl font-medium py-20 bg-gray-100 rounded-3xl animate-pulse-fast flex items-center justify-center">
        <svg class="animate-spin -ml-1 mr-4 h-10 w-10 text-purple-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        Đang tải dữ liệu...
      </div>
      <div v-else-if="error" class="text-red-800 text-center text-3xl font-medium py-20 bg-red-100 rounded-3xl border-2 border-red-400 flex items-center justify-center">
        <span class="mr-4 text-4xl">⚠️</span> Lỗi: {{ error }}
      </div>
      <div v-else>
        <div v-if="inventoryLogs.length === 0" class="text-gray-600 text-center text-3xl py-20 bg-gray-100 rounded-3xl border-2 border-gray-300">
          Không có nhật ký tồn kho nào.
        </div>
        <div v-else class="overflow-x-auto rounded-3xl border-2 border-gray-300 shadow-xl">
          <table class="min-w-full text-xl text-left">
            <thead class="bg-blue-600 text-white uppercase tracking-wider font-extrabold border-b-4 border-blue-700">
              <tr>
                <th class="px-12 py-6 border-r-2 border-blue-700">ID</th>
                <th class="px-12 py-6 border-r-2 border-blue-700">ID Chi tiết SP</th>
                <th class="px-12 py-6 border-r-2 border-blue-700">Hành động</th>
                <th class="px-12 py-6 border-r-2 border-blue-700">Số lượng</th>
                <th class="px-12 py-6 border-r-2 border-blue-700">Loại tham chiếu</th>
                <th class="px-12 py-6 border-r-2 border-blue-700">ID tham chiếu</th>
                <th class="px-12 py-6">Thời gian</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="log in inventoryLogs" :key="log.id" class="border-b-2 border-gray-200 last:border-b-0 hover:bg-blue-50 transition-all duration-200 ease-in-out">
                <td class="px-12 py-5 border-r-2 border-gray-200 text-gray-800">{{ log.id }}</td>
                <td class="px-12 py-5 border-r-2 border-gray-200 text-gray-800">{{ log.productDetail?.id || 'N/A' }}</td>
                <td class="px-12 py-5 border-r-2 border-gray-200 font-semibold text-gray-900">{{ log.action }}</td>
                <td :class="['px-12 py-5 border-r-2 border-gray-200 font-black text-3xl', log.quantity > 0 ? 'text-green-600' : 'text-red-600']">
                  {{ log.quantity }}
                </td>
                <td class="px-12 py-5 border-r-2 border-gray-200 text-gray-800">{{ log.referenceType || 'N/A' }}</td>
                <td class="px-12 py-5 border-r-2 border-gray-200 text-gray-800">{{ log.referenceId || 'N/A' }}</td>
                <td class="px-12 py-5 text-gray-600">{{ formatDate(log.createdAt) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Low Stock Alert Section -->
    <div class="mt-20 bg-white p-14 rounded-4xl shadow-2xl border-2 border-gray-200 animate-fade-in-up">
      <h3 class="text-6xl font-extrabold text-gray-800 mb-14 text-center leading-tight">Cảnh báo tồn kho thấp</h3>
      <div v-if="loading" class="text-gray-700 text-center text-3xl font-medium py-20 bg-gray-100 rounded-3xl animate-pulse-fast flex items-center justify-center">
        <svg class="animate-spin -ml-1 mr-4 h-10 w-10 text-purple-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        Đang kiểm tra cảnh báo...
      </div>
      <div v-else-if="error" class="text-red-800 text-center text-3xl font-medium py-20 bg-red-100 rounded-3xl border-2 border-red-400 flex items-center justify-center">
        <span class="mr-4 text-4xl">⚠️</span> Lỗi: {{ error }}
      </div>
      <div v-else>
        <div v-if="lowStockProducts.length === 0" class="text-gray-600 text-center text-3xl py-20 bg-gray-100 rounded-3xl border-2 border-gray-300">
          Không có sản phẩm nào tồn kho thấp.
        </div>
        <div v-else class="overflow-x-auto rounded-3xl border-2 border-yellow-400 shadow-xl">
          <table class="min-w-full text-xl text-left">
            <thead class="bg-yellow-600 text-white uppercase tracking-wider font-extrabold border-b-4 border-yellow-700">
              <tr>
                <th class="px-12 py-6 border-r-2 border-yellow-700">ID</th>
                <th class="px-12 py-6 border-r-2 border-yellow-700">Tên sản phẩm</th>
                <th class="px-12 py-6 border-r-2 border-yellow-700">Màu sắc</th>
                <th class="px-12 py-6 border-r-2 border-yellow-700">Kích thước</th>
                <th class="px-12 py-6 text-center">Tồn kho hiện tại</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in lowStockProducts" :key="product.id" class="border-b-2 border-yellow-200 last:border-b-0 hover:bg-yellow-50 transition-all duration-200 ease-in-out">
                <td class="px-12 py-5 border-r-2 border-yellow-200 text-gray-800">{{ product.id }}</td>
                <td class="px-12 py-5 border-r-2 border-yellow-200 font-semibold text-gray-900">{{ product.product?.name || 'N/A' }}</td>
                <td class="px-12 py-5 border-r-2 border-yellow-200 text-gray-800">{{ product.color || 'N/A' }}</td>
                <td class="px-12 py-5 border-r-2 border-yellow-200 text-gray-800">{{ product.size || 'N/A' }}</td>
                <td class="px-12 py-5 text-center font-black text-red-600 text-3xl">{{ product.quantity }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Toast Notification -->
    <div v-if="showNotification" class="notification" :class="notificationType">
      {{ notificationMessage }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminInventoryPage',
  data() {
    return {
      inventoryLogs: [],
      lowStockProducts: [],
      loading: true,
      error: null,
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
    async fetchData() {
      this.loading = true;
      this.error = null;
      try {
        // Fetch Inventory Logs
        const inventoryLogsResponse = await fetch('http://localhost:8080/inventoryLogs');
        if (!inventoryLogsResponse.ok) {
          throw new Error(`HTTP error! status: ${inventoryLogsResponse.status}`);
        }
        this.inventoryLogs = await inventoryLogsResponse.json();

        // Fetch Low Stock Products (assuming you have an endpoint for this)
        // You would need to implement this endpoint in your Spring Boot backend
        // For example, a GET /productDetails/lowStock endpoint that returns ProductDetail objects
        const lowStockProductsResponse = await fetch('http://localhost:8080/productDetails/lowStock');
        if (!lowStockProductsResponse.ok) {
          throw new Error(`HTTP error! status: ${lowStockProductsResponse.status}`);
        }
        this.lowStockProducts = await lowStockProductsResponse.json();

      } catch (err) {
        console.error("Lỗi khi tải dữ liệu:", err);
        this.error = "Không thể tải dữ liệu. Vui lòng kiểm tra kết nối backend và CORS.";
        this.showNotify('Không thể tải dữ liệu kho hàng!', 'error');

        // Fallback to mock data if API call fails
        this.inventoryLogs = [
            { id: 1, productDetail: { id: 101, name: 'Áo Polo Xanh' }, action: 'IMPORT', quantity: 50, referenceType: 'ImportInvoice', referenceId: 1, createdAt: '2025-07-17T10:00:00' },
            { id: 2, productDetail: { id: 102, name: 'Quần Jean Đen' }, action: 'EXPORT', quantity: -5, referenceType: 'Order', referenceId: 101, createdAt: '2025-07-17T11:30:00' },
            { id: 3, productDetail: { id: 101, name: 'Áo Polo Xanh' }, action: 'ADJUSTMENT', quantity: -2, referenceType: 'Adjustment', referenceId: 5, createdAt: '2025-07-17T14:00:00' },
            { id: 4, productDetail: { id: 103, name: 'Giày Sneaker Trắng' }, action: 'IMPORT', quantity: 100, referenceType: 'ImportInvoice', referenceId: 2, createdAt: '2025-07-17T16:00:00' },
        ];
        this.lowStockProducts = [
            { id: 201, product: { name: 'Áo thun cơ bản' }, color: 'Trắng', size: 'M', quantity: 3 },
            { id: 202, product: { name: 'Quần jean slim-fit' }, color: 'Xanh đậm', size: 'L', quantity: 5 },
            { id: 203, product: { name: 'Váy maxi hoa' }, color: 'Đa sắc', size: 'S', quantity: 1 },
        ];
      } finally {
        this.loading = false;
      }
    },
    formatDate(dateString) {
      if (!dateString) return "";
      return new Date(dateString).toLocaleString("vi-VN", {
        hour: "2-digit",
        minute: "2-digit",
        day: "2-digit",
        month: "2-digit",
        year: "numeric"
      });
    }
  },
  mounted() {
    this.fetchData();
  }
};
</script>

<style scoped>
/* Animation cho form */
@keyframes fade-in-down {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.animate-fade-in-down {
  animation: fade-in-down 0.5s ease-out forwards;
}

/* Custom animation for subtle bounce */
@keyframes bounce-subtle {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}
.animate-bounce-subtle {
  animation: bounce-subtle 2s infinite ease-in-out;
}

/* Custom animation for pulse-fast */
@keyframes pulse-fast {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: .5;
  }
}
.animate-pulse-fast {
  animation: pulse-fast 1s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}


/* Style cho thông báo Toast */
.notification {
  position: fixed;
  bottom: 20px;
  right: 20px;
  padding: 15px 25px;
  border-radius: 8px;
  color: white;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: fadeInOut 3.5s ease forwards;
  font-weight: 600;
}
.notification.success {
  background-color: #22c55e; /* Tailwind green-500 */
}
.notification.error {
  background-color: #ef4444; /* Tailwind red-500 */
}
@keyframes fadeInOut {
  0% { opacity: 0; transform: translateY(20px); }
  10% { opacity: 1; transform: translateY(0); }
  90% { opacity: 1; transform: translateY(0); }
  100% { opacity: 0; transform: translateY(20px); }
}

/* Style cho thông báo lỗi dưới input (nếu có form) */
.error-message {
  color: #ef4444; /* Tailwind red-500 */
  font-weight: 500;
  font-size: 0.875rem; /* text-sm */
  margin-top: 0.25rem;
}
</style>
