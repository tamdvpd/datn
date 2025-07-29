# Hướng dẫn Debug Chức năng Giỏ hàng

## Các bước kiểm tra lỗi

### 1. Kiểm tra Backend Server
```bash
# Chạy backend server
cd datn/fashionshop
mvn spring-boot:run
```

**Kiểm tra:**
- Server có chạy ở port 8080 không?
- Console có hiển thị lỗi gì không?
- API endpoint có hoạt động không?

### 2. Kiểm tra Frontend
```bash
# Chạy frontend
cd datn/fashionstore-vue
npm run serve
```

**Kiểm tra:**
- Frontend có chạy ở port 3001 không?
- Console browser có lỗi gì không?

### 3. Test API trực tiếp
Mở file `test-cart-api.html` trong browser để test API:

1. Mở file `datn/fashionstore-vue/test-cart-api.html`
2. Nhấn "Test Add to Cart" 
3. Xem kết quả response

### 4. Kiểm tra Console Browser
1. Mở Developer Tools (F12)
2. Vào tab Console
3. Thử thêm sản phẩm vào giỏ hàng
4. Xem log và error messages

### 5. Kiểm tra Network Tab
1. Mở Developer Tools (F12)
2. Vào tab Network
3. Thử thêm sản phẩm vào giỏ hàng
4. Xem request và response

## Các lỗi thường gặp và cách khắc phục

### Lỗi 1: "Failed to fetch"
**Nguyên nhân:** Backend server không chạy hoặc CORS lỗi
**Cách khắc phục:**
- Kiểm tra backend có chạy không
- Kiểm tra CORS configuration
- Kiểm tra URL API có đúng không

### Lỗi 2: "User không tồn tại"
**Nguyên nhân:** userId không hợp lệ
**Cách khắc phục:**
- Kiểm tra user đã đăng nhập chưa
- Kiểm tra localStorage có user data không
- Kiểm tra userId có đúng format không

### Lỗi 3: "Sản phẩm không tồn tại"
**Nguyên nhân:** productDetailId không hợp lệ
**Cách khắc phục:**
- Kiểm tra sản phẩm có tồn tại trong database không
- Kiểm tra productDetailId có đúng không

### Lỗi 4: "Số lượng vượt quá tồn kho"
**Nguyên nhân:** Số lượng yêu cầu > số lượng tồn kho
**Cách khắc phục:**
- Giảm số lượng yêu cầu
- Kiểm tra tồn kho trong database

## Debug Commands

### Kiểm tra Database
```sql
-- Kiểm tra user
SELECT * FROM Users WHERE id = 1;

-- Kiểm tra product detail
SELECT * FROM ProductDetails WHERE id = 1;

-- Kiểm tra cart
SELECT * FROM Carts WHERE user_id = 1;
```

### Kiểm tra Logs
```bash
# Backend logs
tail -f datn/fashionshop/logs/application.log

# Frontend logs (trong browser console)
console.log('User data:', localStorage.getItem('user'));
console.log('Selected detail:', this.selectedDetail);
```

## Test Cases

### Test Case 1: Thêm sản phẩm mới
1. Đăng nhập với user hợp lệ
2. Vào trang chi tiết sản phẩm
3. Chọn size và màu
4. Nhập số lượng = 1
5. Nhấn "Thêm vào giỏ"
6. **Expected:** Thành công, hiển thị thông báo

### Test Case 2: Thêm sản phẩm đã có
1. Thêm sản phẩm đã có trong giỏ hàng
2. **Expected:** Cập nhật số lượng thay vì tạo mới

### Test Case 3: Chưa đăng nhập
1. Đăng xuất
2. Thử thêm sản phẩm
3. **Expected:** Chuyển đến trang đăng nhập

### Test Case 4: Số lượng = 0
1. Nhập số lượng = 0
2. Thử thêm vào giỏ
3. **Expected:** Hiển thị lỗi validation

## Contact Support
Nếu vẫn gặp lỗi, hãy cung cấp:
1. Screenshot lỗi
2. Console logs
3. Network request/response
4. Backend logs 