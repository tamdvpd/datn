<template>
  <div class="bg-gray-100 min-h-screen">
    <Header />
    <main class="px-6 py-6">
      <h1 class="text-xl font-bold mb-4">SẢN PHẨM</h1>
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        <ProductCard v-for="(p, index) in products" :key="index" :product="p" />
      </div>
    </main>
    <Footer />
  </div>
</template>

<script>
import Header from "../components/Header.vue";
import Footer from "../components/Footer.vue";
import ProductCard from "../components/ProductCard.vue";

export default {
  components: { Header, Footer, ProductCard },
  data() {
    return {
      products: [
        { name: "Áo sơ mi dài tay trơn GATB011", price: "550,000", image: "link1.jpg" },
        {
          name: "Áo polo trơn có thêu logo ngực GSTP066",
          price: "342,250",
          image: "link2.jpg",
        },
        { name: "Quần kaki dài basic GABK021", price: "450,000", image: "link3.jpg" },
        {
          name: "Áo polo kẻ ngang bo tay phối màu GSTP039",
          price: "299,000",
          image: "link4.jpg",
        },
        { name: "Kính Chống Tia UV | Gọng Vuông", price: "130,000", image: "link5.jpg" },
        { name: "Mũ Lưỡi Trai / Nón Chống Tia UV", price: "90,000", image: "link6.jpg" },
      ],
    };
  },
};
</script>
