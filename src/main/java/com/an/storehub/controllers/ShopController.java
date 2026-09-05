package com.an.storehub.controllers;

import com.an.storehub.dto.request.CreateShopRequest;
import com.an.storehub.dto.response.CreateShopResponse;
import com.an.storehub.security.UserPrincipal;
import com.an.storehub.services.ShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private ShopService service;

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping
    public CreateShopResponse createShop(
            @Valid @ModelAttribute CreateShopRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return service.createShop(request, userPrincipal);
    }
}
