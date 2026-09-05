package com.an.storehub.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String image;

    @Column(length = 255)
    private String description;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
}