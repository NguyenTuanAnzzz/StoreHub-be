package com.an.storehub.repositories;

import com.an.storehub.models.OtpVerification;
import com.an.storehub.models.Shop;
import com.an.storehub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String phone, Long id);
}
