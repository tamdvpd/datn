<template>
  <div class="p-4">
    <h2 class="text-center mb-4">👤 Quản lý người dùng</h2>
    <p class="text-center text-muted mb-4">Thông tin tài khoản người dùng và phân quyền.</p>

    <!-- Bảng người dùng -->
    <div class="table-responsive">
      <table class="table table-hover table-bordered text-center align-middle">
        <thead class="table-light">
          <tr>
            <th>ID</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th>Vai trò</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
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
              <button
                class="btn btn-sm btn-outline-secondary me-2"
                @click="toggleStatus(user)"
              >
                {{ user.status ? 'Khóa' : 'Mở khóa' }}
              </button>
              <button
                class="btn btn-sm btn-outline-warning"
                @click="showEdit(user)"
              >
                ✏️ Sửa
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Form chỉnh sửa -->
    <transition name="fade-slide">
      <div
        v-if="showEditForm"
        class="card shadow-sm mx-auto mt-4"
        style="max-width: 800px;"
      >
        <div class="card-header bg-warning text-dark">
          <h5 class="mb-0">
            <i class="bi bi-pencil-square me-2"></i>Cập nhật thông tin người dùng
          </h5>
        </div>
        <div class="card-body">
          <form @submit.prevent="updateUser">
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Họ tên</label>
                <input
                  v-model="editUser.fullName"
                  type="text"
                  class="form-control"
                  required
                />
              </div>
              <div class="col-md-6">
                <label class="form-label">Email</label>
                <input
                  v-model="editUser.email"
                  type="email"
                  class="form-control"
                  disabled
                />
              </div>
            </div>
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label class="form-label">Vai trò</label>
                <select v-model="editUser.role" class="form-select">
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                  <option value="STAFF">STAFF</option>
                </select>
              </div>
              <div class="col-md-6">
                <label class="form-label">Số điện thoại</label>
                <input
                  v-model="editUser.phoneNumber"
                  type="text"
                  class="form-control"
                />
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Địa chỉ</label>
              <input
                v-model="editUser.address"
                type="text"
                class="form-control"
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Trạng thái hoạt động</label>
              <select v-model="editUser.status" class="form-select">
                <option :value="true">Hoạt động</option>
                <option :value="false">Khóa</option>
              </select>
            </div>
            <div class="text-end">
              <button type="submit" class="btn btn-success me-2">
                <i class="bi bi-check-circle me-1"></i> Cập nhật
              </button>
              <button
                @click="showEditForm = false"
                type="button"
                class="btn btn-secondary"
              >
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

export default {
  name: 'AdminUserPage',
  data() {
    return {
      users: [],
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
    this.fetchUsers();
  },
  methods: {
    fetchUsers() {
      axios.get('http://localhost:8080/users')
        .then(response => {
          this.users = response.data;
        })
        .catch(error => {
          console.error('Lỗi khi lấy danh sách người dùng:', error);
        });
    },
    toggleStatus(user) {
      const newStatus = !user.status;
      axios.put(`http://localhost:8080/users/${user.id}/update`, {
        status: newStatus
      })
      .then(() => {
        alert('Cập nhật trạng thái thành công');
        this.fetchUsers();
      })
      .catch(error => {
        console.error('Lỗi khi cập nhật trạng thái:', error);
      });
    },
    showEdit(user) {
      this.editUser = { ...user };
      this.showEditForm = true;
    },
    updateUser() {
      axios.put(`http://localhost:8080/users/${this.editUser.id}/update`, {
        fullName: this.editUser.fullName,
        role: this.editUser.role,
        phoneNumber: this.editUser.phoneNumber,
        address: this.editUser.address,
        status: this.editUser.status
      })
      .then(() => {
        alert('Cập nhật thành công');
        this.showEditForm = false;
        this.fetchUsers();
      })
      .catch(error => {
        console.error('Lỗi khi cập nhật:', error);
      });
    }
  }
};
</script>

<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.4s ease;
}
.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
