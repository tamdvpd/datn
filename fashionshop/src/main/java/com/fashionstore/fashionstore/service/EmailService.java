package com.fashionstore.fashionstore.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Lưu trữ OTP tạm thời: email → mã OTP
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    // Lưu trạng thái email đã xác thực: email → true
    private final Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom("tam1662005@gmail.com");
            mailSender.send(message);
            System.out.println("✔️ Đã gửi mail đến: " + to);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email", e);
        }
    }

    // Gửi OTP và lưu lại
    public void sendOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        String subject = "Mã xác thực đăng ký tài khoản";
        String content = "Mã xác thực của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.";

        sendSimpleEmail(email, subject, content);
    }

    // Kiểm tra OTP
    public boolean verifyOTP(String email, String inputOtp) {
        String savedOtp = otpStorage.get(email);
        if (savedOtp != null && savedOtp.equals(inputOtp)) {
            verifiedEmails.put(email, true); // ✅ đánh dấu đã xác thực
            otpStorage.remove(email); // ✅ xoá OTP
            return true;
        }
        return false;
    }

    // Xoá OTP sau khi dùng
    public void clearOTP(String email) {
        otpStorage.remove(email);
    }

    // Kiểm tra email đã xác thực chưa
    public boolean isEmailVerified(String email) {
        return verifiedEmails.getOrDefault(email, false);
    }

    // Xoá trạng thái xác thực sau khi đăng ký xong
    public void clearVerified(String email) {
        verifiedEmails.remove(email);
    }

    // ====== THÊM MỚI cho QUÊN MẬT KHẨU ======
    private final Map<String, String> resetOtpStorage = new ConcurrentHashMap<>();
    private final Map<String, Boolean> resetVerifiedEmails = new ConcurrentHashMap<>();
    private final Map<String, Long> resetLastSentAt = new ConcurrentHashMap<>();

    // Gửi OTP reset password (không ảnh hưởng sendOTP đăng ký)
    public void sendResetOTP(String email) {
        // (tuỳ chọn) rate-limit 60s
        long now = System.currentTimeMillis();
        Long last = resetLastSentAt.get(email);
        if (last != null && now - last < 60_000) {
            throw new RuntimeException("Bạn vừa yêu cầu OTP. Vui lòng thử lại sau ít phút.");
        }

        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        resetOtpStorage.put(email, otp);
        resetLastSentAt.put(email, now);

        String subject = "Mã xác thực đặt lại mật khẩu";
        String content = "Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.";
        sendSimpleEmail(email, subject, content);

        // (tuỳ chọn) Auto-expire OTP sau 5 phút
        new Thread(() -> {
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException ignored) {
            }
            // chỉ xóa nếu vẫn còn và chưa verify
            resetOtpStorage.remove(email, otp);
        }).start();
    }

    // Xác thực OTP reset
    public boolean verifyResetOTP(String email, String inputOtp) {
        String saved = resetOtpStorage.get(email);
        if (saved != null && saved.equals(inputOtp)) {
            resetVerifiedEmails.put(email, true);
            resetOtpStorage.remove(email);
            return true;
        }
        return false;
    }

    // Kiểm tra email đã verify cho reset chưa
    public boolean isResetVerified(String email) {
        return resetVerifiedEmails.getOrDefault(email, false);
    }

    // Xoá OTP và cờ verify cho reset
    public void clearResetOTP(String email) {
        resetOtpStorage.remove(email);
    }

    public void clearResetVerified(String email) {
        resetVerifiedEmails.remove(email);
    }

}