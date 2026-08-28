package com.an.storehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
}