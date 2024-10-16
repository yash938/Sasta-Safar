package com.Ecommerce.store.repository;

import com.Ecommerce.store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
}
