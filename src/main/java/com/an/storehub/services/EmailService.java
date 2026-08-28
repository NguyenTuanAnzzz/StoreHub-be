package com.an.storehub.services;

import com.an.storehub.enums.OtpType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email, String otp, OtpType type) {

        String subject;
        String title;
        String description;

        switch (type) {

            case EMAIL_VERIFICATION -> {
                subject = "StoreHub — Xác thực tài khoản";
                title = "Xác thực tài khoản";
                description =
                        "Cảm ơn bạn đã đăng ký tài khoản StoreHub. " +
                                "Vui lòng sử dụng mã OTP bên dưới để hoàn tất việc xác thực email.";
            }

            case PASSWORD_RESET -> {
                subject = "StoreHub — Đặt lại mật khẩu";
                title = "Đặt lại mật khẩu";
                description =
                        "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản StoreHub. " +
                                "Sử dụng mã OTP bên dưới để tiếp tục.";
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
                        background-color: #f4f4f5;
                        font-family: Arial, Helvetica, sans-serif;
                        color: #09090b;
                    ">

                        <div style="
                            padding: 48px 20px;
                            background-color: #f4f4f5;
                        ">

                            <div style="
                                max-width: 560px;
                                margin: 0 auto;
                                background-color: #ffffff;
                                border: 1px solid #ececee;
                                border-radius: 36px;
                                padding: 40px;
                            ">

                                <!-- Logo -->

                                <div style="
                                    margin-bottom: 32px;
                                    font-size: 24px;
                                    font-weight: 700;
                                    color: #09090b;
                                ">
                                    Store<span style="color: #ff5a00;">
                                        Hub
                                    </span>
                                </div>


                                <!-- Badge -->

                                <div style="
                                    display: inline-block;
                                    padding: 5px 10px;
                                    margin-bottom: 20px;
                                    background-color: #ff5a00;
                                    color: #ffffff;
                                    border-radius: 12px;
                                    font-size: 12px;
                                    font-weight: 600;
                                ">
                                    STOREHUB
                                </div>


                                <!-- Title -->

                                <h1 style="
                                    margin: 0 0 16px 0;
                                    font-size: 40px;
                                    line-height: 1.2;
                                    font-weight: 700;
                                    letter-spacing: -1px;
                                    color: #09090b;
                                ">
                                    %s
                                </h1>


                                <!-- Description -->

                                <p style="
                                    margin: 0 0 32px 0;
                                    font-size: 15px;
                                    line-height: 1.6;
                                    color: #52525b;
                                ">
                                    %s
                                </p>


                                <!-- OTP -->

                                <div style="
                                    padding: 28px;
                                    margin-bottom: 28px;
                                    background-color: #f4f4f5;
                                    border: 1px solid #ececee;
                                    border-radius: 20px;
                                    text-align: center;
                                ">

                                    <div style="
                                        margin-bottom: 10px;
                                        font-size: 12px;
                                        color: #71717a;
                                        font-weight: 600;
                                    ">
                                        MÃ XÁC THỰC
                                    </div>

                                    <div style="
                                        font-size: 36px;
                                        line-height: 1.2;
                                        font-weight: 700;
                                        letter-spacing: 8px;
                                        color: #09090b;
                                    ">
                                        %s
                                    </div>

                                </div>


                                <!-- Warning -->

                                <div style="
                                    padding: 16px 20px;
                                    margin-bottom: 28px;
                                    background-color: #fafafa;
                                    border-radius: 14px;
                                ">

                                    <p style="
                                        margin: 0;
                                        font-size: 13px;
                                        line-height: 1.6;
                                        color: #52525b;
                                    ">
                                        <strong style="color: #18181b;">
                                            Lưu ý:
                                        </strong>

                                        Mã OTP có hiệu lực trong
                                        <strong style="color: #18181b;">
                                            1 phút
                                        </strong>.

                                        Không chia sẻ mã này với bất kỳ ai.
                                    </p>

                                </div>


                                <!-- Divider -->

                                <div style="
                                    height: 1px;
                                    margin-bottom: 24px;
                                    background-color: #ececee;
                                "></div>


                                <!-- Footer -->

                                <p style="
                                    margin: 0;
                                    font-size: 13px;
                                    line-height: 1.6;
                                    color: #71717a;
                                ">
                                    Nếu bạn không thực hiện yêu cầu này,
                                    bạn có thể bỏ qua email này.
                                </p>

                                <p style="
                                    margin: 20px 0 0 0;
                                    font-size: 13px;
                                    color: #a1a1aa;
                                ">
                                    © 2026 StoreHub
                                </p>

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

            throw new RuntimeException(
                    "Không thể gửi email OTP",
                    e
            );
        }
    }
}

