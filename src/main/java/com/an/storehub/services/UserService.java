package com.an.storehub.services;

import com.an.storehub.dto.response.GetMyProfileResponse;
import com.an.storehub.models.User;
import com.an.storehub.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public GetMyProfileResponse getMyProfile(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        User user = principal.getUser();

        return new GetMyProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}
