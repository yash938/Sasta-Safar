package com.Ecommerce.store.services;

import com.Ecommerce.store.dtos.CategotyDto;
import com.Ecommerce.store.dtos.PaegableResponse;

import java.awt.print.Pageable;

public interface CategoryService {

    CategotyDto create(CategotyDto categotyDto);

    CategotyDto updateCategory(CategotyDto categotyDto,int categoryId);

    void Delete(int id);

    PaegableResponse<CategotyDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    CategotyDto getSingleCategory(int categoryId);



}
