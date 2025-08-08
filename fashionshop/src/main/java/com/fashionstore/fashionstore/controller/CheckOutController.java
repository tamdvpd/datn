package com.fashionstore.fashionstore.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import com.fashionstore.fashionstore.service.CheckOutService;
import com.fashionstore.fashionstore.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckOutController {

    private final CheckOutService checkOutService;
    private final VNPayService vnPayService;

    @PostMapping("/review")
    public ResponseEntity<Map<String, Object>> getFormBuyNow(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(checkOutService.reviewOrder(payload));
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> getPayment(@RequestBody Map<String, Object> payload,
            HttpServletRequest request) {
        Map<String, Object> result = checkOutService.processPayment(payload);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/return")
    public RedirectView handleVNPayReturn(HttpServletRequest request) {
        Map<String, String> result = vnPayService.orderReturn(request);
        Integer orderId = Integer.valueOf(result.get("vnp_OrderInfo"));
        if ("success".equals(result.get("status"))) {
            checkOutService.updateStatus(orderId, "Pending Confirmation");
        } else {
            checkOutService.updateStatus(orderId, "Failed Payment");
        }
        // Redirect về frontend
        String redirectUrl = "http://localhost:3001/payment-result" +
                "?status=" + result.get("status") +
                "&method=" + "VNPAY" +
                "&orderId=" + orderId +
                "&amount=" + result.get("totalPrice");

        return new RedirectView(redirectUrl);
    }

    @PostMapping("/payment-cart")
    public ResponseEntity<Map<String, Object>> getPaymentCart(@RequestBody Map<String, Object> payload,
            HttpServletRequest request) {
        Map<String, Object> result = checkOutService.processPaymentCart(payload);
        return ResponseEntity.ok(result);
    }

}
