package com.an.storehub.dto.response;

import com.an.storehub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
}
