<template>
  <div class="p-4">
    <h2 class="text-center mb-2">👤 Quản lý người dùng</h2>
    <p class="text-center text-muted mb-4">Thông tin tài khoản người dùng và phân quyền.</p>

    <!-- Bộ lọc & tạo mới -->
    <div class="row g-2 align-items-end mb-3">
      <div class="col-md-3">
        <label class="form-label">Tìm kiếm (tên/email)</label>
        <input
          v-model.trim="q"
          class="form-control"
          placeholder="Nhập từ khóa..."
          @keyup.enter="page=0; fetchUsersPage()"
        />
      </div>
      <div class="col-md-2">
        <label class="form-label">Vai trò</label>
        <select v-model="filterRole" class="form-select">
          <option value="">Tất cả</option>
          <option value="USER">USER</option>
          <option value="ADMIN">ADMIN</option>
          <!-- Nếu BE cho phép: <option value="STAFF">STAFF</option> -->
        </select>
      </div>
      <div class="col-md-2">
        <label class="form-label">Trạng thái</label>
        <select v-model="filterStatus" class="form-select">
          <option value="">Tất cả</option>
          <option value="true">Hoạt động</option>
          <option value="false">Khóa</option>
        </select>
      </div>
      <div class="col-md-2">
        <label class="form-label">Kích thước trang</label>
        <select v-model.number="size" class="form-select">
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select>
      </div>
      <div class="col-md-3 text-end">
        <button class="btn btn-primary me-2" @click="page=0; fetchUsersPage()">Lọc</button>
        <button class="btn btn-success" @click="showCreateForm = true">+ Thêm tài khoản</button>
      </div>
    </div>

    <!-- Bảng người dùng -->
    <div class="table-responsive">
      <table class="table table-hover table-bordered text-center align-middle">
        <thead class="table-light">
          <tr>
            <th style="width:90px">ID</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th style="width:120px">Vai trò</th>
            <th style="width:120px">Trạng thái</th>
            <th style="width:200px">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.fullName }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span class="badge bg-info text-dark">{{ user.role }}</span>
            </td>
            <td>
              <span :class="user.status ? 'badge bg-success' : 'badge bg-danger'">
                {{ user.status ? 'Hoạt động' : 'Khóa' }}
              </span>
            </td>
            <td>
              <button class="btn btn-sm btn-outline-secondary me-2" @click="toggleStatus(user)">
                {{ user.status ? 'Khóa' : 'Mở khóa' }}
              </button>
              <button class="btn btn-sm btn-outline-warning" @click="showEdit(user)">
                ✏️ Sửa
              </button>
            </td>
          </tr>
          <tr v-if="!loading && users.length === 0">
            <td colspan="6" class="text-muted">Không có người dùng.</td>
          </tr>
          <tr v-if="loading">
            <td colspan="6" class="text-muted">Đang tải...</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <nav v-if="totalPages > 1" class="mt-3">
      <ul class="pagination justify-content-center">
        <li :class="['page-item', {disabled: page === 0}]">
          <button class="page-link" @click="goTo(page - 1)">«</button>
        </li>
        <li v-for="p in totalPages" :key="p" :class="['page-item', {active: p-1 === page}]">
          <button class="page-link" @click="goTo(p-1)">{{ p }}</button>
        </li>
        <li :class="['page-item', {disabled: page >= totalPages-1}]">
          <button class="page-link" @click="goTo(page + 1)">»</button>
        </li>
      </ul>
    </nav>

    <!-- Form tạo mới (Admin create, không OTP) -->
    <transition name="fade-slide">
      <div v-if="showCreateForm" class="card shadow-sm mx-auto mt-4" style="max-width: 800px;">
        <div class="card-header bg-primary text-white">
          <h5 class="mb-0">➕ Tạo tài khoản (Admin)</h5>
        </div>
        <div class="card-body">
          <form @submit.prevent="createUser">
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Email *</label>
                <input v-model.trim="createForm.email" type="email" class="form-control" required />
              </div>
              <div class="col-md-6">
                <label class="form-label">Mật khẩu *</label>
                <input v-model="createForm.password" type="password" class="form-control" required minlength="6" />
              </div>
            </div>
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Họ tên</label>
                <input v-model.trim="createForm.fullName" type="text" class="form-control" />
              </div>
              <div class="col-md-3">
                <label class="form-label">Vai trò</label>
                <select v-model="createForm.role" class="form-select">
                  <option value="ADMIN">ADMIN</option>
                  <option value="USER">USER</option>
                  <!-- Thêm STAFF nếu BE cho phép -->
                </select>
              </div>
              <div class="col-md-3">
                <label class="form-label">Trạng thái</label>
                <select v-model="createForm.status" class="form-select">
                  <option :value="true">Hoạt động</option>
                  <option :value="false">Khóa</option>
                </select>
              </div>
            </div>
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Số điện thoại</label>
                <input v-model.trim="createForm.phoneNumber" type="text" class="form-control" />
              </div>
              <div class="col-md-6">
                <label class="form-label">Địa chỉ</label>
                <input v-model.trim="createForm.address" type="text" class="form-control" />
              </div>
            </div>

            <div class="text-end">
              <button type="submit" class="btn btn-success me-2" :disabled="saving">
                {{ saving ? 'Đang tạo...' : 'Tạo' }}
              </button>
              <button type="button" class="btn btn-secondary" @click="showCreateForm=false">Hủy</button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- Form chỉnh sửa -->
    <transition name="fade-slide">
      <div v-if="showEditForm" class="card shadow-sm mx-auto mt-4" style="max-width: 800px;">
        <div class="card-header bg-warning text-dark">
          <h5 class="mb-0"><i class="bi bi-pencil-square me-2"></i>Cập nhật thông tin người dùng</h5>
        </div>
        <div class="card-body">
          <form @submit.prevent="updateUser">
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Họ tên</label>
                <input v-model="editUser.fullName" type="text" class="form-control" required />
              </div>
              <div class="col-md-6">
                <label class="form-label">Email</label>
                <input v-model="editUser.email" type="email" class="form-control" disabled />
              </div>
            </div>
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Vai trò</label>
                <select v-model="editUser.role" class="form-select">
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                  <!-- Nếu BE cho phép: <option value="STAFF">STAFF</option> -->
                </select>
              </div>
              <div class="col-md-6">
                <label class="form-label">Số điện thoại</label>
                <input v-model="editUser.phoneNumber" type="text" class="form-control" />
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Địa chỉ</label>
              <input v-model="editUser.address" type="text" class="form-control" />
            </div>
            <div class="mb-3">
              <label class="form-label">Trạng thái hoạt động</label>
              <select v-model="editUser.status" class="form-select">
                <option :value="true">Hoạt động</option>
                <option :value="false">Khóa</option>
              </select>
            </div>
            <div class="text-end">
              <button type="submit" class="btn btn-success me-2" :disabled="saving">
                <i class="bi bi-check-circle me-1"></i> {{ saving ? 'Đang lưu...' : 'Cập nhật' }}
              </button>
              <button @click="showEditForm = false" type="button" class="btn btn-secondary">
                <i class="bi bi-x-circle me-1"></i> Hủy
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import axios from 'axios';

const API_BASE = import.meta?.env?.VITE_API_BASE || 'http://localhost:8080';
const api = axios.create({
  baseURL: API_BASE,
  withCredentials: false
});

// Gắn JWT nếu có (đổi key nếu bạn lưu khác)
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwt');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default {
  name: 'AdminUserPage',
  data() {
    return {
      // danh sách & tải
      users: [],
      loading: false,
      saving: false,

      // filter + paging
      q: '',
      filterRole: '',
      filterStatus: '',
      page: 0,
      size: 10,
      totalPages: 0,
      sort: 'id,asc',

      // tạo mới
      showCreateForm: false,
      createForm: {
        email: '',
        fullName: '',
        password: '',
        role: 'ADMIN',
        status: true,
        phoneNumber: '',
        address: ''
      },

      // sửa
      showEditForm: false,
      editUser: {
        id: null,
        fullName: '',
        email: '',
        role: 'USER',
        phoneNumber: '',
        address: '',
        status: true
      }
    };
  },
  created() {
    this.fetchUsersPage();
  },
  methods: {
    async fetchUsersPage() {
      this.loading = true;
      try {
        const params = {
          page: this.page,
          size: this.size,
          sort: this.sort,
          q: this.q || undefined,
          role: this.filterRole || undefined,
          status: this.filterStatus === '' ? undefined : this.filterStatus // 'true' | 'false'
        };
        // BE: GET /users/admin/page
        const res = await api.get('/users/admin/page', { params });
        this.users = res.data?.content || [];
        this.totalPages = res.data?.totalPages || 0;
        this.page = res.data?.number ?? 0;
      } catch (error) {
        console.error('Lỗi khi lấy danh sách người dùng:', error);
        alert('Không tải được danh sách người dùng.');
      } finally {
        this.loading = false;
      }
    },
    goTo(p) {
      if (p < 0 || p >= this.totalPages) return;
      this.page = p;
      this.fetchUsersPage();
    },

    async createUser() {
      try {
        if (!this.createForm.email || !this.createForm.password) {
          return alert('Vui lòng nhập email & mật khẩu');
        }
        // BE: POST /users/admin/create (không cần OTP)
        await api.post('/users/admin/create', this.createForm);
        alert('Tạo tài khoản thành công');
        this.showCreateForm = false;
        this.resetCreate();
        this.page = 0; // quay về trang đầu để thấy user mới
        await this.fetchUsersPage();
      } catch (error) {
        console.error('Lỗi tạo tài khoản:', error);
        const msg = error?.response?.data?.message || 'Tạo tài khoản thất bại';
        alert(msg);
      }
    },
    resetCreate() {
      this.createForm = {
        email: '',
        fullName: '',
        password: '',
        role: 'ADMIN',
        status: true,
        phoneNumber: '',
        address: ''
      };
    },

    async toggleStatus(user) {
      const newStatus = !user.status;
      try {
        await api.put(`/users/${user.id}/update`, { status: newStatus });
        user.status = newStatus; // cập nhật ngay
      } catch (error) {
        console.error('Lỗi khi cập nhật trạng thái:', error);
        alert('Cập nhật trạng thái thất bại.');
      }
    },

    showEdit(user) {
      this.editUser = {
        id: user.id,
        fullName: user.fullName || '',
        email: user.email || '',
        role: user.role || 'USER',
        phoneNumber: user.phoneNumber || '',
        address: user.address || '',
        status: Boolean(user.status)
      };
      this.showEditForm = true;
    },

    async updateUser() {
      this.saving = true;
      try {
        await api.put(`/users/${this.editUser.id}/update`, {
          fullName: this.editUser.fullName,
          role: this.editUser.role,
          phoneNumber: this.editUser.phoneNumber,
          address: this.editUser.address,
          status: this.editUser.status
        });
        alert('Cập nhật thành công');
        this.showEditForm = false;
        await this.fetchUsersPage();
      } catch (error) {
        console.error('Lỗi khi cập nhật:', error);
        const msg = error?.response?.data?.message || 'Cập nhật thất bại';
        alert(msg);
      } finally {
        this.saving = false;
      }
    }
  }
};
</script>

<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active { transition: all 0.4s ease; }
.fade-slide-enter-from,
.fade-slide-leave-to { opacity: 0; transform: translateY(-10px); }
</style>
