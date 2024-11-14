package com.Ecommerce.store.repository;

import com.Ecommerce.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    Optional<Role>  findByName(String name);
}
