package com.Ecommerce.store.services;

import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.ProductDto;

public interface ProductService {
    //create Product
    ProductDto createProduct(ProductDto productDto);
     //update Product
    ProductDto updateProduct(ProductDto productDto,int productId);

    //single Product
    ProductDto getSingleProduct(int productId);

    //All Product
    PaegableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir);

    //delete Product
    void deleteProduct(int productId);

    PaegableResponse<ProductDto> getLiveProduct(int pageNumber,int pageSize,String sortBy,String sortDir);

    PaegableResponse<ProductDto> searchByTitle(String title,int pageNumber,int pageSize,String sortBy,String sortDir);
    ProductDto createWithCategory(int categoryId,ProductDto productDto);

    ProductDto updateProduct(int categoryId,int productId);
    PaegableResponse<ProductDto> getAllOfCategory(int categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
}
