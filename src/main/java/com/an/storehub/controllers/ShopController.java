package com.an.storehub.controllers;

import com.an.storehub.dto.request.CreateShopRequest;
import com.an.storehub.dto.request.UpdateShopRequest;
import com.an.storehub.dto.response.CreateShopResponse;
import com.an.storehub.dto.response.ShopAdminResponse;
import com.an.storehub.enums.ShopRegion;
import com.an.storehub.enums.ShopStatus;
import com.an.storehub.services.ShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<ShopAdminResponse> getAllShops(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ShopRegion region,
            @RequestParam(required = false) ShopStatus status,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return service.getAllShop(keyword, region, status, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail/{id}")
    public ShopAdminResponse getShopById(
            @PathVariable Long id
    ) {
        return service.getShopById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ShopAdminResponse UpdateShopById(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateShopRequest request
    ){
        return service.updateShopById(id, request);
    }
}