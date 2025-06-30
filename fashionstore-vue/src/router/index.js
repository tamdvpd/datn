import { createRouter, createWebHistory } from "vue-router";

import HomePage from "@/components/views/HomePage.vue";
import Product from "@/components/views/Product.vue";
import LoginView from "@/components/views/LoginView.vue";
import RegisterView from "@/components/views/RegisterView.vue";
import Cart from "@/components/views/Cart.vue";
import ProductDetail from "@/components/views/ProductDetail.vue";
import AdminDashBoard from "@/components/admin/AdminDashBoard.vue";
import AdminHomePage from '@/components/admin/AdminHomePage.vue';
import ProductManager from '@/components/admin/AdminProductPage.vue';
import OrderManager from '@/components/admin/AdminOrderPage.vue';
import ReportPage from '@/components/admin/AdminReportPage.vue';
import CategoryPage from "@/components/admin/AdminCategoryPage.vue";
import PaymentPage from "@/components/admin/AdminPaymentPage.vue";
import AdminShippingPage from "@/components/admin/AdminShippingPage.vue";
import AdminSupportPage from "@/components/admin/AdminSupportPage.vue";
import AdminUiPage from "@/components/admin/AdminUiPage.vue";
import AdminInventoryPage from "@/components/admin/AdminInventoryPage.vue";
import AdminUserPage from "@/components/admin/AdminUserPage.vue";
import AdminCouponPage from "@/components/admin/AdminCouponPage.vue"
import AdminShippingProvider from "@/components/admin/AdminShippingProvider.vue";

const routes = [
  { path: "/", name: "Home", component: HomePage },
  { path: "/product", name: "Product", component: Product },
  { path: "/login", name: "Login", component: LoginView },
  { path: "/register", name: "Register", component: RegisterView },
  { path: "/cart", name: "Cart", component: Cart },
  { path: "/product/:id", name: "ProductDetail", component: ProductDetail },
  {
    path: "/admin",
    component: AdminDashBoard,
    children: [
      {
        path: "",
        component: AdminHomePage,
      },
      {
        path: "products",
        component: ProductManager,
      },
      {
        path: "orders",
        component: OrderManager,
      },
      {
        path: "coupons",
        component: AdminCouponPage,
      },
      {
        path: "reports",
        component: ReportPage,
      },
      {
        path: "categories",
        component: CategoryPage,
      },
      {
        path: "payments",
        component: PaymentPage,
      },
      {
        path: "support",
        component: AdminSupportPage,
      },
      {
        path: "ui",
        component: AdminUiPage,
      },
      {
        path: "inventory",
        component: AdminInventoryPage,
      },
      {
        path: "users",
        component: AdminUserPage,
      },
      {
        path: "shipping",
        component: AdminShippingProvider,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;