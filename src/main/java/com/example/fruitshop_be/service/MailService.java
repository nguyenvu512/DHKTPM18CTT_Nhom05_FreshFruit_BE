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

    // PHƯƠNG THỨC 2: ĐƠN HÀNG ĐANG XỬ LÝ (PROCESSING)
    public void sendOrderProcessingMail(
            String to,
            String customerName,
            String orderId) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String html = buildOrderProcessingTemplate(customerName, orderId);

            helper.setTo(to);
            helper.setSubject("Đơn hàng #" + orderId + " đang được xử lý - Fruit Shop");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Gửi email PROCESSING thất bại", e);
        }
    }

    // PHƯƠNG THỨC 3: ĐƠN HÀNG ĐANG GIAO (SHIPPING)
    public void sendOrderShippingMail(
            String to,
            String customerName,
            String orderId) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String html = buildOrderShippingTemplate(customerName, orderId);

            helper.setTo(to);
            helper.setSubject("Đơn hàng #" + orderId + " đã được gửi đi - Fruit Shop");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Gửi email SHIPPING thất bại", e);
        }
    }

    // PHƯƠNG THỨC 4: GIAO HÀNG THÀNH CÔNG (DELIVERED)
    public void sendOrderDeliveredMail(
            String to,
            String customerName,
            String orderId) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String html = buildOrderDeliveredTemplate(customerName, orderId);

            helper.setTo(to);
            helper.setSubject("Chúc mừng! Đơn hàng #" + orderId + " đã giao thành công - Fruit Shop");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Gửi email DELIVERED thất bại", e);
        }
    }

    // PHƯƠNG THỨC 5: ĐƠN HÀNG THẤT BẠI (FAILED)
    public void sendOrderFailedMail(
            String to,
            String customerName,
            String orderId,
            String reason) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String html = buildOrderFailedTemplate(customerName, orderId, reason);

            helper.setTo(to);
            helper.setSubject("Thông báo: Đơn hàng #" + orderId + " giao thất bại - Fruit Shop");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Gửi email FAILED thất bại", e);
        }
    }

    // Phần private String build...Template(....)

    // TEMPLATE CHUNG (Giữ nguyên style)
    private static final String EMAIL_STYLE = """
        <style>
            body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
            .container { max-width: 520px; margin: auto; background: #ffffff; padding: 25px; border-radius: 6px; }
            .status-box { margin: 15px 0; padding: 12px; background: #e6f7ff; border: 1px solid #91d5ff; font-size: 16px; text-align: center; font-weight: bold; color: #0056b3; }
            .footer { margin-top: 30px; font-size: 12px; color: #777; }
            .button { display: inline-block; padding: 10px 20px; margin-top: 15px; background-color: #5cb85c; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; }
        </style>
    """;

    // TEMPLATE 1: PROCESSING
    private String buildOrderProcessingTemplate(String customerName, String orderId) {
        return """
            <html>
            <head>
                <meta charset="UTF-8">
                %s
            </head>
            <body>
                <div class="container">
                    <h2>Xin chào %s,</h2>

                    <p>Chúng tôi xin thông báo đơn hàng **#%s** của bạn đã được tiếp nhận thành công và đang được **CHUẨN BỊ**.</p>
                    
                    <div class="status-box" style="background: #fffbe6; border-color: #ffe58f; color: #faad14;">
                        ĐANG XỬ LÝ
                    </div>

                    <p>Bộ phận kho đang nhanh chóng lấy hàng, kiểm tra chất lượng và đóng gói sản phẩm.</p>
                    
                    <div class="footer">
                        <p>Chúng tôi sẽ thông báo cho bạn ngay khi đơn hàng được giao cho đơn vị vận chuyển.</p>
                        <p>© Fruit Shop</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(EMAIL_STYLE, customerName, orderId);
    }

    // TEMPLATE 2: SHIPPING
    private String buildOrderShippingTemplate(String customerName, String orderId) {
        return """
            <html>
            <head>
                <meta charset="UTF-8">
                %s
            </head>
            <body>
                <div class="container">
                    <h2>Xin chào %s,</h2>

                    <p>Đơn hàng **#%s** của bạn **ĐÃ ĐƯỢC GỬI ĐI** và đang trên đường giao đến bạn!</p>
                    
                    <div class="status-box">
                        ĐANG VẬN CHUYỂN
                    </div>

                    <p>Xin lưu ý giữ điện thoại để nhân viên giao hàng có thể liên hệ.</p>

                    <div class="footer">
                        <p>Mọi thắc mắc, vui lòng liên hệ bộ phận hỗ trợ của Fruit Shop.</p>
                        <p>© Fruit Shop</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(EMAIL_STYLE, customerName, orderId);
    }

    // TEMPLATE 3: DELIVERED
    private String buildOrderDeliveredTemplate(String customerName, String orderId) {
        return """
            <html>
            <head>
                <meta charset="UTF-8">
                %s
            </head>
            <body>
                <div class="container">
                    <h2>Xin chào %s,</h2>

                    <p>Chúng tôi rất vui mừng thông báo rằng đơn hàng **#%s** của bạn **ĐÃ GIAO HÀNG THÀNH CÔNG**.</p>
                    
                    <div class="status-box" style="background: #f6ffed; border-color: #b7eb8f; color: #52c41a;">
                        ĐÃ GIAO HÀNG
                    </div>

                    <p>Cảm ơn bạn đã tin tưởng và mua sắm tại Fruit Shop. Hy vọng bạn hài lòng với sản phẩm.</p>
                    
                    <div class="footer">
                        <p>Nếu bạn có bất kỳ vấn đề gì về đơn hàng, vui lòng liên hệ chúng tôi trong vòng 7 ngày.</p>
                        <p>© Fruit Shop</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(EMAIL_STYLE, customerName, orderId);
    }

    // TEMPLATE 4: FAILED
    private String buildOrderFailedTemplate(String customerName, String orderId, String reason) {
        return """
            <html>
            <head>
                <meta charset="UTF-8">
                %s
            </head>
            <body>
                <div class="container">
                    <h2>Xin chào %s,</h2>

                    <p>Chúng tôi rất tiếc phải thông báo rằng đơn hàng **#%s** của bạn **GIAO HÀNG THẤT BẠI**.</p>
                    
                    <div class="status-box" style="background: #fff1f0; border-color: #ffa39e; color: #f5222d;">
                        GIAO HÀNG THẤT BẠI
                    </div>

                    <p>Lý do thất bại: <b>%s</b></p>
                    
                    <p>Vui lòng liên hệ với chúng tôi ngay lập tức để sắp xếp lại lịch giao hàng hoặc xác nhận lại thông tin giao nhận.</p>
                    
                    <div class="footer">
                        <p>Nếu chúng tôi không nhận được phản hồi, đơn hàng có thể bị hủy và hoàn tiền (nếu đã thanh toán).</p>
                        <p>© Fruit Shop</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(EMAIL_STYLE, customerName, orderId, reason);
    }
}
