<template>
  <MainHeader />
  <div
    class="p-4 d-flex justify-content-center align-items-center"
    style="min-height: 80vh"
  >
    <div style="width: 100%; max-width: 600px">
      <h2 class="mb-3 text-center">👤 Thông tin cá nhân</h2>
      <p class="text-center">
        Quản lý và chỉnh sửa thông tin tài khoản của bạn.
      </p>

      <div class="text-center mb-4 position-relative">
        <img
          :src="displayAvatar"
          @error="onImgError"
          alt="Avatar"
          class="rounded-circle border"
          width="120"
          height="120"
          style="object-fit: cover"
        />

        <div class="mt-2">
          <button
            class="btn btn-outline-primary btn-sm me-2"
            @click="triggerFileSelect"
          >
            Chọn ảnh từ máy
          </button>
          <button
            class="btn btn-outline-secondary btn-sm"
            @click="enterImageUrl"
          >
            Nhập URL ảnh
          </button>
        </div>

        <input
          type="file"
          ref="fileInput"
          accept="image/*"
          @change="handleImageChange"
          style="display: none"
        />
      </div>

      <div class="card shadow-sm">
        <div class="card-header bg-info text-white">
          <h5 class="mb-0">Cập nhật thông tin cá nhân</h5>
        </div>
        <div class="card-body">
          <form @submit.prevent="updateProfile">
            <div class="mb-3">
              <label class="form-label">Họ tên</label>
              <input
                v-model="user.fullName"
                type="text"
                class="form-control"
                required
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input
                v-model="user.email"
                type="email"
                class="form-control"
                disabled
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Số điện thoại</label>
              <input
                v-model="user.phoneNumber"
                type="text"
                class="form-control"
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Địa chỉ</label>
              <input v-model="user.address" type="text" class="form-control" />
            </div>
            <div class="text-end">
              <button type="submit" class="btn btn-success">
                <i class="bi bi-check-circle me-1"></i> Cập nhật
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <MainFooter />
</template>
<script setup>
import MainHeader from "@/components/MainHeader.vue";
import MainFooter from "@/components/MainFooter.vue";
import { ref, onMounted, computed } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const API_BASE = "http://localhost:8080";

const router = useRouter();
const user = ref({});
const fileInput = ref(null);
const selectedFile = ref(null);
const previewUrl = ref("");
const bust = ref(""); // cache-busting sau khi upload

function isAbsHttp(u = "") {
  return /^https?:\/\//i.test(u);
}
function normalizeRelative(u = "") {
  if (!u) return "";
  if (u.startsWith("/")) return API_BASE + u;
  return `${API_BASE}/${u}`;
}
function normalizeImageUrl(u = "") {
  if (!u) return "";
  if (u.startsWith("http://localhost:3001"))
    return u.replace("http://localhost:3001", API_BASE);
  if (isAbsHttp(u)) return u;
  return normalizeRelative(u);
}

const displayAvatar = computed(() => {
  if (previewUrl.value) return previewUrl.value;
  const url = user.value?.imageUrl;
  const finalUrl = url
    ? normalizeImageUrl(url)
    : new URL("@/assets/img/default-avatar.png", import.meta.url).href;
  return bust.value ? `${finalUrl}?t=${bust.value}` : finalUrl;
});

function onImgError(e) {
  e.target.src = new URL(
    "@/assets/img/default-avatar.png",
    import.meta.url
  ).href;
}

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) return router.push("/login");
  user.value = JSON.parse(storedUser);
  if (
    typeof user.value.imageUrl === "string" &&
    user.value.imageUrl.startsWith("http://localhost:3001")
  ) {
    user.value.imageUrl = user.value.imageUrl.replace(
      "http://localhost:3001",
      API_BASE
    );
    localStorage.setItem("user", JSON.stringify(user.value));
  }
});

function triggerFileSelect() {
  fileInput.value?.click();
}

async function uploadAvatarOnly() {
  if (!selectedFile.value) return;
  const form = new FormData();
  form.append("file", selectedFile.value);

  const { data } = await axios.put(
    `${API_BASE}/users/${user.value.id}/avatar`,
    form,
    {
      headers: { "Content-Type": "multipart/form-data" },
    }
  );

  if (data?.imageUrl) user.value.imageUrl = data.imageUrl;

  // reset file + preview
  fileInput.value.value = "";
  if (previewUrl.value) URL.revokeObjectURL(previewUrl.value);
  previewUrl.value = "";
  selectedFile.value = null;

  bust.value = Date.now().toString();
  localStorage.setItem("user", JSON.stringify(user.value));
}

async function handleImageChange(e) {
  const file = e.target.files?.[0];
  if (!file) return;

  const allowed = ["image/jpeg", "image/png", "image/webp"];
  if (!allowed.includes(file.type)) {
    alert("Chỉ chấp nhận JPEG/PNG/WEBP");
    e.target.value = "";
    return;
  }
  if (file.size > 5 * 1024 * 1024) {
    alert("Kích thước tối đa 5MB");
    e.target.value = "";
    return;
  }

  selectedFile.value = file;
  if (previewUrl.value) URL.revokeObjectURL(previewUrl.value);
  previewUrl.value = URL.createObjectURL(file);

  try {
    await uploadAvatarOnly(); // upload ngay khi chọn (hoặc tách nút "Lưu avatar" riêng nếu muốn)
    alert("Cập nhật avatar thành công!");
  } catch (e2) {
    console.error(e2);
    alert("Tải ảnh thất bại, vui lòng thử lại.");
  }
}

function enterImageUrl() {
  const url = prompt("Nhập URL ảnh:");
  if (!url) return;
  if (!/^https?:\/\/.+/i.test(url)) {
    alert("URL không hợp lệ, vui lòng nhập lại.");
    return;
  }
  if (previewUrl.value) URL.revokeObjectURL(previewUrl.value);
  previewUrl.value = "";
  selectedFile.value = null;
  user.value.imageUrl = url.trim();
  localStorage.setItem("user", JSON.stringify(user.value));
}

async function updateProfile() {
  try {
    const payload = {
      fullName: (user.value.fullName || "").trim(),
      phoneNumber: (user.value.phoneNumber || "").trim(),
      address: (user.value.address || "").trim(),
      imageUrl: (user.value.imageUrl || "").trim(), // có thể cho BE bỏ qua nếu muốn khoá
    };
    await axios.put(`${API_BASE}/users/${user.value.id}/update`, payload);
    localStorage.setItem("user", JSON.stringify(user.value));
    alert("Cập nhật thành công!");
  } catch (err) {
    console.error("Lỗi chi tiết:", err?.response?.data || err);
    alert(
      err?.response?.data?.error ||
        err?.response?.data?.message ||
        "Cập nhật thất bại!"
    );
  }
}
</script>
