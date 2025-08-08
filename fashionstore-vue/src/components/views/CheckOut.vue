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

                                    <!-- Nếu là địa chỉ mặc định -->
                                    <div class="mb-3" v-if="addressType === 'default'">
                                        <label for="defaultAddressField" class="form-label">Địa chỉ mặc định</label>
                                        <input type="text" id="defaultAddressField" class="form-control"
                                            v-model="user.address" disabled />
                                    </div>

                                    <!-- Nếu là địa chỉ tạm thời -->
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
                                                <option v-for="w in wards" :key="w.ward_code" :value="w.ward_name">{{
                                                    w.ward_name }}</option>
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
                                                @click="applyCoupon">Áp
                                                Dụng</button>
                                        </div>
                                        <p v-if="couponStatus === 'error'" class="text-danger mt-2">Mã giảm giá không
                                            hợp
                                            lệ!</p>
                                        <p v-if="couponStatus === 'success'" class="text-success mt-2">Áp dụng mã giảm
                                            giá
                                            thành công!</p>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Order Summary -->
                    <div class="col-lg-5">
                        <div class="card">
                            <div class="card-body" v-if="productDetail">
                                <h3 class="text-center mb-4">Đơn Hàng Của Bạn</h3>
                                <div class="fw-bold">Sản phẩm bạn đã chọn:</div>
                                <div class="mb-3">
                                    <div class="d-flex mb-3">
                                        <img :src="'http://localhost:8080/images/productDetails/' + productDetail.imageUrl"
                                            class="img-thumbnail me-3" style="width: 80px" />
                                        <div>
                                            <div>
                                                <span class="fw-bold d-block">
                                                    {{ productDetail.product.name }}
                                                </span>
                                                <span v-if="productDetail.discountPrice"
                                                    class="text-decoration-line-through text-muted">
                                                    {{ productDetail.price.toLocaleString() }}đ
                                                </span>
                                                <span class="ms-2 text-danger">
                                                    {{ (productDetail.discountPrice ||
                                                        productDetail.price).toLocaleString()
                                                    }}đ
                                                </span>
                                            </div>
                                            <small>Phân loại: Màu: {{ productDetail.color }}, Size: {{
                                                productDetail.size
                                                }}</small><br>
                                            <small>Số lượng: {{ buyNowProduct.quantity }}</small>
                                        </div>
                                    </div>
                                </div>
                                <h5 class="border-top pt-3">Tóm Tắt Thanh Toán</h5>
                                <ul class="list-unstyled">
                                    <li class="d-flex justify-content-between">
                                        <span>Tổng tiền hàng:</span>
                                        <span>{{ (totalPayment.price * buyNowProduct.quantity).toLocaleString()
                                            }}đ</span>
                                    </li>
                                    <li v-if="discountPercent" class="d-flex justify-content-between">
                                        <span class="text-success">Mã giảm giá ({{ discountPercent }}%):</span>
                                        <span class="text-success">-{{ ((totalPayment.price *
                                            buyNowProduct.quantity) * (discountPercent / 100)).toLocaleString()
                                            }}đ</span>
                                    </li>
                                    <li class="d-flex justify-content-between">
                                        <span>Phí vận chuyển:</span>
                                        <span>{{ shippingFee.toLocaleString() }}đ</span>
                                    </li>
                                    <li class="d-flex justify-content-between fw-bold fs-5 border-top pt-2">
                                        <span>Tổng thanh toán:</span>
                                        <span>{{ totalPayment.totalAmount.toLocaleString() }}đ</span>
                                    </li>
                                </ul>
                                <button type="button" form="checkoutForm" class="btn btn-primary w-100"
                                    @click="submitOrder">ĐẶT
                                    HÀNG</button>
                                <p class="text-center text-muted mt-3" style="font-size: 0.875rem">
                                    Bằng cách nhấn "Đặt hàng", bạn đồng ý với Điều khoản dịch vụ và Chính sách bảo mật
                                    của
                                    chúng tôi.
                                </p>
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
    name: "CheckOutPage",
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
            buyNowProduct: {},
            productDetail: null,
            code: '',
            coupon: null,
            couponStatus: null, // 'success' | 'error' | null
            discountPercent: 0,
        };
    },
    computed: {
        selectedShippingProvider() {
            return this.shippingProviders.find(sp => sp.id === this.selectedShipping) || null;
        },
        shippingFee() {
            return this.selectedShippingProvider ? this.selectedShippingProvider.shippingFee : 0;
        },
        totalPayment() {
            if (!this.productDetail || !this.buyNowProduct) {
                return { price: 0, totalAmount: 0 }; // Trả về giá trị mặc định nếu dữ liệu chưa sẵn sàng
            }
            const price = (this.productDetail.discountPrice === 0 || !this.productDetail.discountPrice)
                ? this.productDetail.price
                : this.productDetail.discountPrice;
            const subtotal = price * this.buyNowProduct.quantity;
            const total = subtotal + this.shippingFee - (subtotal * (this.discountPercent / 100));
            return {
                price: price,
                totalAmount: total,
            };
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

                // Gán tên tỉnh vào để dùng sau này (gộp địa chỉ)
                const selected = this.provinces.find(p => p.province_code === this.temporaryAddress.provinceCode);
                this.temporaryAddress.provinceName = selected?.name || '';
            } catch (error) {
                console.error('Lỗi khi lấy danh sách phường/xã:', error);
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

                const productData = JSON.parse(localStorage.getItem("buyNowProduct"));

                // Kiểm tra validation tên và số điện thoại
                const isNameValid = this.validateName();
                const isPhoneValid = this.validatePhone();
                if (!isNameValid || !isPhoneValid) {
                    return;
                }

                // Gộp địa chỉ nếu là tạm thời
                let finalAddress = this.user.address;
                if (this.addressType === 'temporary') {
                    this.validateStreet(); // <--- gọi kiểm tra lại
                    if (this.streetError) {
                        // alert("Tên đường không hợp lệ. Vui lòng kiểm tra lại.");
                        return;
                    }

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
                    productDetailId: productData.productDetailId,
                    quantity: productData.quantity,
                };
                console.log("Dữ liệu gửi lên backend:");
                console.log(JSON.stringify(orderData, null, 2));
                // Gửi yêu cầu đặt hàng lên backend
                const res = await axios.post("http://localhost:8080/api/checkout/payment", orderData);

                const result = res.data;
                // Xử lý response dạng Map
                if (result.status === "success") {
                    // Thanh toán thành công (COD)
                    this.$router.push({
                        path: '/payment-result',
                        query: {
                            status: 'success',
                            orderId: result.orderId,
                            amount: result.totalAmount,
                        }
                    });
                } else if (result.status === "fail") {
                    // Thanh toán thất bại
                    this.$router.push({
                        path: '/payment-result',
                        query: {
                            status: 'fail',
                            error: response.error || "Thanh toán thất bại"
                        }
                    });
                } else if (result.paymentUrl) {
                    window.location.href = result.paymentUrl;
                    // window.open(result.paymentUrl, '_blank');
                } else {
                    // Trường hợp response không rõ ràng
                    this.$router.push({
                        path: '/payment-result',
                        query: {
                            status: 'fail',
                            error: "Phản hồi từ server không hợp lệ"
                        }
                    });
                }
            } catch (error) {
                console.error("Lỗi khi gửi đơn hàng:", error);
                this.$router.push({
                    path: '/payment-result',
                    query: {
                        status: 'fail',
                        error: error.response?.data?.error ||
                            error.response?.data?.message ||
                            "Có lỗi xảy ra trong quá trình đặt hàng"
                    }
                });
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
        async fetchProductDetail(id) {
            try {
                const res = await axios.get(`http://localhost:8080/productdetails/${id}`);
                this.productDetail = res.data;
            } catch (error) {
                console.error("Lỗi khi lấy chi tiết sản phẩm:", error);
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
        }
    },
    async mounted() {
        await this.fetchInitialData();
        await this.fetchProvinces();
        const productData = JSON.parse(localStorage.getItem("buyNowProduct"));
        if (productData && productData.productDetailId) {
            this.buyNowProduct = productData;
            await this.fetchProductDetail(productData.productDetailId);
        }
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
