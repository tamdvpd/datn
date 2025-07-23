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
            otpStorage.remove(email);        // ✅ xoá OTP
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
}