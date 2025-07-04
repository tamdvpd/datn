<template>
  <div>
    <h3>🚚 Quản lý Đơn vị vận chuyển</h3>

    <!-- FORM THÊM / CẬP NHẬT -->
    <div class="form-add">
      <input v-model="form.name" placeholder="Tên đơn vị vận chuyển" />
      <input v-model="form.code" placeholder="Mã (GHN, GHTK...)" />
      <input type="number" v-model="form.shippingFee" placeholder="Phí vận chuyển" />
      <textarea v-model="form.description" placeholder="Mô tả (tùy chọn)"></textarea>

      <button @click="save">💾 {{ form.id ? 'Cập nhật' : 'Thêm' }}</button>
      <button @click="resetForm">❌ Hủy</button>
    </div>

    <!-- DANH SÁCH -->
    <h4>📄 Danh sách đơn vị vận chuyển</h4>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên</th>
          <th>Mã</th>
          <th>Phí</th>
          <th>Mô tả</th>
          <th>Sửa</th>
          <th>Xóa</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in providers" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.code }}</td>
          <td>{{ item.shippingFee }}đ</td>
          <td>{{ item.description }}</td>
          <td><button @click="edit(item)">📝</button></td>
          <td><button @click="remove(item.id)">🗑️</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      providers: [],
      form: {
        id: null,
        name: '',
        code: '',
        description: '',
        shippingFee: 0
      }
    };
  },
  methods: {
    loadData() {
      axios.get('/shipping-providers').then((res) => {
        this.providers = res.data;
      });
    },
    save() {
      if (!this.form.name || !this.form.code || !this.form.shippingFee) {
        alert('Vui lòng nhập đầy đủ tên, mã, và phí vận chuyển.');
        return;
      }

      const api = this.form.id
        ? axios.put(`/shipping-providers/${this.form.id}`, this.form)
        : axios.post('/shipping-providers', this.form);

      api.then(() => {
        this.loadData();
        this.resetForm();
      });
    },
    edit(item) {
      this.form = { ...item };
    },
    resetForm() {
      this.form = {
        id: null,
        name: '',
        code: '',
        description: '',
        shippingFee: 0
      };
    },
    remove(id) {
      if (confirm('Bạn có chắc chắn muốn xóa?')) {
        axios.delete(`/shipping-providers/${id}`).then(() => {
          this.loadData();
        });
      }
    }
  },
  mounted() {
    this.loadData();
  }
};
</script>

<style scoped>
input,
textarea {
  display: block;
  margin: 5px 0;
  padding: 5px;
  width: 300px;
}
button {
  margin-right: 5px;
}
table {
  margin-top: 15px;
  border-collapse: collapse;
  width: 100%;
}
th,
td {
  border: 1px solid #ccc;
  padding: 8px;
}
</style>
