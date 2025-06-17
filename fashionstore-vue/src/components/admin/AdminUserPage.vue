<template>
  <div class="p-4">
    <h2 class="mb-3">👤 Quản lý người dùng</h2>

    <!-- ✅ FORM CHỈNH SỬA NGƯỜI DÙNG -->
    <div v-if="editingUser" class="card mx-auto mb-4" style="max-width: 600px;">
      <div class="card-header bg-warning text-dark">
        <h5 class="mb-0">✏️ Chỉnh sửa người dùng</h5>
      </div>
      <div class="card-body">
        <form @submit.prevent="updateUser">
          <div class="mb-3">
            <label class="form-label">Tên đăng nhập</label>
            <input v-model="editingUser.username" type="text" class="form-control" disabled />
          </div>
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input v-model="editingUser.email" type="email" class="form-control" />
          </div>
          <div class="mb-3">
            <label class="form-label">Họ tên</label>
            <input v-model="editingUser.fullName" type="text" class="form-control" />
          </div>
          <div class="mb-3">
            <label class="form-label">Quyền</label>
            <select v-model="editingUser.role" class="form-select">
              <option value="ADMIN">ADMIN</option>
              <option value="CUSTOMER">CUSTOMER</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">Trạng thái</label>
            <select v-model="editingUser.status" class="form-select">
              <option :value="true">Hoạt động</option>
              <option :value="false">Vô hiệu hóa</option>
            </select>
          </div>
          <div class="text-end">
            <button type="submit" class="btn btn-success me-2">Lưu</button>
            <button @click="cancelEdit" type="button" class="btn btn-secondary">Hủy</button>
          </div>
        </form>
      </div>
    </div>

    <!-- ✅ DANH SÁCH NGƯỜI DÙNG -->
    <table class="table table-striped table-bordered">
      <thead class="table-light">
        <tr>
          <th>ID</th>
          <th>Tên đăng nhập</th>
          <th>Họ tên</th>
          <th>Email</th>
          <th>Quyền</th>
          <th>Trạng thái</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.fullName }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td>{{ user.status ? 'Hoạt động' : 'Vô hiệu hóa' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-1" @click="editUser(user)">Sửa</button>
            <button class="btn btn-danger btn-sm" @click="deleteUser(user.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="error" class="alert alert-danger mt-3">{{ error }}</div>
    <div v-if="success" class="alert alert-success mt-3">{{ success }}</div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AdminUserPage',
  data() {
    return {
      users: [],
      editingUser: null,
      error: '',
      success: ''
    }
  },
  methods: {
    fetchUsers() {
      axios.get('http://localhost:8080/users')
        .then(res => {
          this.users = res.data
        })
        .catch(() => {
          this.error = 'Lỗi khi tải danh sách người dùng.'
        })
    },
    editUser(user) {
      this.editingUser = { ...user }
      this.success = ''
      this.error = ''
    },
    cancelEdit() {
      this.editingUser = null
    },
    updateUser() {
      axios.put(`http://localhost:8080/users/${this.editingUser.id}`, this.editingUser)
        .then(() => {
          this.success = 'Cập nhật thành công!'
          this.fetchUsers()
          this.editingUser = null
        })
        .catch(() => {
          this.error = 'Cập nhật thất bại!'
        })
    },
    deleteUser(id) {
      if (confirm('Bạn chắc chắn muốn xóa người dùng này?')) {
        axios.delete(`http://localhost:8080/users/${id}`)
          .then(() => {
            this.success = 'Xóa thành công!'
            this.fetchUsers()
          })
          .catch(() => {
            this.error = 'Xóa thất bại!'
          })
      }
    }
  },
  mounted() {
    this.fetchUsers()
  }
}
</script>
