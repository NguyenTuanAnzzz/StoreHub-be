package com.an.storehub.controllers;

import com.an.storehub.dto.request.UpdatePhoneRequest;
import com.an.storehub.dto.response.GetMyProfileResponse;
import com.an.storehub.dto.response.UpdatePhoneResponse;
import com.an.storehub.dto.response.UpdateProfileResponse;
import com.an.storehub.dto.request.UpdateProfileRequest;
import com.an.storehub.services.AuthService;
import com.an.storehub.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public GetMyProfileResponse getMyProfile(Authentication authentication){

        return service.getMyProfile(authentication);
    }

    @PutMapping("update-phone")
    public UpdatePhoneResponse updatePhone(Authentication authentication, @Valid @RequestBody UpdatePhoneRequest request){
        return service.updatePhone(authentication ,request);
    }

    @PutMapping(value = "/update-profile", consumes = "multipart/form-data")
    public UpdateProfileResponse updateProfile(
            Authentication authentication,
            @Valid @ModelAttribute UpdateProfileRequest request
    ) {
        return service.updateProfile(authentication, request);
    }

}
