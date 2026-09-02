package com.an.storehub.services;

import com.an.storehub.dto.request.UpdatePhoneRequest;
import com.an.storehub.dto.request.UpdateProfileRequest;
import com.an.storehub.dto.response.GetMyProfileResponse;
import com.an.storehub.dto.response.UpdatePhoneResponse;
import com.an.storehub.dto.response.UpdateProfileResponse;
import com.an.storehub.exceptions.AppException;
import com.an.storehub.models.User;
import com.an.storehub.repositories.UserRepository;
import com.an.storehub.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private CloudinaryService cloudinaryService;

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

    public UpdateProfileResponse updateProfile(Authentication authentication, @Valid UpdateProfileRequest request)  {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        if (request.getPhone() != null
                && repo.existsByPhoneAndIdNot(request.getPhone(), user.getId())) {

            throw new AppException(
                    "Số điện thoại đã được sử dụng",
                    400
            );
        }
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        try {

            if (request.getAvatar() != null
                    && !request.getAvatar().isEmpty()) {

                String avatarUrl =
                        cloudinaryService.uploadImage(request.getAvatar());

                user.setAvatar(avatarUrl);
            }

        } catch (IOException e) {

            throw new AppException(
                    "Không thể upload ảnh",
                    500
            );
        }
        repo.save(user);

        return new UpdateProfileResponse(user.getName(), user.getPhone(), user.getAvatar());
    }
}
