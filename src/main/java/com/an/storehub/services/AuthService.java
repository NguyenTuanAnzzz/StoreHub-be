package com.an.storehub.services;

import com.an.storehub.dto.request.LoginRequest;
import com.an.storehub.dto.request.RegisterRequest;
import com.an.storehub.dto.request.VerifyOtpRequest;
import com.an.storehub.dto.response.LoginResponse;
import com.an.storehub.dto.response.RegisterResponse;
import com.an.storehub.dto.response.VerifyOtpResponse;
import com.an.storehub.enums.OtpType;
import com.an.storehub.enums.Role;
import com.an.storehub.enums.UserStatus;
import com.an.storehub.exceptions.AppException;
import com.an.storehub.models.OtpVerification;
import com.an.storehub.models.User;
import com.an.storehub.repositories.OtpRepository;
import com.an.storehub.repositories.UserRepository;
import com.an.storehub.security.JwtService;
import com.an.storehub.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new AppException("Email đã tồn tại", 409);
        }

        if (userRepo.existsByPhone(request.getPhone())) {
            throw new AppException("Số điện thoại đã tồn tại", 409);
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(encodedPassword);
        user.setRole(Role.CUSTOMER);

        user.setStatus(UserStatus.PENDING);



        User savedUser = userRepo.save(user);

        String otp = generateOtp();

        OtpVerification verification = new OtpVerification();

        verification.setOtp( passwordEncoder.encode(otp) );
        verification.setType(OtpType.EMAIL_VERIFICATION);

        verification.setExpiresAt( LocalDateTime.now().plusMinutes(1) );

        verification.setCooldownUntil( LocalDateTime.now().plusMinutes(1) );

        verification.setUsed(false);
        verification.setCreatedAt(LocalDateTime.now());
        verification.setUser(savedUser);
        otpRepository.save(verification);
        emailService.sendOtp(
                savedUser.getEmail(),
                otp,
                OtpType.EMAIL_VERIFICATION
        );
        return new RegisterResponse( savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPhone(), savedUser.getRole().name(), savedUser.getStatus().name() );
    }

    public String generateOtp() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            otp.append(characters.charAt(index));
        }

        return otp.toString();
    }

    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("User không tồn tại", 404));
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new AppException("User đã được kích hoạt", 400);
        }
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new AppException("User đã bị khóa", 400);
        }

        OtpVerification otpVerification = otpRepository
                .findTopByUserAndTypeOrderByCreatedAtDesc(user, OtpType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new AppException(
                        "OTP không chính xác",
                        400
                ));


        VerifyOtpResponse response = new VerifyOtpResponse();
        if (!otpVerification.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new AppException(
                    "OTP đã hết hạn",
                    400
            );
        }

        if (!passwordEncoder.matches(
                request.getOtp(),
                otpVerification.getOtp()
        )) {
            throw new AppException(
                    "OTP không chính xác",
                    400
            );
        }

        user.setStatus(UserStatus.ACTIVE);
        userRepo.save(user);

        response.setMessage("Xác thực mã OTP thành công");
        return response;

    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        User user = principal.getUser();

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token
        );
    }
}