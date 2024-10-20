package com.Ecommerce.store.repository;

import com.Ecommerce.store.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItems,Integer> {

}
