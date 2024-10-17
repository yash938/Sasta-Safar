package com.Ecommerce.store.dtos;

import com.Ecommerce.store.entities.OrderItems;
import com.Ecommerce.store.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private int orderId;

    //PENDING,DISPATCH,DELIVERED
    private String orderStatus = "PENDING";

    private String paymentStatus = "NOTPAID";

    private int orderAmount;

    private String billingAddress;

    private String billingName;
    private Date orderDate = new Date();
    private Date deliveredDate;

    private UserDto user;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}
