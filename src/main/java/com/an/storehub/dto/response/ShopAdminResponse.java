package com.an.storehub.dto.response;

import com.an.storehub.enums.ShopRegion;
import com.an.storehub.enums.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ShopAdminResponse {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private ShopRegion region;
    private ShopStatus status;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}