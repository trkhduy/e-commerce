package com.dev.identity.controller;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.ShopActiveRequest;
import com.dev.identity.service.ShopService;
import com.dev.identity.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShopController {

    ShopService shopService;
    UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, shopService.getAll()));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PutMapping("/disableShop")
    public ResponseEntity<?> disableShop(@RequestBody ShopActiveRequest request) {
        String message = userService.updateStatus(request) ? Message.User.UPDATE_STATUS : Message.User.UPDATE_STATUS_FAILED;
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, message));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(@PathVariable Integer id) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, shopService.findById(id)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }


}
