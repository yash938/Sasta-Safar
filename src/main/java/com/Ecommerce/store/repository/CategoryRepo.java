package com.Ecommerce.store.repository;

import com.Ecommerce.store.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepo extends JpaRepository<Categories,Integer> {
}
