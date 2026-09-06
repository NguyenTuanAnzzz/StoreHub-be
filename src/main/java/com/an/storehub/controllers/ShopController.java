package com.an.storehub.controllers;

import com.an.storehub.dto.request.CreateShopRequest;
import com.an.storehub.dto.response.CreateShopResponse;
import com.an.storehub.services.ShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private ShopService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CreateShopResponse createShop(
            @Valid @ModelAttribute CreateShopRequest request
    ) {
        return service.createShop(request);
    }
}