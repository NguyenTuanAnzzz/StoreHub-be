package com.an.storehub.controllers;

import com.an.storehub.dto.response.GetMyProfileResponse;
import com.an.storehub.services.AuthService;
import com.an.storehub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public GetMyProfileResponse getMyProfile(Authentication authentication){

        return service.getMyProfile(authentication);
    }

}
