package com.Ecommerce.store.serviceImplementation;

import com.Ecommerce.store.Utility.Helper;
import com.Ecommerce.store.dtos.CategotyDto;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.entities.Categories;
import com.Ecommerce.store.exceptions.ResourceNotFoundException;
import com.Ecommerce.store.repository.CategoryRepo;
import com.Ecommerce.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CategoryServiceImp implements CategoryService {
Logger logger = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public CategotyDto create(CategotyDto categotyDto) {
        Categories map = modelMapper.map(categotyDto, Categories.class);
        Categories save = categoryRepo.save(map);
        CategotyDto map1 = modelMapper.map(save, CategotyDto.class);


        return map1;
    }

    @Override
    public CategotyDto updateCategory(CategotyDto categotyDto, int categoryId) {
        Categories category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category ID is not found"));


         category.setTitle(categotyDto.getTitle());
         category.setDescription(categotyDto.getDescription());
         category.setCoverImage(categotyDto.getCoverImage());

        Categories save = categoryRepo.save(category);
        return modelMapper.map(save,CategotyDto.class);
    }

    @Override
    public void Delete(int categoryId) {
        Categories category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Id not found to deletion"));
        logger.info("category was deleted {}",categoryId);
        categoryRepo.delete(category);
    }

    @Override
    public PaegableResponse<CategotyDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortBy.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Categories> all = categoryRepo.findAll(pageable);
        PaegableResponse<CategotyDto> paegable = Helper.getPaegable(all, CategotyDto.class);
        return paegable;
    }

    @Override
    public CategotyDto getSingleCategory(int categoryId) {
        Categories category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Id is not found"));
        return modelMapper.map(category,CategotyDto.class);
    }
}
