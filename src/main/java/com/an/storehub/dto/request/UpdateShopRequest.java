package com.an.storehub.dto.request;

import com.an.storehub.enums.ShopRegion;
import com.an.storehub.enums.ShopStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShopRequest {

    @NotBlank(message = "Tên shop không được để trống")
    @Size(max = 100, message = "Tên shop tối đa 100 ký tự")
    private String name;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "Khu vực không được để trống")
    private ShopRegion region;

    @NotNull(message = "Trạng thái không được để trống")
    private ShopStatus status;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    private List<String> oldImages;

    private List<MultipartFile> images;
}