<template>
  <div class="max-w-md mx-auto p-4 border rounded shadow">
    <h2 class="text-xl font-semibold mb-4">Write a Review</h2>

    <form @submit.prevent="submitReview">
      <div class="mb-3">
        <label>Rating (1-5):</label>
        <input type="number" v-model="form.rating" min="1" max="5" required class="border w-full p-1" />
      </div>

      <div class="mb-3">
        <label>Comment:</label>
        <textarea v-model="form.comment" class="border w-full p-1" rows="3"></textarea>
      </div>

      <div class="mb-3">
        <label>Select Images:</label>
        <input type="file" multiple @change="handleFileChange" />
      </div>

      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        Submit Review
      </button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    productDetailId: Number,
    userId: Number
  },
  data() {
    return {
      form: {
        rating: 5,
        comment: '',
      },
      selectedFiles: []
    };
  },
  methods: {
    handleFileChange(event) {
      this.selectedFiles = Array.from(event.target.files);
    },
    async submitReview() {
      try {
        const formData = new FormData();
        formData.append('rating', this.form.rating);
        formData.append('comment', this.form.comment);
        formData.append('productDetailId', this.productDetailId);
        formData.append('userId', this.userId);

        this.selectedFiles.forEach(file => {
          formData.append('images', file);
        });

        const response = await axios.post('http://localhost:8080/api/reviews/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });

        alert('Review submitted successfully!');
        this.form.rating = 5;
        this.form.comment = '';
        this.selectedFiles = [];

      } catch (error) {
        console.error(error);
        alert('Error submitting review');
      }
    }
  }
};
</script>

<style scoped>
input,
textarea {
  display: block;
  width: 100%;
  margin-top: 4px;
}
</style>
