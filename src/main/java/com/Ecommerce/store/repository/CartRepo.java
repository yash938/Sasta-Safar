package com.Ecommerce.store.repository;

import com.Ecommerce.store.entities.Cart;
import com.Ecommerce.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {

    Optional<Cart> findByUser(User user);
}
