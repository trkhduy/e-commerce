package com.dev.identity.service;

import com.dev.identity.dto.request.ShopActiveRequest;
import com.dev.identity.dto.response.CountUserByMonth;
import com.dev.identity.entity.Shop;

import java.util.List;

public interface ShopService {

    List<Shop> getAll();

    Shop findById(Integer id);

}
