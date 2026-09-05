package com.an.storehub.services;

import com.an.storehub.dto.request.CreateShopRequest;
import com.an.storehub.dto.response.CreateShopResponse;
import com.an.storehub.exceptions.AppException;
import com.an.storehub.models.Shop;
import com.an.storehub.models.ShopImage;
import com.an.storehub.models.User;
import com.an.storehub.repositories.ShopRepository;
import com.an.storehub.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ShopService {
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ShopRepository repo;

    public CreateShopResponse createShop(CreateShopRequest request, UserPrincipal userPrincipal) {
        if(repo.existsByName(request.getName())){
            throw new AppException("Tên cửa hàng đã tồn tại", 409);
        }

        User staff = userPrincipal.getUser();
        Shop shop = Shop.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .phone(request.getPhone())
                .businessLicense(request.getBusinessLicense())
                .staff(staff)
                .build();

        if(request.getImages() != null){
            for(int i = 0; i < request.getImages().size(); i++){
                try {
                    MultipartFile file = request.getImages().get(i);

                    String imageUrl = cloudinaryService.uploadImage(file);
                    ShopImage shopImage = ShopImage.builder()
                            .image(imageUrl)
                            .displayOrder(i)
                            .shop(shop)
                            .build();

                    shop.getImages().add(shopImage);
                }catch (IOException e) {

                    throw new AppException(
                            "Không thể upload ảnh",
                            500
                    );
                }
            }


        }
        Shop savedShop = repo.save(shop);

        String avatar = savedShop.getImages().isEmpty()
                ? null
                : savedShop.getImages().get(0).getImage();

        return new CreateShopResponse(
                savedShop.getId(),
                savedShop.getName(),
                savedShop.getStatus(),
                savedShop.getStaff().getName(),
                avatar,
                savedShop.getCreatedAt(),
                "Tạo cửa hàng thành công"
        );
    }
}
