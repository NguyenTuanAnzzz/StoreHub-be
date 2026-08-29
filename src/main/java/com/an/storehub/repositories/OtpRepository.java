package com.an.storehub.repositories;

import com.an.storehub.enums.OtpType;
import com.an.storehub.models.OtpVerification;
import com.an.storehub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    // Lấy OTP mới nhất của user theo loại OTP
    Optional<OtpVerification> findTopByUserAndTypeOrderByCreatedAtDesc(
            User user,
            OtpType type
    );

    // Kiểm tra user có OTP đang tồn tại theo loại hay không
    boolean existsByUserAndTypeAndUsedFalse(
            User user,
            OtpType type
    );

}

