import { createRouter, createWebHistory } from "vue-router";

import CategoryPage from "@/components/admin/AdminCategoryPage.vue";
import AdminCouponPage from "@/components/admin/AdminCouponPage.vue";
import AdminDashBoard from "@/components/admin/AdminDashBoard.vue";
import AdminHomePage from "@/components/admin/AdminHomePage.vue";
import AdminImportInvoiceDetail from "@/components/admin/AdminImportInvoiceDetail.vue";
import AdminImportInvoicePage from "@/components/admin/AdminImportInvoicePage.vue";
import AdminInventoryAdjustmentPage from "@/components/admin/AdminInventoryAdjustmentPage.vue";
import AdminInventoryPage from "@/components/admin/AdminInventoryPage.vue";
import OrderManager from "@/components/admin/AdminOrderPage.vue";
import PaymentPage from "@/components/admin/AdminPaymentPage.vue";
import ProductManager from "@/components/admin/AdminProductPage.vue";
import ReportPage from "@/components/admin/AdminReportPage.vue";
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
import CheckOut from "@/components/views/CheckOut.vue";
import CartCheckout from "@/components/views/CartCheckout.vue";
import PaymentResult from "@/components/views/PaymentResult.vue";
import ForgotPassword from "@/components/views/ForgotPassword.vue";

// ⬅️ ADD: imports 6 trang chính sách
import ProductInfo from "@/components/views/policies/ProductInfo.vue";
import ReturnPolicy from "@/components/views/policies/ReturnPolicy.vue";
import PrivacyPolicy from "@/components/views/policies/PrivacyPolicy.vue";
import ShippingPolicy from "@/components/views/policies/ShippingPolicy.vue";
import PaymentPolicy from "@/components/views/policies/PaymentPolicy.vue";
import RefundPolicy from "@/components/views/policies/RefundPolicy.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: HomePage,
    meta: { title: "Trang chủ" },
  },
  {
    path: "/product",
    name: "Product",
    component: Product,
    meta: { title: "Sản phẩm" },
  },
  {
    path: "/login",
    name: "Login",
    component: LoginView,
    meta: { title: "Đăng nhập" },
  },
  {
    path: "/register",
    name: "Register",
    component: RegisterView,
    meta: { title: "Đăng ký" },
  },
  { path: "/cart", name: "Cart", component: Cart, meta: { title: "Giỏ hàng" } },
  { path: "/products/:id", name: "ProductDetail", component: ProductDetail },
  {
    path: "/profile",
    name: "Profile",
    component: ProfilePage,
    meta: { title: "Thông tin cá nhân" },
  },
  {
    path: "/change-password",
    name: "ChangePassword",
    component: ChangePassword,
    meta: { title: "Đổi mật khẩu" },
  },
  {
    path: "/order",
    name: "Order",
    component: Order,
    meta: { title: "Đơn hàng" },
  },
  {
    path: "/check-out",
    name: "CheckOut",
    component: CheckOut,
    meta: { title: "Thanh toán" },
  },
  { path: "/checkout-cart", name: "CartCheckout", component: CartCheckout },
  { path: "/payment-result", name: "PaymentResult", component: PaymentResult },
  {
    path: "/forgot-password",
    name: "ForgotPassword",
    component: ForgotPassword,
    meta: { title: "Quên mật khẩu" },
  },

  // ⬅️ ADD: 6 routes chính sách
  {
    path: "/policy/product-info",
    name: "ProductInfo",
    component: ProductInfo,
    meta: { title: "Thông tin sản phẩm" },
  },
  {
    path: "/policy/return",
    name: "ReturnPolicy",
    component: ReturnPolicy,
    meta: { title: "Chính sách đổi trả" },
  },
  {
    path: "/policy/privacy",
    name: "PrivacyPolicy",
    component: PrivacyPolicy,
    meta: { title: "Chính sách bảo mật" },
  },
  {
    path: "/policy/shipping",
    name: "ShippingPolicy",
    component: ShippingPolicy,
    meta: { title: "Chính sách vận chuyển" },
  },
  {
    path: "/policy/payment",
    name: "PaymentPolicy",
    component: PaymentPolicy,
    meta: { title: "Chính sách thanh toán" },
  },
  {
    path: "/policy/refund",
    name: "RefundPolicy",
    component: RefundPolicy,
    meta: { title: "Chính sách hoàn tiền" },
  },
  {
    path: "/tips/detect-clothes",
    name: "DetectClothes",
    component: () => import("@/components/views/tips/DetectClothes.vue"),
    meta: { title: "Kinh nghiệm nhận biết quần áo" },
  },
  {
    path: "/tips/choose-online",
    name: "ChooseOutfitOnline",
    component: () => import("@/components/views/tips/ChooseOutfitOnline.vue"),
    meta: { title: "Kinh nghiệm chọn trang phục online" },
  },
  {
    path: "/tips/choose-loafers",
    name: "ChooseLoafers",
    component: () => import("@/components/views/tips/ChooseLoafers.vue"),
    meta: { title: "Kinh nghiệm chọn giày lười" },
  },
  {
    path: "/tips/choose-for-adults",
    name: "ChooseForAdults",
    component: () => import("@/components/views/tips/ChooseForAdults.vue"),
    meta: { title: "Kinh nghiệm chọn quần áo người lớn" },
  },

  {
    path: "/admin",
    component: AdminDashBoard,
    children: [
      { path: "", component: AdminHomePage },
      { path: "products", component: ProductManager },
      { path: "orders", component: OrderManager },
      { path: "coupons", component: AdminCouponPage },
      { path: "reports", component: ReportPage },
      { path: "categories", component: CategoryPage },
      { path: "payments", component: PaymentPage },
      { path: "shipping", component: AdminShippingProvider },
      { path: "supplier", component: SupplierPage },
      { path: "support", component: AdminSupportPage },
      { path: "ui", component: AdminUiPage },
      { path: "invoicedetails", component: AdminImportInvoiceDetail },
      { path: "inventory", component: AdminInventoryPage },
      { path: "invoice", component: AdminImportInvoicePage },
      { path: "users", component: AdminUserPage },
      { path: "inventory_adjustment", component: AdminInventoryAdjustmentPage },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem("user"));

  if (to.path.startsWith("/admin")) {
    if (user && user.role === "ADMIN") {
      next();
    } else {
      alert("Bạn không có quyền truy cập trang quản trị!");
      next("/");
    }
  } else {
    next();
  }
});

// ⬅️ ADD (tuỳ chọn): set document.title theo meta.title nếu có
router.afterEach((to) => {
  if (to.meta?.title) {
    document.title = `${to.meta.title} | Haravan Shop`;
  }
});

export default router;
