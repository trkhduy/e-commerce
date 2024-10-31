package com.dev.identity.service.impl;

import com.dev.commons.Message;
import com.dev.commons.exception.CustomException;
import com.dev.commons.response.ErrorModel;
import com.dev.identity.dto.request.ShopActiveRequest;
import com.dev.identity.dto.response.CountUserByMonth;
import com.dev.identity.entity.Shop;
import com.dev.identity.repository.ShopRepository;
import com.dev.identity.service.ShopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShopServiceImpl implements ShopService {

    ShopRepository shopRepository;

    @Override
    public List<Shop> getAll() {
        return shopRepository.findAll();
    }

    @Override
    public Shop findById(Integer id) {
        return shopRepository.findById(id).orElseThrow(() ->
                new CustomException(new ErrorModel(400, Message.Shop.DOES_NOT_EXITED)));
    }

}
