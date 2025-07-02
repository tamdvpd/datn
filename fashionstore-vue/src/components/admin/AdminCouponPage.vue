<template>
  <div>
    <h2 class="text-center d-block">🏷️ Quản lý mã giảm giá</h2>
    <!-- ✅ FORM GỘP: Thêm & Sửa -->
     <transition name="fade-slide">
      <div v-if="showForm" class="card shadow-sm mx-auto mb-4" style="max-width: 800px;">
        <div class="card-header" :class="isEditing ? 'bg-warning text-dark' : 'bg-primary text-white'">
          <h5 class="mb-0">
            <i :class="isEditing ? 'bi bi-pencil-square me-2' : 'bi bi-plus-circle me-2'"></i>
            {{ isEditing ? 'Cập nhật Mã Giảm Giá' : 'Thêm Mã Giảm Giá' }}
          </h5>
        </div>
        <div class="card-body">
          <form @submit.prevent="submitForm">
            <div class="row mb-3">
              <div class="col-md-6">
                <label class="form-label">Mã</label>
                <input v-model="formCoupon.code" type="text" class="form-control" required />
                <div class="text-danger mt-1" v-if="validationErrors.code">{{ validationErrors.code }}</div>
              </div>
              <div class="col-md-6">
                <label class="form-label">Giảm (%)</label>
                <input v-model="formCoupon.discountPercent" type="number" min="1" max="100" step="1" class="form-control" required />
                <div class="text-danger mt-1" v-if="validationErrors.discountPercent">{{ validationErrors.discountPercent }}</div>
              </div>
            </div>

            <div class="mb-3">
              <label class="form-label">Ngày hết hạn</label>
              <input v-model="formCoupon.expiryDate" type="datetime-local" class="form-control" />
              <div class="text-danger mt-1" v-if="validationErrors.expiryDate">{{ validationErrors.expiryDate }}</div>
            </div>

            <div class="mb-3">
              <label class="form-label">Số lượng</label>
              <input v-model="formCoupon.quantity" type="number" min="1" class="form-control" />
              <div class="text-danger mt-1" v-if="validationErrors.quantity">{{ validationErrors.quantity }}</div>
            </div>

            <div class="mb-3">
              <label class="form-label">Trạng thái</label>
              <div class="form-check form-switch">
                <input class="form-check-input" type="checkbox" id="statusSwitch" v-model="formCoupon.status" :true-value="true" :false-value="false" />
                <label class="form-check-label" for="statusSwitch">
                  {{ formCoupon.status ? 'Bật' : 'Tắt' }}
                </label>
              </div>
            </div>

            <div class="text-end">
              <button type="submit" :class="['me-2 btn', isEditing ? 'btn-warning' : 'btn-primary']">
                <i class="bi bi-save me-1"></i>{{ isEditing ? 'Cập nhật' : 'Thêm' }}
              </button>
              <button type="button" class="btn btn-secondary" @click="showForm = false">
                <i class="bi bi-x-circle me-1"></i>Hủy
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <div class="d-flex align-items-center justify-content-between mb-3 mt-4">
      <h3 class="mb-0">
        <i class="bi bi-tags-fill text-primary me-2"></i> Danh sách mã giảm giá
      </h3>
      <button class="btn btn-outline-primary" @click="showAdd()">
        <i class="bi bi-plus-square-fill"></i>
      </button>
    </div>
  <div class="table-responsive">
    <table class="table table-hover table-bordered text-center align-middle mx-auto">
      <thead class="bg-gray-200">
        <tr>
          <th class="border px-4 py-2">Mã</th>
          <th class="border px-4 py-2">Giảm giá (%)</th>
          <th class="border px-4 py-2">Ngày tạo</th>
          <th class="border px-4 py-2">Ngày hết hạn</th>
          <th class="border px-4 py-2">Số lượng</th>
          <th class="border px-4 py-2">Trạng thái</th>
          <th class="border px-4 py-2">Cập nhật</th>
          <th class="border px-4 py-2">Sửa</th>
          <th class="border px-4 py-2">Xóa</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="coupon in coupons" :key="coupon.id">
          <td class="border px-4 py-2">{{ coupon.code }}</td>
          <td class="border px-4 py-2">{{ coupon.discountPercent }}</td>
          <td class="border px-4 py-2">{{ formatDateTime(coupon.createdAt) }}</td>
          <td class="border px-4 py-2">{{ formatDateTime(coupon.expiryDate) }}</td>
          <td class="border px-4 py-2">{{ coupon.quantity }}</td>
          <td class="border px-4 py-2">
            <span :class="coupon.status ? 'badge bg-success' : 'badge bg-danger'">
              {{ coupon.status ? 'Hoạt động' : 'Không hoạt động' }}
            </span>
          </td>
          <td class="border px-4 py-2">{{ formatDateTime(coupon.updatedAt) }}</td>
          <td class="border px-4 py-2">
            <button class="btn btn-outline-warning" @click="showEdit(coupon)">
              <i class="bi bi-pencil-square"></i>
            </button>
          </td>
          <td class="border px-4 py-2">
            <button class="btn btn-outline-danger" @click="deleteCoupon(coupon.id)">
              <i class="bi bi-trash"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
    <nav aria-label="Page navigation example" class="mt-4">
      <ul class="pagination justify-content-center">
        <li class="page-item disabled">
          <a class="page-link">Previous</a>
        </li>
        <li class="page-item"><a class="page-link" href="#">1</a></li>
        <li class="page-item"><a class="page-link" href="#">2</a></li>
        <li class="page-item"><a class="page-link" href="#">3</a></li>
        <li class="page-item">
          <a class="page-link" href="#">Next</a>
        </li>
      </ul>
    </nav>
    <div v-if="showNotification" class="notification" :class="notificationType">
      {{ notificationMessage }}
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'AdminCouponPage',
  data() {
    return {
      coupons: [],
      formCoupon: {
        id: null,
        code: '',
        discountPercent: '',
        expiryDate: '',
        quantity: 0,
        status: true
      },
      isEditing: false,
      showForm: false,
      validationErrors : {},
      showNotification: false,
      notificationMessage: '',
      notificationType: 'success'
    };
  },
  methods: {
    showAdd() {
      this.resetForm();
      this.showForm = true;
      this.isEditing = false;
      this.$nextTick(() => {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    });
    },
    showEdit(coupon) {
      this.formCoupon = { ...coupon };
      this.showForm = true;
      this.isEditing = true;
      this.$nextTick(() => {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    });
    },
    submitForm() {
      if (this.isEditing) {
        axios.put(`http://localhost:8080/api/coupons/${this.formCoupon.id}`, this.formCoupon)
          .then(() => {
            this.showNotify('Cập nhật mã giảm giá thành công!');
            this.resetForm();
            this.fetchCoupons();
          })
          .catch(error => {
            console.error('Lỗi khi cập nhật mã:', error);
            this.showNotify('Cập nhật mã giảm giá thất bại!', 'error');
          });
      } else {
        axios.post('http://localhost:8080/api/coupons', this.formCoupon)
          .then(() => {
            this.showNotify('Thêm mã giảm giá thành công!');
            this.resetForm();
            this.validationErrors = {};
            this.fetchCoupons();
          })
          .catch(error => {
            console.error('Lỗi khi thêm mã:', error);
            if (error.response && error.response.status === 400) {
              this.validationErrors = error.response.data;
              this.showNotify('Vui lòng kiểm tra lại các trường!', 'error');
            } else {
              this.showNotify('Thêm mã giảm giá thất bại!', 'error');
            }
          });
      }
    },
    resetForm() {
      this.formCoupon = {
        id: null,
        code: '',
        discountPercent: '',
        expiryDate: '',
        quantity: 0,
        status: true
      };
      this.validationErrors = {};
      this.showForm = false;
      this.isEditing = false;
    },
    deleteCoupon(id) {
      if (confirm('Bạn có chắc chắn muốn xóa mã giảm giá này không?')) {
        axios.delete(`http://localhost:8080/api/coupons/${id}`)
          .then(() => {
            this.fetchCoupons();
            this.showNotify('Xoá mã giảm giá thành công!');
          })
          .catch(error => {
            console.error('Lỗi khi xoá mã:', error);
            this.showNotify('Xoá mã giảm giá thất bại!', 'error');
          });
      }
    },
    fetchCoupons() {
      axios.get('http://localhost:8080/api/coupons')
        .then(response => {
          this.coupons = response.data;
        })
        .catch(error => {
        this.showNotify('Tải danh sách mã giảm giá thất bại!');
          console.error('Error fetching coupons:', error);
        });
    },
    showNotify(message, type = 'success', duration = 3000) {
      this.notificationMessage = message;
      this.notificationType = type;
      this.showNotification = true;
      setTimeout(() => {
        this.showNotification = false;
      }, duration);
    },
    formatDateTime(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString() : 'N/A';
    }
  },
  mounted() {
    this.fetchCoupons();
  }
};
</script>

<style scoped>
.fade-slide-enter-active, .fade-slide-leave-active {
  transition: all 0.5s ease;
}
.fade-slide-enter-from, .fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
.fade-slide-enter-to, .fade-slide-leave-from {
  opacity: 1;
  transform: translateY(0);
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
