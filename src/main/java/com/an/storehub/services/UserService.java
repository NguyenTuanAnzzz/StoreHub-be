package com.an.storehub.services;

import com.an.storehub.dto.request.UpdatePhoneRequest;
import com.an.storehub.dto.response.GetMyProfileResponse;
import com.an.storehub.dto.response.UpdatePhoneResponse;
import com.an.storehub.exceptions.AppException;
import com.an.storehub.models.User;
import com.an.storehub.repositories.UserRepository;
import com.an.storehub.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public GetMyProfileResponse getMyProfile(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        User user = principal.getUser();

        return new GetMyProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatar(),
                user.getRole()
        );
    }

    public UpdatePhoneResponse updatePhone(Authentication authentication, UpdatePhoneRequest request) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        User user = principal.getUser();

        if(repo.existsByPhone(request.getPhone())){
            throw new AppException(
                    "Số điện thoại đã được sử dụng",
                    400
            );
        }

        user.setPhone(request.getPhone());
        repo.save(user);

        return new UpdatePhoneResponse("Cập nhật số điện thoại thành công", user.getPhone());
    }
}
