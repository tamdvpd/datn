<template><MainHeader></MainHeader>
  <div class="container py-4">
    
    <h2 class="h4 fw-bold mb-4">🧾 Đơn hàng của tôi</h2>

    <!-- Bộ lọc & điều khiển -->
    <div class="row g-3 align-items-center mb-3">
      <div class="col-auto"><label class="col-form-label">Trạng thái:</label></div>
      <div class="col-auto">
        <select v-model="selectedStatus" @change="onFilterChange" class="form-select form-select-sm">
          <option value="">Tất cả</option>
          <option v-for="s in statuses" :key="s.value" :value="s.value">{{ s.label }}</option>
        </select>
      </div>

      <div class="col-auto ms-auto d-flex align-items-center gap-2">
        <span class="text-muted small">Hiển thị</span>
        <select v-model.number="pageSize" @change="onFilterChange" class="form-select form-select-sm" style="width: 90px">
          <option v-for="n in [5,10,20,50]" :key="n" :value="n">{{ n }}</option>
        </select>
        <span class="text-muted small">/ trang</span>
        <span class="text-muted small">Tổng: {{ totalElements }} đơn</span>
      </div>
    </div>

    <!-- Bảng -->
    <div class="table-responsive border rounded">
      <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
          <tr>
            <th style="width: 140px">Mã đơn</th>
            <th style="width: 180px">Ngày đặt</th>
            <th style="width: 160px" class="text-end">Tổng tiền</th>
            <th style="width: 180px">Trạng thái</th>
            <th style="width: 120px">Chi tiết</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading"><td colspan="5" class="text-center py-4">Đang tải...</td></tr>
          <tr v-else-if="error"><td colspan="5" class="text-danger py-4 text-center">{{ error }}</td></tr>
          <tr v-else-if="orders.length === 0"><td colspan="5" class="text-center py-4">Không có đơn hàng</td></tr>
          <tr v-else v-for="o in orders" :key="o.id">
            <td>#{{ o.id }}</td>
            <td>{{ formatDate(o.createdAt) }}</td>
            <td class="text-end">{{ formatCurrency(o.totalAmount) }}</td>
            <td>
              <span :class="['badge', statusBadge(o.status)]">{{ o.statusVi || humanStatus(o.status) }}</span>
            </td>
            <td><button class="btn btn-sm btn-outline-primary" @click="viewDetail(o.id)">Xem</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <div class="d-flex align-items-center justify-content-between mt-3">
      <div class="text-muted small">Trang {{ page + 1 }} / {{ totalPages || 1 }}</div>
      <div class="btn-group">
        <button class="btn btn-sm btn-outline-secondary" :disabled="page === 0" @click="goToPage(page - 1)">Trước</button>
        <button class="btn btn-sm btn-outline-secondary" :disabled="page + 1 >= totalPages" @click="goToPage(page + 1)">Sau</button>
      </div>
    </div>

    <!-- Chi tiết -->
    <div v-if="detail" class="card mt-4 shadow-sm">
      <div class="card-header d-flex justify-content-between align-items-center bg-primary text-white">
        <h5 class="mb-0">Chi tiết đơn hàng #{{ detail.id }}</h5>
        <button class="btn btn-sm btn-light" @click="detail = null">Đóng</button>
      </div>
      <div class="card-body">
<div class="row g-3">
  <div class="col-md-6">
    <p><strong>Ngày đặt:</strong> {{ formatDate(detail.createdAt) }}</p>
    <p><strong>Trạng thái:</strong> {{ detail.statusVi || humanStatus(detail.status) }}</p>
    <p><strong>Phương thức thanh toán:</strong> {{ detail.paymentMethodName || '—' }}</p>
  </div>
  <div class="col-md-6">
    <p><strong>Người nhận:</strong> {{ detail.receiverName }} - {{ detail.receiverPhone }}</p>
    <p><strong>Địa chỉ:</strong> {{ detail.receiverAddress }}</p>
    <p><strong>Đơn vị vận chuyển:</strong> {{ detail.shippingProviderName || '—' }}</p>
  </div>
</div>


        <h6 class="fw-bold mt-3">🛒 Sản phẩm</h6>
        <div class="table-responsive">
          <table class="table table-bordered table-sm">
            <thead class="table-light">
              <tr>
                <th>Tên</th>
                <th>Màu</th>
                <th>Size</th>
                <th class="text-center">SL</th>
                <th class="text-end">Đơn giá</th>
                <th class="text-end">Thành tiền</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="d in (detail.orderDetails || [])" :key="d.id">
                <td>{{ d.productDetail?.product?.name || '—' }}</td>
                <td>{{ d.productDetail?.color || '—' }}</td>
                <td>{{ d.productDetail?.size || '—' }}</td>
                <td class="text-center">{{ d.quantity }}</td>
                <td class="text-end">{{ formatCurrency(d.unitPrice) }}</td>
                <td class="text-end">{{ formatCurrency(mul(d.unitPrice, d.quantity)) }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="text-end mt-3">
          <p><strong>Phí vận chuyển:</strong> {{ formatCurrency(detail.shippingFee) }}</p>
          <p><strong>Giảm giá:</strong> {{ formatCurrency(detail.discountAmount) }}</p>
          <h5><strong>Tổng tiền:</strong> {{ formatCurrency(detail.totalAmount) }}</h5>
        </div>

        <!-- Hành động của user -->
        <div class="d-flex gap-2 mt-2 justify-content-end">
          <button
            v-if="['PENDING_PAYMENT','Pending Confirmation','CONFIRMED','PROCESSING'].includes(detail.status)"
            class="btn btn-sm btn-outline-danger"
            @click="cancelOrder(detail.id)">
            Huỷ đơn
          </button>
          <button
            v-if="['DELIVERED'].includes(detail.status)"
            class="btn btn-sm btn-success"
            @click="markReceived(detail.id)">
            Đã nhận hàng
          </button>
        </div>
      </div>
    </div>
  
  </div>
    <MainFooter></MainFooter>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios'
import MainFooter from '../MainFooter.vue'
import MainHeader from '../MainHeader.vue'

const API_BASE = 'http://localhost:8080'

const statuses = [
  { value: 'Pending Confirmation', label: 'Chờ xác nhận' },
  { value: 'CONFIRMED', label: 'Đã xác nhận' },
  { value: 'PROCESSING', label: 'Đang xử lý' },
  { value: 'SHIPPED', label: 'Đang giao' },
  { value: 'DELIVERED', label: 'Đã giao' },
  { value: 'COMPLETED', label: 'Hoàn tất' },
  { value: 'CANCELLED', label: 'Đã huỷ' }
]

const loading = ref(false)
const error = ref('')
const orders = ref([])
const page = ref(0)
const pageSize = ref(10)
const totalPages = ref(0)
const totalElements = ref(0)
const selectedStatus = ref('')
const detail = ref(null)

// helpers
function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''))
    return JSON.parse(jsonPayload)
  } catch { return null }
}
function readCookie(name) {
  const m = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'))
  return m ? decodeURIComponent(m[2]) : ''
}
function getUserId() {
  try { const u = JSON.parse(localStorage.getItem('user') || 'null'); if (u?.id) return u.id } catch {}
  const tok = localStorage.getItem('access_token') || readCookie('access_token')
  const payload = tok ? parseJwt(tok) : null
  return payload?.id || payload?.userId || null
}
function getUserEmail() {
  try { const u = JSON.parse(localStorage.getItem('user') || 'null'); if (u?.email) return u.email } catch {}
  const tok = localStorage.getItem('access_token') || readCookie('access_token')
  const payload = tok ? parseJwt(tok) : null
  return payload?.email || null
}
function formatCurrency(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}
function formatDate(d) { return d ? new Date(d).toLocaleString('vi-VN') : '—' }
function humanStatus(s) {
  const map = {
    PENDING_PAYMENT:'Đang thanh toán',
    PendingConfirmation:'Chờ xác nhận',
    CONFIRMED:'Đã xác nhận',
    PROCESSING:'Đang xử lý',
    SHIPPED:'Đang giao',
    DELIVERED:'Đã giao',
    COMPLETED:'Hoàn tất',
    CANCELLED:'Đã huỷ'
  }
  return map[s] || s
}
function statusBadge(s) {
  return {
    PENDING_PAYMENT:'text-bg-secondary',
    PendingConfirmation:'text-bg-secondary',
    CONFIRMED:'text-bg-info',
    PROCESSING:'text-bg-warning',
    SHIPPED:'text-bg-primary',
    DELIVERED:'text-bg-success',
    COMPLETED:'text-bg-success',
    CANCELLED:'text-bg-danger'
  }[s] || 'text-bg-light'
}
function mul(a, b) { return Number(a || 0) * Number(b || 0) }

// list
async function fetchOrders() {
  const userId = getUserId()
  if (!userId) { error.value = 'Không xác định được người dùng. Vui lòng đăng nhập.'; return }
  loading.value = true; error.value = ''
  try {
    const params = { userId, page: page.value, size: pageSize.value }
    if (selectedStatus.value) params.status = selectedStatus.value
    const { data } = await axios.get(`${API_BASE}/orders/user`, { params })
    orders.value = data?.content || []
    totalPages.value = data?.totalPages || 0
    totalElements.value = data?.totalElements || 0
  } catch (e) {
    error.value = e?.response?.data?.message || 'Không tải được danh sách đơn hàng.'
  } finally { loading.value = false }
}
function goToPage(p) {
  if (p < 0) p = 0
  if (totalPages.value && p >= totalPages.value) p = totalPages.value - 1
  page.value = p; fetchOrders()
}
function onFilterChange() { page.value = 0; fetchOrders() }

// detail
async function viewDetail(orderId) {
  const email = getUserEmail()
  if (!email) { error.value = 'Không xác định được email người dùng.'; return }
  try {
    const { data } = await axios.get(`${API_BASE}/orders/${orderId}`, { params: { email } })
    detail.value = data
  } catch (e) {
    error.value = e?.response?.data?.message || 'Không tải được chi tiết đơn hàng.'
  }
}

// actions
async function cancelOrder(id) {
  const email = getUserEmail()
  if (!email) return alert('Thiếu email người dùng')
  try {
    await axios.post(`${API_BASE}/orders/${id}/cancel`, null, { params: { email } })
    await viewDetail(id); await fetchOrders()
    alert('Huỷ đơn thành công')
  } catch (e) {
    alert(e?.response?.data || 'Huỷ đơn thất bại')
  }
}
async function markReceived(id) {
  const email = getUserEmail()
  if (!email) return alert('Thiếu email người dùng.')
  if (!confirm('Xác nhận đã nhận hàng?')) return
  try {
    await axios.post(`${API_BASE}/orders/${id}/received`, null, { params: { email } })
    await viewDetail(id)
    await fetchOrders()
    alert('Đơn hàng đã hoàn tất.')
  } catch (e) {
    alert(e?.response?.data || 'Thao tác thất bại.')
  }
}


onMounted(fetchOrders)
</script>

<style scoped>
.table { font-size: 0.95rem; }
.badge { font-weight: 500; }
</style>
