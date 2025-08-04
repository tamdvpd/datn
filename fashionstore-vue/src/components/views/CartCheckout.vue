<template>
    <div>
        <MainHeader />
        <div class="container-fluid">
            <div class="container py-4">
                <div class="row">
                    <!-- Checkout Form -->
                    <div class="col-lg-7 mb-4">
                        <div class="card">
                            <div class="card-body">
                                <h3 class="text-center mb-4">Thông Tin Thanh Toán</h3>
                                <form id="checkoutForm">
                                    <h5>Thông Tin Nhận Hàng</h5>
                                    <div class="mb-3">
                                        <label for="fullName" class="form-label">Họ và Tên</label>
                                        <input type="text" id="fullName" class="form-control" placeholder="Trấn Thành"
                                            v-model="user.fullName" />
                                        <div v-if="nameError" class="text-danger mt-1">{{ nameError }}</div>
                                    </div>
                                    <div class="mb-3">
                                        <label for="phone" class="form-label">Số Điện Thoại</label>
                                        <input type="tel" id="phone" class="form-control" placeholder="0987654321"
                                            v-model="user.phoneNumber" />
                                        <div v-if="phoneError" class="text-danger mt-1">{{ phoneError }}</div>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Chọn địa chỉ nhận hàng</label>
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" id="defaultAddress"
                                                value="default" v-model="addressType" />
                                            <label class="form-check-label" for="defaultAddress">Địa chỉ mặc
                                                định</label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" id="temporaryAddress"
                                                value="temporary" v-model="addressType" />
                                            <label class="form-check-label" for="temporaryAddress">Địa chỉ tạm
                                                thời</label>
                                        </div>
                                    </div>

                                    <div class="mb-3" v-if="addressType === 'default'">
                                        <label for="defaultAddressField" class="form-label">Địa chỉ mặc định</label>
                                        <input type="text" id="defaultAddressField" class="form-control"
                                            v-model="user.address" disabled />
                                    </div>

                                    <div v-if="addressType === 'temporary'">
                                        <div class="mb-3">
                                            <label for="street" class="form-label">Tên đường</label>
                                            <input type="text" id="street" class="form-control"
                                                v-model="temporaryAddress.street" @blur="validateStreet"
                                                placeholder="20 Nguyễn Sinh Sắc" />
                                            <div v-if="streetError" class="text-danger mt-1">{{ streetError }}</div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="province" class="form-label">Tỉnh/Thành</label>
                                            <select id="province" class="form-select"
                                                v-model="temporaryAddress.provinceCode" @change="fetchWards">
                                                <option disabled value="">-- Chọn tỉnh/thành --</option>
                                                <option v-for="p in provinces" :key="p.province_code"
                                                    :value="p.province_code">{{ p.name }}</option>
                                            </select>
                                        </div>
                                        <div class="mb-3">
                                            <label for="ward" class="form-label">Phường/Xã</label>
                                            <select id="ward" class="form-select" v-model="temporaryAddress.wardName">
                                                <option disabled value="">-- Chọn phường/xã --</option>
                                                <option v-for="w in wards" :key="w.ward_code" :value="w.ward_name">
                                                    {{ w.ward_name }}
                                                </option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <label for="note" class="form-label">Ghi Chú</label>
                                        <textarea id="note" rows="3" class="form-control"
                                            placeholder="Ghi chú về đơn hàng (không bắt buộc)"
                                            v-model="note"></textarea>
                                    </div>

                                    <h5>Vận Chuyển</h5>
                                    <div class="mb-3">
                                        <label for="shippingMethod" class="form-label">Đơn Vị Vận Chuyển*</label>
                                        <select id="shippingMethod" class="form-select" v-model="selectedShipping"
                                            required>
                                            <option v-for="sp in shippingProviders" :key="sp.id" :value="sp.id">
                                                {{ sp.name }} - {{ sp.shippingFee.toLocaleString() }}đ
                                            </option>
                                        </select>
                                    </div>

                                    <h5>Phương Thức Thanh Toán</h5>
                                    <div class="mb-3">
                                        <div class="form-check" v-for="method in paymentMethods" :key="method.id">
                                            <input class="form-check-input" type="radio" name="paymentMethod"
                                                :id="method.code" :value="method.id" v-model="selectedPayment" />
                                            <label class="form-check-label" :for="method.code">
                                                {{ method.description }}
                                            </label>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="coupon" class="form-label">Mã Giảm Giá</label>
                                        <div class="input-group">
                                            <input type="text" id="coupon" class="form-control"
                                                placeholder="Nhập mã giảm giá" v-model="code" />
                                            <button type="button" class="btn btn-outline-secondary"
                                                @click="applyCoupon">Áp Dụng</button>
                                        </div>
                                        <p v-if="couponStatus === 'error'" class="text-danger mt-2">Mã giảm giá không
                                            hợp lệ!</p>
                                        <p v-if="couponStatus === 'success'" class="text-success mt-2">Áp dụng mã giảm
                                            giá thành công!</p>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Order Summary -->
                    <div class="col-lg-5">
                        <div class="card">
                            <div class="card-body">
                                <h3 class="text-center mb-4">Đơn Hàng Của Bạn</h3>

                                <div class="fw-bold">Sản phẩm đã chọn ({{ cartItems.length }}):</div>
                                <div class="mb-3">
                                    <div v-for="(item, index) in cartItems" :key="index"
                                        class="d-flex mb-3 border-bottom pb-3">
                                        <img :src="'http://localhost:8080/images/productDetails/' + item.productDetail.imageUrl"
                                            class="img-thumbnail me-3" style="width: 80px" />
                                        <div>
                                            <div>
                                                <span class="fw-bold d-block">
                                                    {{ item.productDetail.product.name }}
                                                </span>
                                                <span v-if="item.productDetail.discountPrice"
                                                    class="text-decoration-line-through text-muted">
                                                    {{ item.productDetail.price.toLocaleString() }}đ
                                                </span>
                                                <span class="ms-2 text-danger">
                                                    {{ getFinalPrice(item.productDetail).toLocaleString() }}đ
                                                </span>
                                            </div>
                                            <small>Phân loại: Màu: {{ item.productDetail.color }}, Size: {{
                                                item.productDetail.size }}</small><br>
                                            <small>Số lượng: {{ item.quantity }}</small>
                                        </div>
                                    </div>
                                </div>

                                <h5 class="border-top pt-3">Tóm Tắt Thanh Toán</h5>
                                <ul class="list-unstyled">
                                    <li class="d-flex justify-content-between">
                                        <span>Tổng tiền hàng:</span>
                                        <span>{{ subtotal.toLocaleString() }}đ</span>
                                    </li>
                                    <li v-if="discountPercent" class="d-flex justify-content-between">
                                        <span class="text-success">Mã giảm giá ({{ discountPercent }}%):</span>
                                        <span class="text-success">-{{ discountAmount.toLocaleString() }}đ</span>
                                    </li>
                                    <li class="d-flex justify-content-between">
                                        <span>Phí vận chuyển:</span>
                                        <span>{{ shippingFee.toLocaleString() }}đ</span>
                                    </li>
                                    <li class="d-flex justify-content-between fw-bold fs-5 border-top pt-2">
                                        <span>Tổng thanh toán:</span>
                                        <span>{{ totalAmount.toLocaleString() }}đ</span>
                                    </li>
                                </ul>

                                <button type="button" class="btn btn-primary w-100" @click="submitOrder">
                                    ĐẶT HÀNG
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <MainFooter />
    </div>
</template>

<script>
import axios from "axios";
import MainHeader from "@/components/MainHeader.vue";
import MainFooter from "@/components/MainFooter.vue";

export default {
    name: "CartCheckout",
    components: {
        MainHeader,
        MainFooter,
    },
    data() {
        return {
            user: {
                fullName: '',
                phoneNumber: '',
                address: ''
            },
            addressType: 'default',
            temporaryAddress: {
                street: '',
                provinceCode: '',
                provinceName: '',
                wardName: ''
            },
            streetError: '',
            phoneError: '',
            nameError: '',
            provinces: [],
            wards: [],
            note: '',
            shippingProviders: [],
            paymentMethods: [],
            selectedShipping: '',
            selectedPayment: '',
            code: '',
            coupon: null,
            couponStatus: null,
            discountPercent: 0,
            cartItems: []
        };
    },
    computed: {
        selectedShippingProvider() {
            return this.shippingProviders.find(sp => sp.id === this.selectedShipping) || null;
        },
        shippingFee() {
            return this.selectedShippingProvider ? this.selectedShippingProvider.shippingFee : 0;
        },
        getFinalPrice() {
            return (productDetail) => {
                return productDetail.discountPrice || productDetail.price;
            };
        },
        subtotal() {
            return this.cartItems.reduce((sum, item) => {
                return sum + (this.getFinalPrice(item.productDetail) * item.quantity);
            }, 0);
        },
        discountAmount() {
            return this.subtotal * (this.discountPercent / 100);
        },
        totalAmount() {
            return this.subtotal - this.discountAmount + this.shippingFee;
        }
    },
    methods: {
        validateStreet() {
            const value = this.temporaryAddress.street.trim();
            if (!value) {
                this.streetError = "Vui lòng nhập địa chỉ";
            } else if (value.length < 8) {
                this.streetError = "Tên đường tối thiểu là 8 ký tự!";
            } else if (value.length > 20) {
                this.streetError = "Tên đường tối đa là 20 ký tự!";
            } else {
                this.streetError = "";
            }
        },
        validateName() {
            const name = this.user.fullName?.trim() || '';

            if (!name) {
                this.nameError = "Vui lòng nhập họ và tên!";
                return false;
            }

            if (name.length < 8) {
                this.nameError = "Họ và tên tối thiểu 8 ký tự!";
                return false;
            }

            if (name.length > 20) {
                this.nameError = "Họ và tên tối đa 20 ký tự!";
                return false;
            }

            this.nameError = '';
            return true;
        },
        validatePhone() {
            const phone = this.user.phoneNumber?.trim() || '';
            const phoneRegex = /^0\d{9}$/;

            if (!phone) {
                this.phoneError = "Vui lòng nhập số điện thoại!";
                return false;
            }

            if (!phoneRegex.test(phone)) {
                this.phoneError = "Số điện thoại phải có 10 chữ số, bắt đầu bằng 0 và không chứa ký tự đặc biệt!";
                return false;
            }

            this.phoneError = '';
            return true;
        },
        async fetchProvinces() {
            try {
                const res = await axios.get('https://34tinhthanh.com/api/provinces');
                this.provinces = res.data;
            } catch (error) {
                console.error('Lỗi khi lấy danh sách tỉnh/thành:', error);
            }
        },
        async fetchWards() {
            if (!this.temporaryAddress.provinceCode) return;
            try {
                const res = await axios.get(`https://34tinhthanh.com/api/wards?province_code=${this.temporaryAddress.provinceCode}`);
                this.wards = res.data;
                const selected = this.provinces.find(p => p.province_code === this.temporaryAddress.provinceCode);
                this.temporaryAddress.provinceName = selected?.name || '';
            } catch (error) {
                console.error('Lỗi khi lấy danh sách phường/xã:', error);
            }
        },
        async fetchInitialData() {
            const user = JSON.parse(localStorage.getItem('user'));
            if (!user || !user.id) {
                alert('Vui lòng đăng nhập để mua sản phẩm!');
                this.$router.push('/login');
                return;
            }

            this.user = {
                fullName: user.fullName || '',
                phoneNumber: user.phoneNumber || '',
                address: user.address || ''
            };

            try {
                const [shippingRes, paymentRes] = await Promise.all([
                    axios.get('http://localhost:8080/shipping-providers/active'),
                    axios.get('http://localhost:8080/api/payment_method/active')
                ]);
                this.shippingProviders = shippingRes.data;
                this.paymentMethods = paymentRes.data;

                if (this.paymentMethods.length > 0) {
                    this.selectedPayment = this.paymentMethods[0].id;
                }
                if (this.shippingProviders.length > 0) {
                    this.selectedShipping = this.shippingProviders[0].id;
                }
            } catch (error) {
                console.error('Lỗi khi lấy dữ liệu vận chuyển hoặc thanh toán:', error);
            }
        },
        async fetchCartItems() {
            const cartData = JSON.parse(localStorage.getItem("cartForCheckout"));
            if (!cartData || !cartData.length) {
                alert("Không có sản phẩm nào trong giỏ hàng!");
                this.$router.push("/cart");
                return;
            }

            try {
                this.cartItems = await Promise.all(
                    cartData.map(async item => {
                        const response = await axios.get(`http://localhost:8080/productdetails/${item.productDetailId}`);
                        return {
                            ...item,
                            productDetail: response.data
                        };
                    })
                );
                console.log("Danh sách sản phẩm:", this.cartItems);
            } catch (error) {
                console.error("Lỗi khi tải thông tin sản phẩm:", error);
                alert("Có lỗi xảy ra khi tải thông tin sản phẩm");
            }
        },
        async applyCoupon() {
            if (!this.code) {
                this.couponStatus = null;
                return;
            }

            try {
                const response = await axios.get(`http://localhost:8080/api/coupons/check-code/${this.code}`);
                this.coupon = response.data;
                this.code = response.data.code;
                this.discountPercent = response.data.discountPercent;
                this.couponStatus = 'success';
            } catch (error) {
                this.discountPercent = 0;
                this.couponStatus = 'error';
            }
        },
        async submitOrder() {
            try {
                const user = JSON.parse(localStorage.getItem("user"));
                if (!user || !user.id) {
                    alert("Vui lòng đăng nhập!");
                    this.$router.push("/login");
                    return;
                }

                const isNameValid = this.validateName();
                const isPhoneValid = this.validatePhone();
                if (!isNameValid || !isPhoneValid) {
                    return;
                }

                let finalAddress = this.user.address;
                if (this.addressType === 'temporary') {
                    this.validateStreet();
                    if (this.streetError) return;

                    const { street, wardName, provinceName } = this.temporaryAddress;
                    if (!street || !wardName || !provinceName) {
                        alert("Vui lòng nhập đầy đủ thông tin địa chỉ tạm thời.");
                        return;
                    }
                    finalAddress = `${street}, ${wardName}, ${provinceName}`;
                }

                const orderData = {
                    userId: user.id,
                    receiverName: this.user.fullName,
                    receiverPhone: this.user.phoneNumber,
                    receiverAddress: finalAddress,
                    note: this.note,
                    shippingProviderId: this.selectedShipping,
                    paymentMethodId: this.selectedPayment,
                    couponId: this.coupon ? this.coupon.id : 0,
                    orderItems: this.cartItems.map(item => ({
                        productDetailId: item.productDetailId,
                        quantity: item.quantity
                    }))
                };

                console.log("Dữ liệu đơn hàng:", JSON.stringify(orderData, null, 2));

                const res = await axios.post("http://localhost:8080/api/checkout/payment", orderData);

                if (res.data.success) {
                    await axios.delete(`http://localhost:8080/api/cart/clear/${user.id}`);
                    localStorage.removeItem("cartForCheckout");
                    this.$router.push("/thank-you");
                } else {
                    alert(res.data.message || "Đặt hàng thất bại!");
                }
            } catch (error) {
                console.error("Lỗi khi đặt hàng:", error);
                alert("Có lỗi xảy ra khi đặt hàng");
            }
        }
    },
    async mounted() {
        await this.fetchInitialData();
        await this.fetchProvinces();
        await this.fetchCartItems();
    }
};
</script>
<style scoped>
.card {
    border: none;
    /* Bỏ viền mặc định */
    border-radius: 15px;
    /* Bo góc */
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
    /* Đổ bóng mềm */
    transition: all 0.3s ease;
    /* Hiệu ứng mượt khi hover */
    overflow: hidden;
    /* Đảm bảo nội dung không vượt ra ngoài bo góc */
}

.card:hover {
    transform: translateY(-5px);
    /* Nâng card lên khi hover */
    box-shadow: 0 12px 20px rgba(0, 0, 0, 0.15);
    /* Đổ bóng đậm hơn khi hover */
}

.card-body {
    background: linear-gradient(90deg, #ACB6E5, #86FDE8);
}
</style>
