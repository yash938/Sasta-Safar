package com.Ecommerce.store.repository;

import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.ProductDto;
import com.Ecommerce.store.entities.Categories;
import com.Ecommerce.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

   Page<Product> findByTitleContaining(String title,Pageable pageable);
   Page<Product> findByLiveTrue(Pageable pageable);


   Page<Product> findByCategories(Categories categories,Pageable pageable);
}
