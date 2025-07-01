<template>
  <div class="p-4">
    <h2>👤 Quản lý người dùng</h2>
    <p>Thông tin tài khoản người dùng và phân quyền.</p>

    <table border="1" cellpadding="8" cellspacing="0" style="margin-top: 16px; width: 100%;">
      <thead>
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
          <td>{{ user.role }}</td>
          <td>
            <span v-if="user.status">✅ Hoạt động</span>
            <span v-else>⛔ Khoá</span>
          </td>
          <td>
            <button @click="toggleStatus(user)">
              {{ user.status ? 'Khóa' : 'Mở khóa' }}
            </button>
            <button @click="showEdit(user)">✏️ Sửa</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="showEditForm" class="card shadow-sm mx-auto mt-4" style="max-width: 800px;">
      <div class="card-header bg-warning text-dark">
        <h5 class="mb-0"><i class="bi bi-pencil-square me-2"></i>Cập nhật thông tin người dùng</h5>
      </div>
      <div class="card-body">
        <form @submit.prevent="updateUser">
          <div class="mb-3">
            <label class="form-label">Họ tên</label>
            <input v-model="editUser.fullName" type="text" class="form-control" required />
          </div>
          <div class="mb-3">
            <label class="form-label">Vai trò</label>
            <select v-model="editUser.role" class="form-control">
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
              <option value="STAFF">STAFF</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">Số điện thoại</label>
            <input v-model="editUser.phoneNumber" type="text" class="form-control" />
          </div>
          <div class="text-end">
            <button type="submit" class="btn btn-success me-2">
              <i class="bi bi-check-circle me-1"></i> Cập nhật
            </button>
            <button @click="showEditForm = false" type="button" class="btn btn-secondary">
              <i class="bi bi-x-circle me-1"></i> Hủy
            </button>
          </div>
        </form>
      </div>
    </div>
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
        role: 'USER',
        phoneNumber: ''
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
      axios.put(`http://localhost:8080/users/${this.editUser.id}/update`, this.editUser)
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
table {
  border-collapse: collapse;
}
th {
  background-color: #f0f0f0;
}
button {
  margin: 0 4px;
  cursor: pointer;
}
</style>
