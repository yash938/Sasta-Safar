package com.Ecommerce.store.controllers;

import com.Ecommerce.store.dtos.AddItemToCart;
import com.Ecommerce.store.dtos.CartDto;
import com.Ecommerce.store.exceptions.AllException;
import com.Ecommerce.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable int userId, @RequestBody AddItemToCart addItemToCart){
        CartDto cartDto = cartService.AddItemToCart(userId, addItemToCart);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    public ResponseEntity<AllException> removeItem(@PathVariable int userId,@PathVariable int cartItemId){
        cartService.removeItemToCart(userId,cartItemId);
        AllException cartIdIsDeleted = new AllException("cart id is deleted", true, HttpStatus.OK, LocalDate.now());
        return new ResponseEntity<>(cartIdIsDeleted,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    public ResponseEntity<AllException> clearCart(@PathVariable int userId){
        cartService.clearCart(userId);
        AllException cartIdIsClear = new AllException("cart is clear", true, HttpStatus.OK, LocalDate.now());
        return new ResponseEntity<>(cartIdIsClear,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    public ResponseEntity<CartDto> getCart(@PathVariable int userId){
        CartDto cardByUser = cartService.getCardByUser(userId);
        return new ResponseEntity<>(cardByUser,HttpStatus.OK);
    }
}
