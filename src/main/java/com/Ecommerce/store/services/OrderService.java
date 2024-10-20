package com.Ecommerce.store.services;

import com.Ecommerce.store.dtos.OrderDto;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.entities.CreateOrderList;

import java.util.List;


public interface OrderService {

    OrderDto createOrder(CreateOrderList createOrderList);


    void removeOrder(int orderId);

    List<OrderDto> getUserOrder(int userId);

    PaegableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);


    List<OrderDto> updateOrder(CreateOrderList createOrderList);

}
