import { createRouter, createWebHistory } from "vue-router";

import CategoryPage from "@/components/admin/AdminCategoryPage.vue";
import AdminCouponPage from "@/components/admin/AdminCouponPage.vue";
import AdminDashBoard from "@/components/admin/AdminDashBoard.vue";
import AdminHomePage from '@/components/admin/AdminHomePage.vue';
import AdminImportInvoiceDetail from "@/components/admin/AdminImportInvoiceDetail.vue";
import AdminImportInvoicePage from "@/components/admin/AdminImportInvoicePage.vue";
import AdminInventoryAdjustmentPage from "@/components/admin/AdminInventoryAdjustmentPage.vue";
import AdminInventoryPage from "@/components/admin/AdminInventoryPage.vue";
import OrderManager from '@/components/admin/AdminOrderPage.vue';
import PaymentPage from "@/components/admin/AdminPaymentPage.vue";
import ProductManager from '@/components/admin/AdminProductPage.vue';
import ReportPage from '@/components/admin/AdminReportPage.vue';
import AdminShippingProvider from "@/components/admin/AdminShippingProvider.vue";
import SupplierPage from "@/components/admin/AdminSupplierPage.vue";
import AdminSupportPage from "@/components/admin/AdminSupportPage.vue";
import AdminUiPage from "@/components/admin/AdminUiPage.vue";
import AdminUserPage from "@/components/admin/AdminUserPage.vue";
import Cart from "@/components/views/Cart.vue";
import ChangePassword from "@/components/views/ChangePassword.vue";
import HomePage from "@/components/views/HomePage.vue";
import LoginView from "@/components/views/LoginView.vue";
import Product from "@/components/views/Product.vue";
import ProductDetail from "@/components/views/ProductDetail.vue";
import ProfilePage from "@/components/views/ProfilePage.vue";
import RegisterView from "@/components/views/RegisterView.vue";
import Order from "@/components/views/Order.vue";
const routes = [
  { path: "/", name: "Home", component: HomePage },
  { path: "/product", name: "Product", component: Product },
  { path: "/login", name: "Login", component: LoginView },
  { path: "/register", name: "Register", component: RegisterView },
  { path: "/cart", name: "Cart", component: Cart },
  { path: "/products/:id", name: "ProductDetail", component: ProductDetail },
  { path: "/profile", name: "Profile", component: ProfilePage },
  { path: "/change-password", name: "ChangePassword", component: ChangePassword },
  {path: "/order", name: "Order", component: Order },
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
        path: "shipping",
        component: AdminShippingProvider,
      },
      {
        path: "supplier",
        component: SupplierPage,
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
        path: "invoicedetails",
        component: AdminImportInvoiceDetail,
      },
      {
        path: "inventory",
        component: AdminInventoryPage,
      },
      {
        path: "invoice",
        component: AdminImportInvoicePage,
      },
      {
        path: "users",
        component: AdminUserPage,
      },
      {
        path: "inventory_adjustment",
        component: AdminInventoryAdjustmentPage,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});


router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user'));

  if (to.path.startsWith('/admin')) {
    if (user && user.role === 'ADMIN') {
      next();
    } else {
      alert('Bạn không có quyền truy cập trang quản trị!');
      next('/');
    }
  } else {
    next();
  }
});

export default router;