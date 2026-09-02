package com.an.storehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProfileResponse {

    private String name;
    private String phone;
    private String avatar;
}