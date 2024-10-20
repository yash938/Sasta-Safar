package com.Ecommerce.store.repository;

import com.Ecommerce.store.entities.Order;
import com.Ecommerce.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Integer> {

    List<Order> findByUser(User user);
}
