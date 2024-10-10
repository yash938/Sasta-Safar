package com.Ecommerce.store.serviceImplementation;

import com.Ecommerce.store.Utility.Helper;
import com.Ecommerce.store.dtos.CategotyDto;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.ProductDto;
import com.Ecommerce.store.entities.Categories;
import com.Ecommerce.store.entities.Product;
import com.Ecommerce.store.exceptions.ResourceNotFoundException;
import com.Ecommerce.store.repository.CategoryRepo;
import com.Ecommerce.store.repository.ProductRepo;
import com.Ecommerce.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp  implements ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        product.setAddedDate(new Date());
        Product save = productRepo.save(product);
        return modelMapper.map(save,ProductDto.class);
    }

        @Override
        public ProductDto updateProduct(ProductDto productDto, int productId) {
            Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Id is not found " + productId));

            product.setTitle(productDto.getTitle());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            product.setAddedDate(new Date());
            product.setDiscountPrice(productDto.getDiscountPrice());
            product.setLive(productDto.isLive());
            product.setStock(productDto.isStock());
            product.setProductImage(productDto.getProductImage());

            Product save = productRepo.save(product);
            return modelMapper.map(save,ProductDto.class) ;
        }

    @Override
    public ProductDto getSingleProduct(int productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found with given id " + productId));
        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public PaegableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortBy.equalsIgnoreCase("asc")? (Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending()) );
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> all = productRepo.findAll(pageRequest);
        PaegableResponse<ProductDto> paegable = Helper.getPaegable(all, ProductDto.class);
        return paegable;
    }

    @Override
    public void deleteProduct(int productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Id is not found " + productId));
        productRepo.delete(product);
        logger.info("Product is deleted with given id {}",product);
    }

    @Override
    public PaegableResponse<ProductDto> getLiveProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortBy.equalsIgnoreCase("asc")? (Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending()) );
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> all = productRepo.findByLiveTrue(pageRequest);
        PaegableResponse<ProductDto> paegable = Helper.getPaegable(all, ProductDto.class);
        return paegable;

    }

    @Override
    public PaegableResponse<ProductDto> searchByTitle(String title, int pageNumber,int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortBy.equalsIgnoreCase("asc")? (Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending()) );
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> all = productRepo.findByTitleContaining(title,pageRequest);
        PaegableResponse<ProductDto> paegable = Helper.getPaegable(all, ProductDto.class);
        return paegable;
    }

    @Override
    public ProductDto createWithCategory(int categoryId, ProductDto productDto) {
        Categories category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Id is not found"));
        Product product = modelMapper.map(productDto, Product.class);
        product.setAddedDate(new Date());
        product.setCategories(category);

        Product save = productRepo.save(product);


        return modelMapper.map(save,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(int categoryId, int productId) {
        Categories categories = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category id is not found" + categoryId));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product is is not found"));

        product.setCategories(categories);
        Product save = productRepo.save(product);
        return modelMapper.map(save,ProductDto.class);
    }

    @Override
    public PaegableResponse<ProductDto> getAllOfCategory(int categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {

        Categories categories = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category id is not found" + categoryId));

        Page<ProductDto> byCategory = productRepo.findByCategory(categories);
        return Helper.getPaegable(byCategory,ProductDto.class);
    }


}
