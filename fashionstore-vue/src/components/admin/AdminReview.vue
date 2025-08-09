<template>
  <div class="p-6 bg-white rounded-xl shadow-lg max-w-[1400px] w-full mx-auto">
    <div class="flex justify-between items-center mb-6 border-b pb-2">
      <h2 class="text-2xl font-bold text-gray-800">📝 Quản lý đánh giá sản phẩm</h2>
    </div>

    <!-- Table -->
    <div v-if="reviews.length > 0" class="overflow-x-auto">
      <table class="min-w-full bg-white border border-gray-200">
        <thead class="bg-gray-100">
          <tr>
            <th class="py-2 px-4 border text-left">#</th>
            <th class="py-2 px-4 border text-left">Người dùng</th>
            <th class="py-2 px-4 border text-left">Sản phẩm</th>
            <th class="py-2 px-4 border text-left">Số sao</th>
            <th class="py-2 px-4 border text-left">Bình luận</th>
            <th class="py-2 px-4 border text-left">Ảnh</th>
            <th class="py-2 px-4 border text-left">Ngày tạo</th>
            <th class="py-2 px-4 border text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(review, index) in reviews" :key="review.id" class="hover:bg-gray-50">
            <td class="py-2 px-4 border">{{ index + 1 }}</td>
            <td class="py-2 px-4 border">{{ review.user?.fullName }}</td>
            <td class="py-2 px-4 border">{{ review.productDetail?.product?.name }} - {{ review.productDetail?.color }} / {{ review.productDetail?.size }}</td>
            <td class="py-2 px-4 border">
              <span v-for="i in 5" :key="i">
                <span v-if="i <= review.rating">⭐</span>
                <span v-else>☆</span>
              </span>
            </td>
            <td class="py-2 px-4 border max-w-[200px] truncate" :title="review.comment">{{ review.comment }}</td>
            <td class="py-2 px-4 border">
              <div class="flex space-x-2">
                <img
                  v-for="img in review.images"
                  :key="img.id"
                  :src="img.url"
                  alt="Hình"
                  class="w-10 h-10 object-cover rounded"
                />
              </div>
            </td>
            <td class="py-2 px-4 border">{{ formatDate(review.createdAt) }}</td>
            <td class="py-2 px-4 border text-center">
              <button @click="deleteReview(review.id)" class="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded">
                Xóa
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else class="text-gray-500 text-center mt-10">
      Không có đánh giá nào.
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "AdminReview",
  data() {
    return {
      reviews: []
    };
  },
  mounted() {
    this.fetchReviews();
  },
  methods: {
    fetchReviews() {
      axios.get("http://localhost:8080/api/reviews")
        .then(res => {
          this.reviews = res.data;
        })
        .catch(err => {
          console.error("Lỗi khi lấy đánh giá:", err);
        });
    },
    deleteReview(id) {
      if (confirm("Bạn có chắc muốn xóa đánh giá này?")) {
        axios.delete(`http://localhost:8080/api/reviews/${id}`)
          .then(() => {
            this.fetchReviews();
            alert("Đã xóa thành công");
          })
          .catch(err => {
            console.error("Lỗi xóa:", err);
            alert("Xóa thất bại");
          });
      }
    },
    formatDate(dateTime) {
      const date = new Date(dateTime);
      return date.toLocaleString("vi-VN");
    }
  }
};
</script>

<style scoped>
table th,
table td {
  font-size: 14px;
}
</style>
