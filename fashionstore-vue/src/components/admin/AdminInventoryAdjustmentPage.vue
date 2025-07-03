<template>
    <div>
        <h2 class="text-center d-block">📝🗂️ Quản lý phiếu điều chỉnh</h2>
        <div class="table-responsive">
            <transition name="fade-slide">
                <div v-if="showForm" class="card shadow-sm mx-auto mb-4" style="max-width: 800px;">
                    <div class="card-header" :class="isEditing ? 'bg-warning text-dark' : 'bg-primary text-white'">
                        <h5 class="mb-0">
                            <i :class="isEditing ? 'bi bi-pencil-square me-2' : 'bi bi-plus-circle me-2'"></i>
                            {{ isEditing ? 'Cập nhật Phiếu điều chỉnh' : 'Thêm Phiếu điều chỉnh' }}
                        </h5>
                    </div>
                    <div class="card-body">
                        <form @submit.prevent="submitForm">
                            <div class="mb-3">
                                <label class="form-label">Lý do</label>
                                <input v-model="form.reason" type="text" class="form-control"
                                    placeholder="Nhập lý do điều chỉnh" required />
                                <div class="text-danger mt-1" v-if="validationErrors.reason">
                                    {{ validationErrors.reason }}
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Ghi chú</label>
                                <textarea v-model="form.note" class="form-control" placeholder="Ghi chú thêm (nếu có)"
                                    rows="3"></textarea>
                                <div class="text-danger mt-1" v-if="validationErrors.note">
                                    {{ validationErrors.note }}
                                </div>
                            </div>

                            <div class="text-end">
                                <button type="submit" :class="['me-2 btn', isEditing ? 'btn-warning' : 'btn-primary']">
                                    <i class="bi bi-save me-1"></i>{{ isEditing ? 'Cập nhật' : 'Thêm' }}
                                </button>
                                <button type="button" class="btn btn-secondary" @click="showForm = false">
                                    <i class="bi bi-x-circle me-1"></i>Hủy
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </transition>
            <div class="d-flex align-items-center justify-content-between mb-3 mt-4">
                <h3 class="mb-0">
                    <i class="bi bi-tags-fill text-primary me-2"></i> Danh sách Phiếu điều chỉnh
                </h3>
                <button class="btn btn-outline-primary" @click="showAdd()">
                    <i class="bi bi-plus-square-fill"></i>
                </button>
            </div>
            <div class="table-responsive">
                <table class="table table-hover table-bordered text-center align-middle mx-auto">
                    <thead class="bg-gray-200">
                        <tr>
                            <th class="border px-4 py-2">ID</th>
                            <th class="border px-4 py-2">Lý do</th>
                            <th class="border px-4 py-2">Ghi chú</th>
                            <th class="border px-4 py-2">Ngày tạo</th>
                            <th class="border px-4 py-2">Ngày cập nhật</th>
                            <th class="border px-4 py-2">Chi tiết</th>
                            <th class="border px-4 py-2">Sửa</th>
                            <th class="border px-4 py-2">Xóa</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="inventoryAdjustment in inventoryAdjustments" :key="inventoryAdjustment.id">
                            <td class="border px-4 py-2">{{ inventoryAdjustment.id }}</td>
                            <td class="border px-4 py-2">{{ inventoryAdjustment.reason }}</td>
                            <td class="border px-4 py-2">{{ inventoryAdjustment.note }}</td>
                            <td class="border px-4 py-2">{{ formatDateTime(inventoryAdjustment.createdAt) }}</td>
                            <td class="border px-4 py-2">{{ formatDateTime(inventoryAdjustment.updatedAt) }}</td>
                            <td class="border px-4 py-2">
                                <button class="btn btn-outline-info">
                                    <i class="bi bi-eye"></i>
                                </button>
                            </td>
                            <td class="border px-4 py-2">
                                <button class="btn btn-outline-warning" @click="showEdit(inventoryAdjustment)">
                                    <i class="bi bi-pencil-square"></i>
                                </button>
                            </td>
                            <td class="border px-4 py-2">
                                <button class="btn btn-outline-danger">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 0 }">
                    <a class="page-link" href="#"
                        @click.prevent="fetchInventoryAdjustment(currentPage - 1)">Previous</a>
                </li>
                <li v-for="page in visiblePages" :key="page" class="page-item"
                    :class="{ active: currentPage === page }">
                    <a class="page-link" href="#" @click.prevent="fetchInventoryAdjustment(page)">
                        {{ page + 1 }}
                    </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages - 1 }">
                    <a class="page-link" href="#" @click.prevent="fetchInventoryAdjustment(currentPage + 1)">Next</a>
                </li>
            </ul>
        </nav>
        <div v-if="showNotification" class="notification" :class="notificationType">
            {{ notificationMessage }}
        </div>
    </div>
</template>
<script>
import axios from 'axios';
export default {
    name: 'AdminInventoryAdjustmentPage',
    data() {
        return {
            inventoryAdjustments: [],
            form: {
                id: null,
                reason: "",
                note: ""
            },
            isEditing: false,
            showForm: false,
            showNotification: false,
            currentPage: 0,
            totalPages: 0,
            validationErrors: {}
        }
    },
    methods: {
        fetchInventoryAdjustment(page) {
            const pageNumber = page !== undefined ? page : 0; // fix lỗi
            axios.get(`http://localhost:8080/api/inventory_adjustment?page=${pageNumber}`).then(response => {
                this.inventoryAdjustments = response.data.content;
                this.currentPage = response.data.number;
                this.totalPages = response.data.totalPages;
            }).catch(error => {
                this.showNotify('Tải dữ liệu không thành công')
            })
        },
        submitForm() {
            if (this.isEditing) {
                axios.put(`http://localhost:8080/api/inventory_adjustment/${this.form.id}`, this.form).then(() => {
                    this.showNotify('Cập nhật thành công phiếu điều chỉnh!');
                    this.resetForm();
                    this.validationErrors = {};
                    this.fetchInventoryAdjustment();
                }).catch(error => {
                    console.error('Lỗi khi cập nhật mã:', error);
                    if (error.response && error.response.status === 400) {
                        this.validationErrors = error.response.data;
                        this.showNotify('Vui lòng kiểm tra lại các trường!', 'error');
                    } else {
                        this.showNotify('Cập nhật mã giảm giá thất bại!', 'error');
                    }
                });
            } else {
                axios.post('http://localhost:8080/api/inventory_adjustment', this.form).then(() => {
                    this.showNotify('Thêm phiếu điều chỉnh thành công!');
                    this.resetForm();
                    this.validationErrors = {};
                    this.fetchInventoryAdjustment();
                }).catch(error => {
                    console.error('Lỗi khi thêm phiếu điều chỉnh:', error);
                    if (error.response && error.response.status === 400) {
                        this.validationErrors = error.response.data;
                        this.showNotify('Vui lòng kiểm tra lại các trường!', 'error');
                    } else {
                        this.showNotify('Thêm phiếu điều chỉnh thất bại!', 'error');
                    }
                });
            }
        },
        showNotify(message, type = 'success', duration = 5000) {
            this.notificationMessage = message;
            this.notificationType = type;
            this.showNotification = true;
            setTimeout(() => {
                this.showNotification = false;
            }, duration);
        },
        showAdd() {
            this.resetForm();
            this.showForm = true;
            this.isEditing = false;
            this.$nextTick(() => {
                window.scrollTo({ top: 0, behavior: 'smooth' });
            });
        },
        showEdit(inventoryAdjustment) {
            this.form = { ...inventoryAdjustment };
            this.showForm = true;
            this.isEditing = true;
            this.$nextTick(() => {
                window.scrollTo({ top: 0, behavior: 'smooth' });
            });
        },
        resetForm() {
            this.form = {
                id: null,
                reason: '',
                note: ''
            };
            this.showForm = false;
            this.isEditing = false;
        },
        formatDateTime(dateStr) {
            return dateStr ? new Date(dateStr).toLocaleString() : 'N/A';
        },
    },
    computed: {
        visiblePages() {
            const maxPagesToShow = 5;
            const pages = [];
            let start = Math.max(0, this.currentPage - Math.floor(maxPagesToShow / 2));
            let end = start + maxPagesToShow;

            if (end > this.totalPages) {
                end = this.totalPages;
                start = Math.max(0, end - maxPagesToShow);
            }

            for (let i = start; i < end; i++) {
                pages.push(i);
            }

            return pages;
        }
    },
    mounted() {
        this.fetchInventoryAdjustment(this.currentPage);
    }
}
</script>
<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active {
    transition: all 0.5s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
    opacity: 0;
    transform: translateY(-10px);
}

.fade-slide-enter-to,
.fade-slide-leave-from {
    opacity: 1;
    transform: translateY(0);
}

.notification {
    position: fixed;
    bottom: 20px;
    right: 20px;
    padding: 15px 25px;
    border-radius: 5px;
    color: white;
    z-index: 1000;
    animation: fadeInOut 3s ease forwards;
}

.notification.success {
    background-color: #4CAF50;
}

.notification.error {
    background-color: #f44336;
}

@keyframes fadeInOut {
    0% {
        opacity: 0;
        transform: translateY(20px);
    }

    10% {
        opacity: 1;
        transform: translateY(0);
    }

    90% {
        opacity: 1;
        transform: translateY(0);
    }

    100% {
        opacity: 0;
        transform: translateY(20px);
    }
}
</style>