package com.Ecommerce.store.services;

import com.Ecommerce.store.dtos.AddItemToCart;
import com.Ecommerce.store.dtos.CartDto;

public interface CartService {

    CartDto AddItemToCart(int userId, AddItemToCart itemToCart);

    void removeItemToCart(int userId,int cartItem);

    void clearCart(int userId);

    CartDto getCardByUser(int userId);
}
