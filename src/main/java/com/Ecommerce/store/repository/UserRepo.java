package com.Ecommerce.store.repository;

import com.Ecommerce.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
