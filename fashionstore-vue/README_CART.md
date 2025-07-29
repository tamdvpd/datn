# Hướng dẫn chức năng Giỏ hàng

## Tổng quan
Chức năng giỏ hàng cho phép người dùng thêm sản phẩm vào giỏ hàng từ trang chi tiết sản phẩm và quản lý giỏ hàng của mình.

## Các tính năng chính

### 1. Thêm sản phẩm vào giỏ hàng
- **Vị trí**: Trang chi tiết sản phẩm (`/products/:id`)
- **Cách sử dụng**:
  1. Chọn size và màu sắc sản phẩm
  2. Nhập số lượng mong muốn
  3. Nhấn nút "🛒 Thêm vào giỏ"
  4. Hệ thống sẽ kiểm tra đăng nhập và thêm sản phẩm vào giỏ hàng

### 2. Mua ngay
- **Vị trí**: Trang chi tiết sản phẩm
- **Cách sử dụng**:
  1. Chọn size và màu sắc sản phẩm
  2. Nhập số lượng
  3. Nhấn nút "✅ Chọn và Mua ngay"
  4. Sản phẩm sẽ được thêm vào giỏ hàng và chuyển đến trang giỏ hàng

### 3. Quản lý giỏ hàng
- **Vị trí**: Trang giỏ hàng (`/cart`)
- **Các chức năng**:
  - Xem danh sách sản phẩm trong giỏ hàng
  - Cập nhật số lượng sản phẩm
  - Xóa sản phẩm khỏi giỏ hàng
  - Xem tổng tiền hàng
  - Tiến hành đặt hàng

### 4. Hiển thị số lượng giỏ hàng
- **Vị trí**: Header (MainHeader.vue)
- **Mô tả**: Hiển thị badge với số lượng sản phẩm trong giỏ hàng
- **Cập nhật tự động**: Khi có thay đổi trong giỏ hàng

## Yêu cầu hệ thống

### Backend (Spring Boot)
- API endpoints:
  - `GET /api/cart/{userId}` - Lấy giỏ hàng của user
  - `POST /api/cart` - Thêm sản phẩm vào giỏ hàng
  - `PUT /api/cart/{cartId}` - Cập nhật số lượng
  - `DELETE /api/cart/{cartId}` - Xóa sản phẩm khỏi giỏ hàng
  - `DELETE /api/cart/clear/{userId}` - Xóa toàn bộ giỏ hàng

### Frontend (Vue.js)
- Các component chính:
  - `ProductDetail.vue` - Trang chi tiết sản phẩm
  - `Cart.vue` - Trang giỏ hàng
  - `MainHeader.vue` - Header với hiển thị số lượng giỏ hàng

## Luồng hoạt động

1. **Thêm vào giỏ hàng**:
   ```
   User chọn sản phẩm → Kiểm tra đăng nhập → Gọi API → Cập nhật UI → Thông báo thành công
   ```

2. **Quản lý giỏ hàng**:
   ```
   User vào trang giỏ hàng → Load dữ liệu từ API → Hiển thị danh sách → Cho phép thao tác
   ```

3. **Cập nhật số lượng**:
   ```
   User thay đổi số lượng → Gọi API cập nhật → Cập nhật UI → Tính lại tổng tiền
   ```

## Lưu ý quan trọng

1. **Đăng nhập bắt buộc**: Người dùng phải đăng nhập để sử dụng chức năng giỏ hàng
2. **Kiểm tra tồn kho**: Hệ thống sẽ kiểm tra số lượng tồn kho trước khi thêm vào giỏ hàng
3. **Cập nhật real-time**: Số lượng giỏ hàng được cập nhật tự động khi có thay đổi
4. **Xử lý lỗi**: Có thông báo lỗi khi không thể thực hiện các thao tác

## Cấu hình

### CORS
Backend đã được cấu hình CORS để cho phép frontend ở `http://localhost:3001` truy cập API.

### Database
Cần có các bảng:
- `Carts` - Lưu thông tin giỏ hàng
- `ProductDetails` - Chi tiết sản phẩm (size, màu, giá)
- `Products` - Thông tin sản phẩm
- `Users` - Thông tin người dùng

## Troubleshooting

### Lỗi thường gặp:
1. **"Vui lòng đăng nhập"**: Người dùng chưa đăng nhập
2. **"Số lượng vượt quá tồn kho"**: Sản phẩm không đủ số lượng
3. **"Không thể thêm vào giỏ hàng"**: Lỗi kết nối hoặc server

### Cách khắc phục:
1. Đảm bảo đã đăng nhập
2. Kiểm tra kết nối mạng
3. Kiểm tra backend server có đang chạy không
4. Kiểm tra console browser để xem lỗi chi tiết 