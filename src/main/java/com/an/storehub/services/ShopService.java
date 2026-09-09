package com.an.storehub.services;

import com.an.storehub.dto.request.CreateShopRequest;
import com.an.storehub.dto.response.CreateShopResponse;
import com.an.storehub.dto.response.ShopAdminResponse;
import com.an.storehub.enums.ShopRegion;
import com.an.storehub.enums.ShopStatus;
import com.an.storehub.exceptions.AppException;
import com.an.storehub.models.Shop;
import com.an.storehub.models.ShopImage;
import com.an.storehub.repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;

@Service
public class ShopService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ShopRepository repo;

    public CreateShopResponse createShop(CreateShopRequest request) {

        if (repo.existsByName(request.getName())) {
            throw new AppException(
                    "Tên cửa hàng đã tồn tại",
                    409
            );
        }

        if (repo.existsByPhone(request.getPhone())) {
            throw new AppException(
                    "Số điện thoại cửa hàng đã tồn tại",
                    409
            );
        }

        // Tạo Shop
        Shop shop = Shop.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .phone(request.getPhone())
                .region(request.getRegion())
                .status(request.getStatus())
                .build();


        // Upload ảnh
        if (request.getImages() != null) {

            for (int i = 0; i < request.getImages().size(); i++) {

                try {

                    MultipartFile file = request.getImages().get(i);

                    String imageUrl =
                            cloudinaryService.uploadImage(file);

                    ShopImage shopImage = ShopImage.builder()
                            .image(imageUrl)
                            .displayOrder(i)
                            .shop(shop)
                            .build();

                    shop.getImages().add(shopImage);

                } catch (IOException e) {

                    throw new AppException(
                            "Không thể upload ảnh",
                            500
                    );
                }
            }
        }


        // Lưu Shop
        Shop savedShop = repo.save(shop);


        // Lấy ảnh đầu tiên làm avatar
        String avatar = savedShop.getImages().isEmpty()
                ? null
                : savedShop.getImages()
                .get(0)
                .getImage();


        // Response
        return new CreateShopResponse(
                savedShop.getId(),
                savedShop.getName(),
                savedShop.getRegion(),
                savedShop.getStatus(),
                savedShop.getAddress(),
                savedShop.getPhone(),
                avatar,
                savedShop.getCreatedAt(),
                "Tạo cửa hàng thành công"
        );
    }



    public Page<ShopAdminResponse> getAllShop(
            String keyword,
            ShopRegion region,
            ShopStatus status,
            Pageable pageable
    ) {
        List<Shop> shops = repo.findAll();

        List<ShopAdminResponse> responses =  shops.stream()
                .filter(shop ->
                        keyword == null ||
                                shop.getName()
                                        .toLowerCase()
                                        .contains(keyword.toLowerCase())
                )
                .filter(shop ->
                        region == null ||
                                shop.getRegion() == region
                )
                .filter(shop ->
                        status == null ||
                                shop.getStatus() == status
                )
                .map(shop -> {
                    List<String> avatar = shop.getImages().isEmpty()
                            ? List.of()
                            : shop.getImages().stream().map(image ->(image.getImage())).toList();

                    return new ShopAdminResponse(
                            shop.getId(),
                            shop.getName(),
                            shop.getPhone(),
                            shop.getAddress(),
                            shop.getRegion(),
                            shop.getStatus(),
                            shop.getDescription(),
                            avatar,
                            shop.getCreatedAt(),
                            shop.getUpdatedAt()
                    );
                })
                .toList();

        int start = (int) pageable.getOffset();

        if (start >= responses.size()) {
            return new PageImpl<>(
                    List.of(),
                    pageable,
                    responses.size()
            );
        }

        int end = Math.min(
                start + pageable.getPageSize(),
                responses.size()
        );

        List<ShopAdminResponse> pageContent =
                responses.subList(start, end);

        return new PageImpl<>(
                pageContent,
                pageable,
                responses.size()
        );
    }


    public ShopAdminResponse getShopById(Long id) {
        Shop shop = repo.findById(id).orElseThrow(() -> new AppException("Shop không tồn tại", 404));
        List<String> images = shop.getImages().stream().map(image -> (image.getImage())).toList();
        return new ShopAdminResponse(shop.getId(), shop.getName(), shop.getPhone(), shop.getAddress(), shop.getRegion(),shop.getStatus(), shop.getDescription(), images, shop.getCreatedAt(), shop.getUpdatedAt());
    }
}