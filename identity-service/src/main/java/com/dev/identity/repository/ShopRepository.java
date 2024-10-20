package com.dev.identity.repository;

import com.dev.identity.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, String> {
    boolean existsShopByShopName(String shopName);

    boolean existsShopByContactMail(String contactEmail);
}
