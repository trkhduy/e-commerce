package com.dev.identity.mapper;

import com.dev.identity.dto.request.ShopRequest;
import com.dev.identity.entity.Shop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    Shop toShop(ShopRequest request);
}
