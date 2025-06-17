<template>
  <div class="p-4">
    <h2 class="d-inline-block me-2">🏷️ Quản lý mã giảm giá</h2>
    <button class="btn btn-outline-primary mb-3" @click="showAdd()"><i class="bi bi-plus-square-fill"></i></button>
    <!-- ✅ FORM THÊM - ĐẸP & HIỆN ĐẠI -->
    <div v-show="showAddForm" class="card shadow-sm mx-auto mb-4" style="max-width: 800px;">
    <div class="card-header bg-primary text-white">
        <h5 class="mb-0"><i class="bi bi-plus-circle me-2"></i>Thêm Mã Giảm Giá</h5>
    </div>
    <div class="card-body">
        <form @submit.prevent="addCoupon">
        <div class="row mb-3">
            <div class="col-md-6">
            <label class="form-label">Mã <span class="text-danger">*</span></label>
            <input v-model="newCoupon.code" type="text" class="form-control" required />
            </div>
            <div class="col-md-6">
            <label class="form-label">Giảm (%) <span class="text-danger">*</span></label>
            <input v-model="newCoupon.discountPercent" type="number" step="0.01" class="form-control" required />
            </div>
        </div>

        <div class="mb-3">
            <label class="form-label">Ngày hết hạn</label>
            <input v-model="newCoupon.expiryDate" type="date" class="form-control" />
        </div>

        <div class="text-end">
            <button type="submit" class="btn btn-primary me-2">
            <i class="bi bi-save me-1"></i>Thêm
            </button>
            <button @click="showAddForm = false" type="button" class="btn btn-secondary">
            <i class="bi bi-x-circle me-1"></i>Hủy
            </button>
        </div>
        </form>
    </div>
    </div>
    <!-- ✅ FORM SỬA - ĐẸP & HIỆN ĐẠI -->
    <div v-show="showEditForm" class="card shadow-sm mx-auto mb-4" style="max-width: 800px;">
    <div class="card-header bg-warning text-dark">
        <h5 class="mb-0"><i class="bi bi-pencil-square me-2"></i>Cập nhật Mã Giảm Giá</h5>
    </div>
    <div class="card-body">
        <form @submit.prevent="updateCoupon">
        <div class="row mb-3">
            <div class="col-md-6">
            <label class="form-label">Mã <span class="text-danger">*</span></label>
            <input v-model="editCoupon.code" type="text" class="form-control" required />
            </div>
            <div class="col-md-6">
            <label class="form-label">Giảm (%) <span class="text-danger">*</span></label>
            <input v-model="editCoupon.discountPercent" type="number" step="0.01" class="form-control" required />
            </div>
        </div>

        <div class="mb-3">
            <label class="form-label">Ngày hết hạn</label>
            <input v-model="editCoupon.expiryDate" type="date" class="form-control" />
        </div>

        <div class="text-end">
            <button type="submit" class="btn btn-success me-2">
            <i class="bi bi-check-circle me-1"></i>Cập nhật
            </button>
            <button @click="showEditForm = false" type="button" class="btn btn-secondary">
            <i class="bi bi-x-circle me-1"></i>Hủy
            </button>
        </div>
        </form>
    </div>
    </div>
    <h3 class="mt-4 mb-3"><i class="bi bi-tags-fill text-primary me-2"></i> Danh sách mã giảm giá</h3>
    <!-- ✅ BẢNG DANH SÁCH -->
    <table class="table-auto w-fit border border-gray-300 mx-auto">
      <thead class="bg-gray-200">
        <tr>
          <th class="border px-4 py-2">ID</th>
          <th class="border px-4 py-2">Mã</th>
          <th class="border px-4 py-2">Giảm giá (%)</th>
          <th class="border px-4 py-2">Hạn</th>
          <th class="border px-4 py-2">Ngày Tạo</th>
          <th class="border px-4 py-2">Cập nhật</th>
          <th class="border px-4 py-2">Chỉnh Sửa</th>
          <th class="border px-4 py-2">Xóa</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="coupon in coupons" :key="coupon.id">
          <td class="border px-4 py-2">{{ coupon.id }}</td>
          <td class="border px-4 py-2">{{ coupon.code }}</td>
          <td class="border px-4 py-2">{{ coupon.discountPercent }}</td>
          <td class="border px-4 py-2">{{ formatDate(coupon.expiryDate) }}</td>
          <td class="border px-4 py-2">{{ formatDateTime(coupon.createdAt) }}</td>
          <td class="border px-4 py-2">{{ formatDateTime(coupon.updatedAt) }}</td>
          <td class="border px-4 py-2"><button class="btn btn-outline-warning" @click="showEdit(coupon)"><i class="bi bi-pencil-square"></i></button></td>
          <td class="border px-4 py-2"><button class="btn btn-outline-danger" @click="deleteCoupon(coupon.id)"><i class="bi bi-trash"></i></button></td>
        </tr>
      </tbody>
    </table>
    <!-- Thông báo tự động ẩn -->
    <div v-if="showNotification" class="notification" :class="notificationType">
      {{ notificationMessage }}
    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  name: 'AdminCouponPage',
  data() {
    return {
      coupons: [],
      showAddForm: true,
      showEditForm: false,
      showNotification: false,
      notificationMessage: '',
      notificationType: 'success',
      newCoupon: {
        code: '',
        discountPercent: '',
        expiryDate: ''
      },
      editCoupon: {
        id: null,
        code: '',
        discountPercent: '',
        expiryDate: ''
      }
    }
  },
  methods: {
    showAdd(){
        this.showAddForm = true;
        this.showEditForm = false;
    },
    showEdit(){
        this.showEditForm = true;
        this.showAddForm = false;
    },
    showNotify(message, type = 'success', duration = 3000) {
      this.notificationMessage = message
      this.notificationType = type
      this.showNotification = true
      
      setTimeout(() => {
        this.showNotification = false
      }, duration)
    },
    fetchCoupons() {
      axios.get('http://localhost:8080/coupons')
        .then(response => {
          this.coupons = response.data
        })
        .catch(error => {
          console.error('Error fetching coupons:', error)
        })
    },
    addCoupon() {
        axios.post('http://localhost:8080/coupons', this.newCoupon).then(() => {
        this.showNotify('Thêm mã giảm giá thành công!', 'success');
        
        // 🔧 Reset form thêm
        this.newCoupon = {
            code: '',
            discountPercent: '',
            expiryDate: ''
        };

        // 🔧 Ẩn form sau khi thêm
        this.showAddForm = false;

        // 🔧 Tải lại danh sách mới
        this.fetchCoupons();
        })
        .catch(error => {
        console.error('Lỗi khi thêm mã giảm giá:', error);
        this.showNotify('Thêm mã giảm giá thất bại!', 'error');
        });
    },
    updateCoupon(){
    axios.put(`http://localhost:8080/coupons/${this.editCoupon.id}`, this.editCoupon).then(() => {
      this.showNotify('Cập nhật mã giảm giá thành công!', 'success');

      // ✅ Reset form sửa
      this.editCoupon = {
        id: null,
        code: '',
        discountPercent: '',
        expiryDate: ''
      };

      // ✅ Ẩn form sửa
      this.showEditForm = false;

      // ✅ Tải lại danh sách
      this.fetchCoupons();
    })
    .catch(error => {
      console.error('Lỗi khi cập nhật mã giảm giá:', error);
      this.showNotify('Cập nhật mã giảm giá thất bại!', 'error');
    });
    },
    showEdit(coupon) {
    this.editCoupon = { ...coupon }; // copy toàn bộ coupon vào form sửa
    this.showEditForm = true;
    this.showAddForm = false;
    },
    deleteCoupon(id) {
        if (confirm("Bạn có chắc chắn muốn xóa mã giảm giá này không?")) {
        axios.delete(`http://localhost:8080/coupons/${id}`)
        .then(() => {
            this.fetchCoupons(); // load lại danh sách sau khi xóa
            this.showNotify('Xóa mã giảm giá thành công!');
        })
        .catch(error => {
            console.error("Lỗi khi xóa mã giảm giá:", error);
            this.showNotify('Xoá thất bại!', 'error');
        });
    }
    },
    formatDate(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleDateString() : 'N/A'
    },
    formatDateTime(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString() : 'N/A'
    }
  },
  mounted() {
    this.fetchCoupons()
  }
};
</script>
<style scoped>
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
  0% { opacity: 0; transform: translateY(20px); }
  10% { opacity: 1; transform: translateY(0); }
  90% { opacity: 1; transform: translateY(0); }
  100% { opacity: 0; transform: translateY(20px); }
}
</style>