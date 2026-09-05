package com.an.storehub.dto.response;

import com.an.storehub.enums.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShopResponse {
    private Long id;
    private String name;
    private ShopStatus status;
    private String sellerName;
    private String avatar;
    private LocalDateTime createdAt;
    private String message;
}