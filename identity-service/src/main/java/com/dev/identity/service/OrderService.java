package com.dev.identity.service;

import com.dev.identity.dto.request.OrderRequest;
import com.dev.identity.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getOrderList();

    Order createOrder(OrderRequest request);

    Order updateOrder(OrderRequest request, Integer id);

    void deleteOrder(Integer id);
}
