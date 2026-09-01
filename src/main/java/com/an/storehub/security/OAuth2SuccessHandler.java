package com.an.storehub.security;

import com.an.storehub.enums.Role;
import com.an.storehub.enums.UserStatus;
import com.an.storehub.models.User;
import com.an.storehub.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    public OAuth2SuccessHandler(
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauth2User =
                (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String avatar = oauth2User.getAttribute("picture");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {

                    User newUser = new User();

                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setAvatar(avatar);

                    // tùy enum của bạn
                    newUser.setRole(Role.CUSTOMER);
                    newUser.setStatus(UserStatus.ACTIVE);

                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(user);
        response.sendRedirect(
                "http://localhost:3000/oauth2/success?token="
                        + token
        );
    }
}