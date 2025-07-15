import { createRouter, createWebHistory } from "vue-router";
<<<<<<< Updated upstream
=======

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
>>>>>>> Stashed changes

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
import AdminSupportPage from "@/components/admin/AdminSupportPage.vue";
import AdminUiPage from "@/components/admin/AdminUiPage.vue";
import AdminInventoryPage from "@/components/admin/AdminInventoryPage.vue";
import AdminUserPage from "@/components/admin/AdminUserPage.vue";
import AdminCouponPage from "@/components/admin/AdminCouponPage.vue"
import AdminShippingProvider from "@/components/admin/AdminShippingProvider.vue";
import SupplierPage from "@/components/admin/AdminSupplierPage.vue";
import AdminImportInvoicePage from "@/components/admin/AdminImportInvoicePage.vue";
import ProfilePage from "@/components/views/ProfilePage.vue";
import ChangePassword from "@/components/views/ChangePassword.vue";
import AdminInventoryAdjustmentPage from "@/components/admin/AdminInventoryAdjustmentPage.vue";
import AdminImportInvoiceDetail from "@/components/admin/AdminImportInvoiceDetail.vue";
const routes = [
  { path: "/", name: "Home", component: HomePage },
  { path: "/product", name: "Product", component: Product },
  { path: "/login", name: "Login", component: LoginView },
  { path: "/register", name: "Register", component: RegisterView },
  { path: "/cart", name: "Cart", component: Cart },
  { path: "/product/:id", name: "ProductDetail", component: ProductDetail },
<<<<<<< Updated upstream
  { path: "/profile", name: "Profile", component: ProfilePage },
  { path: "/change-password", name: "ChangePassword", component: ChangePassword },
=======
>>>>>>> Stashed changes
  {
    path: "/admin",
    component: AdminDashBoard,
    children: [
      {
<<<<<<< Updated upstream
        path: "",
=======
        path: "", 
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        component: AdminShippingProvider,
      },
      {
        path: "supplier",
        component: SupplierPage,
=======
        component: AdminShippingPage,
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        path: "invoicedetails",
        component: AdminImportInvoiceDetail,
      },
      {
=======
>>>>>>> Stashed changes
        path: "inventory",
        component: AdminInventoryPage,
      },
      {
<<<<<<< Updated upstream
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
=======
        path: "users",
        component: AdminUserPage,
      },
>>>>>>> Stashed changes
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

<<<<<<< Updated upstream

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
=======
export default router;
>>>>>>> Stashed changes
