package com.dev.identity.service.impl;

import com.dev.commons.Message;
import com.dev.commons.exception.CustomException;
import com.dev.commons.response.ErrorModel;
import com.dev.constant.Constants;
import com.dev.identity.dto.request.OrderItemRequest;
import com.dev.identity.dto.request.OrderRequest;
import com.dev.identity.entity.Order;
import com.dev.identity.entity.OrderItem;
import com.dev.identity.entity.User;
import com.dev.identity.repository.OrderItemRepository;
import com.dev.identity.repository.OrderRepository;
import com.dev.identity.repository.ShopRepository;
import com.dev.identity.repository.UserRepository;
import com.dev.identity.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    ShopRepository shopRepository;
    OrderItemRepository orderItemRepository;

    @Override
    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderDate(new Date());
        User user = userRepository.findById(request.getUserId()).orElseThrow(() ->
                new CustomException(new ErrorModel(400, Message.User.USER_DOES_NOT_EXITED)));
        order.setUser(user);
        order.setStatus(Constants.OrderStatus.NEW);
        order.setTotalAmount(request.getTotalAmount());
        saveOrderItems(request, order);

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(OrderRequest request, Integer id) {
        // Tìm đơn hàng theo ID
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new CustomException(new ErrorModel(400, Message.Order.DOES_NOT_EXITED))
        );
        order.setTotalAmount(request.getTotalAmount() != null ? request.getTotalAmount() : order.getTotalAmount());

//        if (request.getStatus() != null) {
//            order.setStatus(request.getStatus());
//        }
        saveOrderItems(request,order);
        return orderRepository.save(order);
    }

    private void saveOrderItems(OrderRequest request, Order order) {
        order.getOrderItems().clear();
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : request.getOrderItems()) {
            var orderItem = new OrderItem();
            orderItem.setCouponId(orderItemRequest.getCouponId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setProductId(orderItemRequest.getProductId());
            orderItem.setOrder(order); // Liên kết OrderItem với Order
            orderItem.setShop(shopRepository.findById(orderItemRequest.getShopId()).orElseThrow(() ->
                    new CustomException(new ErrorModel(400, Message.Shop.DOES_NOT_EXITED))));

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
    }



    @Override
    public void deleteOrder(Integer id) {

    }
}
