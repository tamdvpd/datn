<template>
    <MainHeader />
    <div class="payment-result-container">
        <div class="card payment-result-card shadow-lg">
            <!-- Body -->
            <div class="card-body text-center py-5">
                <!-- Success State -->
                <div v-if="isSuccess" class="success-state">
                    <div class="icon-container mb-4">
                        <i class="bi bi-check-circle-fill text-success" style="font-size: 5rem;"></i>
                    </div>
                    <h3 class="text-success mb-3">Thanh Toán Thành Công!</h3>
                    <p class="text-muted mb-4">
                        Cảm ơn bạn đã mua hàng tại Fashion Store. Đơn hàng của bạn đã được xử lý thành công.
                    </p>

                    <div class="order-details mb-4">
                        <div class="row justify-content-center">
                            <div class="col-md-8">
                                <div class="detail-item d-flex justify-content-between mb-2">
                                    <span class="fw-bold">Mã đơn hàng:</span>
                                    <span>{{ orderId }}</span>
                                </div>
                                <div class="detail-item d-flex justify-content-between mb-2">
                                    <span class="fw-bold">Ngày đặt hàng:</span>
                                    <span>{{ currentDate }}</span>
                                </div>
                                <div class="detail-item d-flex justify-content-between mb-2">
                                    <span class="fw-bold">Tổng tiền:</span>
                                    <span class="text-success fw-bold">{{ formatCurrency(totalAmount) }}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="action-buttons mt-4">
                        <router-link :to="`/order`" class="btn btn-primary me-3">
                            <i class="bi bi-eye me-2"></i>Xem đơn hàng
                        </router-link>
                        <router-link to="/" class="btn btn-outline-secondary">
                            <i class="bi bi-bag me-2"></i>Tiếp tục mua sắm
                        </router-link>
                    </div>
                </div>

                <!-- Error State -->
                <div v-else class="error-state">
                    <div class="icon-container mb-4">
                        <i class="bi bi-x-circle-fill text-danger" style="font-size: 5rem;"></i>
                    </div>
                    <h3 class="text-danger mb-3">Thanh Toán Thất Bại!</h3>
                    <p class="text-muted mb-4">
                        {{ errorMessage || 'Đã xảy ra lỗi trong quá trình thanh toán. Vui lòng thử lại.' }}
                    </p>

                    <div class="suggestions mb-4">
                        <p class="fw-bold">Bạn có thể:</p>
                        <ul class="list-unstyled">
                            <li class="mb-2">
                                <i class="bi bi-arrow-right text-primary me-2"></i>
                                Kiểm tra lại thông tin thanh toán
                            </li>
                            <li class="mb-2">
                                <i class="bi bi-arrow-right text-primary me-2"></i>
                                Thử lại với phương thức thanh toán khác
                            </li>
                            <li>
                                <i class="bi bi-arrow-right text-primary me-2"></i>
                                Liên hệ bộ phận hỗ trợ nếu cần giúp đỡ
                            </li>
                        </ul>
                    </div>

                    <div class="action-buttons mt-4">
                        <router-link to="/" class="btn btn-danger me-3">
                            <i class="bi bi-arrow-repeat me-2"></i>Thử lại
                        </router-link>
                        <a href="https://zalo.me/0889228847" target="_blank" rel="nofollow"
                            class="btn btn-outline-primary">
                            <i class="bi bi-headset me-2"></i>Liên hệ hỗ trợ
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <MainFooter />
</template>

<script>
import MainHeader from "@/components/MainHeader.vue";
import MainFooter from "@/components/MainFooter.vue";
export default {
    name: 'PaymentResult',
    components: {
        MainHeader,
        MainFooter,
    },
    data() {
        return {
            isSuccess: false, // Mặc định là false, sẽ được cập nhật từ route
            orderId: '',
            totalAmount: 0,
            errorMessage: '',
            currentDate: new Date().toLocaleDateString('vi-VN')
        }
    },
    created() {
        // Lấy thông tin từ route query hoặc params
        this.isSuccess = this.$route.query.status === 'success';
        this.orderId = this.$route.query.orderId || '';
        this.errorMessage = this.$route.query.error || '';
        if (this.$route.query.method === 'VNPAY') {
            this.totalAmount = parseFloat(this.$route.query.amount) / 100 || 0;
        } else {
            this.totalAmount = parseFloat(this.$route.query.amount) || 0;
        }
    },
    methods: {
        formatCurrency(value) {
            return new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(value)
        }
    }
}
// http://localhost:3001/payment-result?status=success&orderId=DH123&amount=1000000
</script>

<style scoped>
/* Giữ nguyên phần style như cũ */
.payment-result-container {
    max-width: 700px;
    margin: 2rem auto;
    padding: 0 15px;
}

.payment-result-card {
    border-radius: 15px;
    overflow: hidden;
    border: none;
}

.card-header h2 {
    font-weight: 700;
    color: #333;
}

.icon-container i {
    animation: bounceIn 0.8s;
}

@keyframes bounceIn {
    0% {
        transform: scale(0.1);
        opacity: 0;
    }

    60% {
        transform: scale(1.2);
        opacity: 1;
    }

    100% {
        transform: scale(1);
    }
}

.order-details {
    background-color: #f8f9fa;
    border-radius: 10px;
    padding: 1.5rem;
}

.detail-item {
    padding: 0.5rem 0;
    border-bottom: 1px solid #eee;
}

.detail-item:last-child {
    border-bottom: none;
}

.btn {
    padding: 0.5rem 1.5rem;
    border-radius: 50px;
    font-weight: 500;
    transition: all 0.3s;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.btn-primary {
    background-color: #6c5ce7;
    border-color: #6c5ce7;
    color: white;
    text-decoration: none;
}

.btn-primary:hover {
    background-color: #5649c0;
    border-color: #5649c0;
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(108, 92, 231, 0.3);
    color: white;
}

.btn-outline-secondary {
    text-decoration: none;
}

.btn-outline-secondary:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.suggestions {
    background-color: #f8f9fa;
    border-radius: 10px;
    padding: 1.5rem;
    text-align: left;
    max-width: 500px;
    margin: 0 auto;
}

.list-unstyled li {
    padding: 0.3rem 0;
}

/* Thêm style cho router-link để loại bỏ gạch chân */
a {
    text-decoration: none;
}
</style>