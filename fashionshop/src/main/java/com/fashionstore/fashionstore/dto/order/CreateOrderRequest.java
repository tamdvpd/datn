package com.fashionstore.fashionstore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull
    @Schema(description = "ID người dùng tạo đơn hàng", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "ID đơn vị vận chuyển", example = "2")
    private Integer shippingProviderId;

    @NotNull
    @Schema(description = "ID phương thức thanh toán", example = "3")
    private Integer paymentMethodId;

    @Schema(description = "ID mã giảm giá nếu có", example = "10")
    private Integer couponId;

    @NotBlank
    @Schema(description = "Tên người nhận", example = "Nguyễn Văn A")
    private String receiverName;

    @NotBlank
    @Size(min = 9, max = 15)
    @Pattern(regexp = "^(0[0-9]{9,10})$", message = "Số điện thoại không hợp lệ")
    @Schema(description = "Số điện thoại người nhận", example = "0912345678")
    private String receiverPhone;

    @NotBlank
    @Schema(description = "Địa chỉ giao hàng", example = "123 Nguyễn Trãi, Quận 5, TP.HCM")
    private String receiverAddress;

    @Schema(description = "Ghi chú thêm của khách hàng", example = "Giao vào buổi sáng")
    private String note;
}
