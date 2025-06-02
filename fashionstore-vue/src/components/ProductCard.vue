<template>
  <div class="border p-3 bg-white shadow rounded-md text-center">
    <img :src="product.image" alt="product" class="w-full h-48 object-cover mb-2" />
    <h3 class="text-sm font-semibold">{{ product.name }}</h3>
    <p class="text-red-500 font-bold">{{ product.price }} VNĐ</p>
  </div>
</template>

<script>
export default {
  props: ["product"],
};
</script>
