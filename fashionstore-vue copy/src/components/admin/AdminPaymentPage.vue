<template>
  <div>
    <h2 class="text-center d-block">💳 Quản lý phương thức thanh toán</h2>
    <div class="table-responsive">
      <transition name="fade-slide">
        <div v-if="showForm" class="card shadow-sm mx-auto mb-4" style="max-width: 800px;">
          <div class="card-header bg-warning text-dark">
            <h5 class="mb-0">
              <i class="bi bi-pencil-square me-2"></i>Cập nhật phương thức thanh toán
            </h5>
          </div>
          <div class="card-body">
            <form @submit.prevent="submitForm" class="needs-validation">
              <div class="row g-3 mb-3">
                <div class="col-md-2">
                  <label for="paymentCode" class="form-label">Mã</label>
                  <input v-model="formPayment.code" type="text" class="form-control" id="paymentCode" required
                    disabled />
                  <div class="text-danger mt-1" v-if="validationErrors.code">
                    {{ validationErrors.code }}
                  </div>
                </div>
                <div class="col-md-10">
                  <label for="paymentDescription" class="form-label">Mô tả</label>
                  <input v-model="formPayment.description" type="text" class="form-control" id="paymentDescription" />
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label d-block">Trạng thái</label>
                <div class="form-check form-switch">
                  <input class="form-check-input" type="checkbox" id="statusSwitch" v-model="formPayment.status"
                    :true-value="true" :false-value="false" />
                  <label class="form-check-label" for="statusSwitch">
                    {{ formPayment.status ? 'Bật' : 'Tắt' }}
                  </label>
                </div>
              </div>

              <div class="text-end mt-4">
                <button type="submit" class="btn btn-warning me-2">
                  <i class="bi bi-save me-1"></i>Cập nhật
                </button>
                <button type="button" class="btn btn-secondary" @click="showForm = false">
                  <i class="bi bi-x-circle me-1"></i>Hủy
                </button>
              </div>
            </form>
          </div>
        </div>
      </transition>
      <table class="table table-hover table-bordered text-center align-middle mx-auto">
        <thead class="bg-gray-200">
          <tr>
            <th class="border px-4 py-2">Mã</th>
            <th class="border px-4 py-2">Mô tả</th>
            <th class="border px-4 py-2">Trạng thái</th>
            <th class="border px-4 py-2">Ngày cập nhật</th>
            <th class="border px-4 py-2">Sửa</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="payment in payments" :key="payment.id">
            <td class="border px-4 py-2">{{ payment.code }}</td>
            <td class="border px-4 py-2">{{ payment.description }}</td>
            <td class="border px-4 py-2">
              <span :class="payment.status ? 'badge bg-success' : 'badge bg-danger'">
                {{ payment.status ? 'Hoạt động' : 'Không hoạt động' }}
              </span>
            </td>
            <td class="border px-4 py-2">{{ formatDateTime(payment.updatedAt) }}</td>
            <td class="border px-4 py-2">
              <button class="btn btn-outline-warning" @click="showEdit(payment)">
                <i class="bi bi-pencil-square"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="showNotification" class="notification" :class="notificationType">
      {{ notificationMessage }}
    </div>
  </div>
</template>
<script>
import axios from 'axios';
export default {
  name: 'AdminPaymentPage',
  data() {
    return {
      payments: [],
      formPayment: {
        id: null,
        code: '',
        description: '',
        status: true
      },
      isEditing: false,
      showForm: false,
      showNotification: false,
      validationErrors: {}
    }
  },
  methods: {
    showEdit(payment) {
      this.formPayment = { ...payment };
      this.showForm = true;
      this.isEditing = true;
    },
    fetchPayment() {
      axios.get('http://localhost:8080/api/payment_method').then(response => {
        this.payments = response.data;
      }).catch(error => {
        this.showNotify('Tải dữ liệu không thành công')
        console.error('Error fetching coupons:', error);
      });
    },
    submitForm() {
      axios.put(`http://localhost:8080/api/payment_method/${this.formPayment.id}`, this.formPayment)
        .then(() => {
          this.showNotify('Cập nhật mã giảm giá thành công!');
          this.resetForm();
          this.fetchPayment();
        })
        .catch(error => {
          console.error('Lỗi khi cập nhật mã:', error);
          this.showNotify('Cập nhật phương thức thanh toán thất bại!', 'error');
        });
    },
    resetForm() {
      this.formPayment = {
        id: null,
        code: '',
        description: '',
        status: true
      };
      this.validationErrors = {};
      this.showForm = false;
      this.isEditing = false;
    },
    showNotify(message, type = 'success', duration = 5000) {
      this.notificationMessage = message;
      this.notificationType = type;
      this.showNotification = true;
      setTimeout(() => {
        this.showNotification = false;
      }, duration);
    },
    formatDateTime(dateStr) {
      return dateStr ? new Date(dateStr).toLocaleString() : 'N/A';
    },
  },
  mounted() {
    this.fetchPayment();
  }
}
</script>
<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.5s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.fade-slide-enter-to,
.fade-slide-leave-from {
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