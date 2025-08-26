<template>
  <div class="p-1">
    <div>
      <!-- Thống kê user -->
      <div class="mb-5">
        <h4 class="section-title">👥 Thống kê người dùng</h4>
        <div class="row g-3">
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-primary">
              <div class="stat-icon">👥</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.countAllAccounts) }}</h5>
                <p class="stat-label">Tổng tài khoản</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-success">
              <div class="stat-icon">✅</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.countActiveAccounts) }}</h5>
                <p class="stat-label">Tài khoản hoạt động</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-warning">
              <div class="stat-icon">❌</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.countInactiveAccounts) }}</h5>
                <p class="stat-label">Tài khoản bị khóa</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">👤</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.countUserAccounts) }}</h5>
                <p class="stat-label">Khách hàng</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-secondary">
              <div class="stat-icon">👨‍💼</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.countAdminAccounts) }}</h5>
                <p class="stat-label">Quản trị viên</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Thống kê đăng ký theo thời gian -->
      <div class="mb-5">
        <h4 class="section-title">📅 Đăng ký theo thời gian</h4>
        <div class="row g-3">
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">📅</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.day) }}</h5>
                <p class="stat-label">Hôm nay</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">📊</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.week) }}</h5>
                <p class="stat-label">Tuần</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">📈</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.month) }}</h5>
                <p class="stat-label">Tháng</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">🗓️</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.user.year) }}</h5>
                <p class="stat-label">Năm</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Thống kê doanh thu -->
      <div class="mb-5">
        <h4 class="section-title">📈 Thống kê doanh thu</h4>
        <div class="row g-3">
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">📅</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.revenue.day) }}đ</h5>
                <p class="stat-label">Hôm nay</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">📊</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.revenue.week) }}đ</h5>
                <p class="stat-label">Tuần</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">📈</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.revenue.month) }}đ</h5>
                <p class="stat-label">Tháng</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-info">
              <div class="stat-icon">🗓️</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.revenue.year) }}đ</h5>
                <p class="stat-label">Năm</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- Thống kê đơn hàng -->
      <div class="mb-5">
        <h4 class="section-title">📈 Thống kê đơn hàng</h4>
        <div class="row g-3">
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-warning">
              <div class="stat-icon">🕒</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.order.pending) }}</h5>
                <p class="stat-label">Chờ xác nhận</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-primary">
              <div class="stat-icon">🚚</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.order.shipping) }}</h5>
                <p class="stat-label">Đang giao hàng</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-success">
              <div class="stat-icon">✅</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.order.completed) }}</h5>
                <p class="stat-label">Đã hoàn thành</p>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6">
            <div class="stat-card bg-secondary">
              <div class="stat-icon">❌</div>
              <div class="stat-content">
                <h5 class="stat-number">{{ formatNumber(this.order.cancelled) }}</h5>
                <p class="stat-label">Bị hủy</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'AdminReportPage',
  data() {
    return {
      user: {},
      revenue: {},
      order: {},
    };
  },
  methods: {
    async fetchUserStatistics() {
      try {
        axios.get('http://localhost:8080/api/statistic/user').then(response => {
          this.user = response.data;
        });
      } catch (error) {
        console.error('Lấy thống kê user thất bại:', error);
        throw error;
      }
    },
    async fetchRevenueStatistics() {
      try {
        axios.get('http://localhost:8080/api/statistic/revenue').then(response => {
          this.revenue = response.data;
        });
      } catch (error) {
        console.error('Lấy thống kê doanh thu thất bại:', error);
        throw error;
      }
    },
    async fetchOrderStatistics() {
      try {
        axios.get('http://localhost:8080/api/statistic/order').then(response => {
          this.order = response.data;
        });
      } catch (error) {
        console.error('Lấy thống kê đơn hàng thất bại:', error);
        throw error;
      }
    },

    formatNumber(number) {
      return new Intl.NumberFormat('vi-VN').format(number);
    }
  },
  mounted() {
    this.fetchUserStatistics();
    this.fetchRevenueStatistics();
    this.fetchOrderStatistics();
  }
};
</script>

<style scoped>
h2 {
  color: #0d6efd;
}

.section-title {
  color: #495057;
  border-bottom: 2px solid #e9ecef;
  padding-bottom: 8px;
  margin-bottom: 20px;
  font-weight: 600;
}

.stat-card {
  background: linear-gradient(135deg, var(--bs-primary) 0%, var(--bs-primary-rgb) 100%);
  color: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  height: 100px;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.stat-card.bg-primary {
  background: linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%);
}

.stat-card.bg-success {
  background: linear-gradient(135deg, #198754 0%, #157347 100%);
}

.stat-card.bg-warning {
  background: linear-gradient(135deg, #ffc107 0%, #ffca2c 100%);
  color: #000;
}

.stat-card.bg-info {
  background: linear-gradient(135deg, #0dcaf0 0%, #31d2f2 100%);
  color: #000;
}

.stat-card.bg-secondary {
  background: linear-gradient(135deg, #6c757d 0%, #5c636a 100%);
}

.stat-icon {
  font-size: 2rem;
  opacity: 0.9;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 1.8rem;
  font-weight: bold;
  margin: 0;
  line-height: 1;
}

.stat-label {
  font-size: 0.9rem;
  margin: 5px 0 0 0;
  opacity: 0.9;
}

.placeholder-section {
  background: #f8f9fa;
  border: 2px dashed #dee2e6;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  min-height: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .stat-card {
    padding: 15px;
    height: auto;
    min-height: 80px;
  }

  .stat-number {
    font-size: 1.5rem;
  }

  .stat-icon {
    font-size: 1.5rem;
  }
}
</style>
