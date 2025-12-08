package com.example.fruitshop_be.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordMail(
            String to,
            String newPassword) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            String html = buildResetPasswordTemplate(to, newPassword);

            helper.setTo(to);
            helper.setSubject("Khôi phục mật khẩu - Fruit Shop");
            helper.setText(html, true); // true = HTML

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }

    private String buildResetPasswordTemplate(String username, String newPassword) {
        return """
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        padding: 20px;
                    }
                    .container {
                        max-width: 520px;
                        margin: auto;
                        background: #ffffff;
                        padding: 25px;
                        border-radius: 6px;
                    }
                    .password-box {
                        margin: 15px 0;
                        padding: 10px;
                        background: #f1f1f1;
                        font-size: 18px;
                        text-align: center;
                        letter-spacing: 2px;
                        font-weight: bold;
                    }
                    .footer {
                        margin-top: 30px;
                        font-size: 12px;
                        color: #777;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Xin chào %s,</h2>

                    <p>Bạn đã yêu cầu khôi phục mật khẩu cho tài khoản <b>Fruit Shop</b>.</p>

                    <p>Mật khẩu mới của bạn là:</p>

                    <div class="password-box">%s</div>

                    <p>Vui lòng đăng nhập và đổi mật khẩu ngay để đảm bảo an toàn.</p>

                    <div class="footer">
                        <p>Nếu bạn không yêu cầu việc này, vui lòng bỏ qua email.</p>
                        <p>© Fruit Shop</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username, newPassword);
    }
}
