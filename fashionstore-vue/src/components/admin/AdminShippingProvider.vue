<template>
  <div>
    <h2>🚚 Danh Sách Đơn Vị Vận Chuyển</h2>

    <!-- FORM HIỂN THỊ THÊM / SỬA / XOÁ ĐƠN VỊ VẬN CHUYỂN -->
    <div class="form-section">
      <h3>{{ form.id ? "✏️ Chỉnh sửa" : "➕ Thêm" }} Đơn Vị Vận Chuyển</h3>
      <input v-model="form.name" placeholder="Tên đơn vị" />
     <input v-model="form.code" placeholder="Mã (GHN...)" maxlength="10" />
      <input
      type="number"
      v-model.number="form.shippingFee"
      min="1000"
      max="100000000"
      placeholder="Phí vận chuyển (≥ 1.000đ, ≤ 100.000.000đ)"
    />

      <textarea v-model="form.description" placeholder="Mô tả"></textarea>
      <select v-model="form.status">
        <option :value="true">Hoạt động</option>
        <option :value="false">Ngưng hoạt động</option>
      </select>
      <br />
      <button @click="save">{{ form.id ? "💾 Cập nhật" : "✅ Thêm mới" }}</button>
      <button @click="resetForm">❌ Hủy</button>
    </div>

    <!-- BẢNG DANH SÁCH -->
    <table border="1" cellspacing="0" cellpadding="8">
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên</th>
          <th>Mã</th>
          <th>Mô tả</th>
          <th>Phí</th>
          <th>Trạng thái</th>
          <th>Ngày tạo</th>
          <th>Cập nhật</th>
          <th>Sửa</th>
          <th>Xóa</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="provider in providers" :key="provider.id">
          <td>{{ provider.id }}</td>
          <td>{{ provider.name }}</td>
          <td>{{ provider.code }}</td>
          <td>{{ provider.description }}</td>
          <td>{{ provider.shippingFee }} đ</td>
          <td :style="{ color: provider.status ? 'green' : 'red' }">
            {{ provider.status ? 'Hoạt động' : 'Ngưng hoạt động' }}
          </td>
          <td>{{ formatDate(provider.createdAt) }}</td>
          <td>{{ formatDate(provider.updatedAt) }}</td>
          <td><button @click="edit(provider)">📝</button></td>
          <td><button @click="remove(provider.id)">🗑️</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AdminShippingProvider",
  data() {
    return {
      providers: [],
      form: {
        id: null,
        name: "",
        code: "",
        description: "",
        shippingFee: null,
        status: true
      }
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    // Tải danh sách đơn vị vận chuyển
    loadData() {
      axios
        .get("http://localhost:8080/shipping-providers")
        .then((res) => (this.providers = res.data))
        .catch((err) =>
          console.error("Lỗi khi tải danh sách đơn vị vận chuyển:", err)
        );
    },

    // Lưu hoặc cập nhật
  save() {
  const { id, name, code, description, shippingFee, status } = this.form;

  // Kiểm tra các trường bắt buộc
  if (!name || !code || shippingFee === null || shippingFee === "") {
    alert("Vui lòng nhập đầy đủ Tên, Mã và Phí vận chuyển.");
    return;
  }

  // Kiểm tra giới hạn phí vận chuyển
  if (shippingFee < 1000) {
    alert("Phí vận chuyển phải từ 1.000đ trở lên.");
    return;
  }
  if (shippingFee > 100000000) {
    alert("Phí vận chuyển không được vượt quá 100.000.000đ.");
    return;
  }

  const payload = { name, code, description, shippingFee, status };

  const req = id
    ? axios.put(`http://localhost:8080/shipping-providers/${id}`, { id, ...payload })
    : axios.post("http://localhost:8080/shipping-providers", payload);

  req
    .then(() => {
      this.loadData();
      this.resetForm();
    })
    .catch((err) => {
      console.error("Lỗi khi lưu:", err);
      alert("Lỗi khi lưu: " + (err.response?.data?.message || err.message));
    });
},


    // Chỉnh sửa
    edit(item) {
      this.form = { ...item };
    },

    // Reset form
    resetForm() {
      this.form = {
        id: null,
        name: "",
        code: "",
        description: "",
        shippingFee: null,
        status: true
      };
    },

    // Xóa
    remove(id) {
      if (confirm("Bạn có chắc chắn muốn xóa không?")) {
        axios
          .delete(`http://localhost:8080/shipping-providers/${id}`)
          .then(() => this.loadData())
          .catch((err) => alert("Lỗi xóa: " + err.message));
      }
    },

    // Định dạng ngày
    formatDate(dateString) {
      if (!dateString) return "";
      return new Date(dateString).toLocaleString("vi-VN", {
        hour: "2-digit",
        minute: "2-digit",
        day: "2-digit",
        month: "2-digit",
        year: "numeric"
      });
    }
  }
};
</script>

<style scoped>
input,
textarea,
select {
  display: block;
  margin: 5px 0;
  padding: 6px;
  width: 550px;
}
button {
  margin-right: 5px;
  margin-top: 5px;
}
.form-section {
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 20px;
  width: 700px;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th {
  background-color: #f9f9f9;
}
td,
th {
  padding: 6px 10px;
  text-align: left;
}
</style>
