<template>
  <div v-if="product">
    <h1>{{ product.name }}</h1>
    <p>{{ product.description }}</p>
    <img :src="product.imageUrl" alt="Product image" />
    <p>Brand: {{ product.brand }}</p>
  </div>
  <div v-else>
    <p>Loading product details...</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      product: null,
    };
  },
  mounted() {
    const id = this.$route.params.id;
    axios.get(`http://localhost:8080/products/${id}`)
      .then((response) => {
        this.product = response.data;
      })
      .catch((error) => {
        console.error("Error loading product:", error);
      });
  },
};
</script>
