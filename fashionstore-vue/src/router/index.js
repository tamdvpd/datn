import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../components/HomePage.vue';
import Product from '../components/Product.vue';
import LoginRegister from '../components/LoginRegister.vue';

const routes = [
  { path: '/', component: HomePage },
  { path: '/products', component: Product },
  { path: '/login', component: LoginRegister },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
