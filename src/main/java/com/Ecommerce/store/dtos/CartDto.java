package com.Ecommerce.store.dtos;

import com.Ecommerce.store.entities.CartItem;
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
public class CartDto {
    private int cartId;
    private Date createdAt;
    private UserDto user;
    private List<CartItemDto> cartItems = new ArrayList<>();
}
