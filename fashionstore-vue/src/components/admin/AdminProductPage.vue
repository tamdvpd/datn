<template>
  <div>
    <h2 class="mb-4">Quản lý sản phẩm</h2>
    <button @click="showAddProductModal = true">Thêm sản phẩm</button>
    
    <table>
      <thead>
        <tr>
          <th>Tên sản phẩm</th>
          <th>Giá</th>
          <th>Tình trạng kho</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.name }}</td>
          <td>{{ product.price }}</td>
          <td>{{ product.stockQuantity }}</td>
          <td>
            <button @click="editProduct(product.id)">Chỉnh sửa</button>
            <button @click="deleteProduct(product.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>

    <modal v-if="showAddProductModal" @close="showAddProductModal = false">
      <template #header>
        <h3>Thêm Sản phẩm</h3>
      </template>
      <template #body>
        <form @submit.prevent="addProduct">
          <input v-model="newProduct.name" placeholder="Tên sản phẩm" required />
          <input v-model="newProduct.price" placeholder="Giá" required />
          <input v-model="newProduct.stockQuantity" placeholder="Tình trạng kho" required />
          <button type="submit">Thêm</button>
        </form>
      </template>
    </modal>
  </div>
</template>

<script>

export default {
  components: { Modal },
  data() {
    return {
      products: [],
      showAddProductModal: false,
      newProduct: {
        name: '',
        price: '',
        stockQuantity: '',
      },
    };
  },
  created() {
    this.fetchProducts(); // Lấy danh sách sản phẩm
  },
  methods: {
    fetchProducts() {
      fetch('/api/products')
        .then(response => response.json())
        .then(data => {
          this.products = data;
        })
        .catch(error => console.error('Error fetching products:', error));
    },
    addProduct() {
      fetch('/api/products', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.newProduct),
      })
      .then(response => {
        if (response.ok) {
          this.fetchProducts(); // Cập nhật danh sách
          this.showAddProductModal = false; // Đóng modal
          this.resetNewProduct(); // Reset form
        } else {
          // Xử lý lỗi nếu có
          console.error('Error adding product:', response.statusText);
        }
      })
      .catch(error => console.error('Error adding product:', error));
    },
    resetNewProduct() {
      this.newProduct = {
        name: '',
        price: '',
        stockQuantity: '',
      };
    },
    editProduct(id) {
      this.$router.push(`/admin/products/edit/${id}`);
    },
    deleteProduct(id) {
      fetch(`/api/products/${id}`, { method: 'DELETE' })
        .then(() => {
          this.fetchProducts();
        })
        .catch(error => console.error('Error deleting product:', error));
    },
  },
};
</script>

<style scoped>
/* Thêm CSS nếu cần */
</style>