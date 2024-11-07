package com.dev.identity.controller;

import com.dev.commons.Message;
import com.dev.commons.response.DataResponse;
import com.dev.commons.response.ResultModel;
import com.dev.identity.dto.request.OrderRequest;
import com.dev.identity.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, orderService.getOrderList()));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated OrderRequest request) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, orderService.createOrder(request)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Validated OrderRequest request, @PathVariable Integer id) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, orderService.updateOrder(request, id)));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(true);
        dataResponse.setResult(new ResultModel<>(null, Message.Order.DELETE));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

}
