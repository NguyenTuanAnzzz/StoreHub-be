package com.an.storehub.services;

import com.an.storehub.enums.OtpType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendOtp(String email, String otp, OtpType type) {

        String subject;
        String title;
        String description;

        switch (type) {

            case EMAIL_VERIFICATION -> {
                subject = "StoreHub — Xác thực tài khoản";
                title = "Xác thực tài khoản";
                description =
                        "Cảm ơn bạn đã đăng ký StoreHub. " +
                                "Sử dụng mã xác thực bên dưới để hoàn tất việc đăng ký.";
            }

            case PASSWORD_RESET -> {
                subject = "StoreHub — Đặt lại mật khẩu";
                title = "Đặt lại mật khẩu";
                description =
                        "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản StoreHub. " +
                                "Sử dụng mã xác thực bên dưới để tiếp tục.";
            }

            default -> throw new IllegalArgumentException(
                    "Loại OTP không được hỗ trợ: " + type
            );
        }

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);

            String html = """
                    <!DOCTYPE html>
                    <html lang="vi">

                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport"
                              content="width=device-width, initial-scale=1.0">

                        <title>%s</title>
                    </head>

                    <body style="
                        margin: 0;
                        padding: 0;
                        background-color: #ffffff;
                        font-family: Arial, Helvetica, sans-serif;
                        color: #08090a;
                    ">

                        <div style="
                            width: 100%%;
                            background-color: #ffffff;
                            padding: 48px 20px;
                            box-sizing: border-box;
                        ">

                            <div style="
                                max-width: 560px;
                                margin: 0 auto;
                            ">

                                <div style="
                                    margin-bottom: 56px;
                                    font-size: 22px;
                                    line-height: 1;
                                    font-weight: 700;
                                    letter-spacing: -0.5px;
                                    color: #08090a;
                                ">
                                    Store<span style="color: #0c8c5e;">Hub</span>
                                </div>

                                <div style="
                                    margin-bottom: 16px;
                                    font-size: 12px;
                                    line-height: 1.5;
                                    font-weight: 600;
                                    letter-spacing: 1px;
                                    color: #0c8c5e;
                                    text-transform: uppercase;
                                ">
                                    StoreHub
                                </div>

                                <h1 style="
                                    margin: 0 0 20px 0;
                                    padding: 0;
                                    font-size: 40px;
                                    line-height: 1.15;
                                    font-weight: 600;
                                    letter-spacing: -0.8px;
                                    color: #08090a;
                                ">
                                    %s
                                </h1>

                                <p style="
                                    margin: 0 0 36px 0;
                                    padding: 0;
                                    font-size: 16px;
                                    line-height: 1.5;
                                    color: #52525b;
                                ">
                                    %s
                                </p>

                                <div style="
                                    padding: 28px 24px;
                                    margin-bottom: 28px;
                                    background-color: #f2f2f2;
                                    border: 1px solid #dddddd;
                                    border-radius: 16px;
                                    text-align: center;
                                ">

                                    <div style="
                                        margin-bottom: 12px;
                                        font-size: 12px;
                                        line-height: 1.5;
                                        font-weight: 600;
                                        letter-spacing: 0.8px;
                                        color: #71717a;
                                        text-transform: uppercase;
                                    ">
                                        Mã xác thực
                                    </div>

                                    <div style="
                                        font-size: 34px;
                                        line-height: 1.2;
                                        font-weight: 700;
                                        letter-spacing: 7px;
                                        color: #08090a;
                                    ">
                                        %s
                                    </div>

                                </div>

                                <div style="
                                    margin-bottom: 40px;
                                    padding-left: 16px;
                                    border-left: 3px solid #0c8c5e;
                                ">

                                    <p style="
                                        margin: 0;
                                        padding: 0;
                                        font-size: 14px;
                                        line-height: 1.5;
                                        color: #52525b;
                                    ">
                                        Mã xác thực có hiệu lực trong
                                        <strong style="
                                            color: #08090a;
                                            font-weight: 600;
                                        ">
                                            1 phút
                                        </strong>.
                                    </p>

                                    <p style="
                                        margin: 6px 0 0 0;
                                        padding: 0;
                                        font-size: 14px;
                                        line-height: 1.5;
                                        color: #71717a;
                                    ">
                                        Không chia sẻ mã này với bất kỳ ai.
                                    </p>

                                </div>

                                <div style="
                                    width: 100%%;
                                    height: 1px;
                                    margin-bottom: 24px;
                                    background-color: #f2f2f2;
                                "></div>

                                <p style="
                                    margin: 0;
                                    padding: 0;
                                    font-size: 13px;
                                    line-height: 1.6;
                                    color: #71717a;
                                ">
                                    Nếu bạn không thực hiện yêu cầu này,
                                    bạn có thể bỏ qua email này.
                                </p>

                                <div style="margin-top: 32px;">

                                    <p style="
                                        margin: 0;
                                        font-size: 13px;
                                        line-height: 1.5;
                                        color: #a1a1aa;
                                    ">
                                        © 2026 StoreHub
                                    </p>

                                </div>

                            </div>

                        </div>

                    </body>

                    </html>
                    """.formatted(
                    title,
                    title,
                    description,
                    otp
            );

            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {

            // Không throw RuntimeException ra request register nữa
            System.err.println("Không thể gửi email OTP tới " + email);
            e.printStackTrace();
        }
    }
}