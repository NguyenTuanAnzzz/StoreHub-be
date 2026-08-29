package com.an.storehub.controllers;

import com.an.storehub.dto.request.RegisterRequest;
import com.an.storehub.dto.request.VerifyOtpRequest;
import com.an.storehub.dto.response.RegisterResponse;
import com.an.storehub.dto.response.VerifyOtpResponse;
import com.an.storehub.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return service.register(request);

    }

    @PostMapping("/verify-otp")
    public VerifyOtpResponse verifyOtp (@Valid @RequestBody VerifyOtpRequest request){
        return service.verifyOtp(request);
    }

}