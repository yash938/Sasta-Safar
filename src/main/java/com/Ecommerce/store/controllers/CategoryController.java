package com.Ecommerce.store.controllers;

import com.Ecommerce.store.dtos.CategotyDto;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.UserDto;
import com.Ecommerce.store.exceptions.AllException;
import com.Ecommerce.store.exceptions.ImageResponse;
import com.Ecommerce.store.services.CategoryService;
import com.Ecommerce.store.services.ImageFile;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageFile imageFile;

    @Value("${category.image.path}")
    private String imagePath;

    @PostMapping("/create")
     public ResponseEntity<CategotyDto> createCategory(@Valid @RequestBody CategotyDto categotyDto){
         CategotyDto categotyDto1 = categoryService.create(categotyDto);
         return new ResponseEntity<>(categotyDto1, HttpStatus.CREATED);
     }

     @PutMapping("/update/{categoryId}")
     public ResponseEntity<CategotyDto> updateCategory(@PathVariable int categoryId ,@RequestBody CategotyDto categotyDto ){
         CategotyDto categotyDto1 = categoryService.updateCategory(categotyDto, categoryId);
         return new ResponseEntity<>(categotyDto1,HttpStatus.OK);
     }

     @DeleteMapping("/delete/{categoryId}")
     public ResponseEntity<AllException> deleteCategory(@PathVariable int categoryId){
        categoryService.Delete(categoryId);
        logger.info("category is deleted with given id :{}",categoryId);
         AllException categoryDeletedSuccessfully = new AllException("Category deleted successfully", true, HttpStatus.OK, LocalDate.now());
         return new ResponseEntity<>(categoryDeletedSuccessfully,HttpStatus.OK);
     }

     @GetMapping("/single/{categoryId}")
     public ResponseEntity<CategotyDto> getSingleCategory(@PathVariable int categoryId){
         CategotyDto singleCategory = categoryService.getSingleCategory(categoryId);
         return new ResponseEntity<>(singleCategory,HttpStatus.OK);
     }


     @GetMapping("/getAll")
     public ResponseEntity<PaegableResponse<CategotyDto>> getAll(
             @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
             @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
             @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
             @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
         PaegableResponse<CategotyDto> all = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
         return new ResponseEntity<>(all,HttpStatus.OK);
     }



     //upload image file
     @PostMapping("/categoryImage/{categoryId}")
     public ResponseEntity<ImageResponse> uploadFile(@PathVariable int categoryId,@RequestParam("categoryImage") MultipartFile categoryImage){
         String fileImage = imageFile.UploadFile(categoryImage, imagePath);
         CategotyDto singleCategory = categoryService.getSingleCategory(categoryId);
         singleCategory.setCoverImage(fileImage);

         CategotyDto categotyDto = categoryService.updateCategory(singleCategory, categoryId);
         ImageResponse imageResponse = new ImageResponse(fileImage,"Image upload successfully",true,HttpStatus.OK, LocalDate.now());
         return new ResponseEntity<>(imageResponse,HttpStatus.OK);
     }



    @GetMapping("/image/{categoryId}")
    public void ServeImage(@PathVariable int categoryId, HttpServletResponse response) throws IOException {
        CategotyDto singleCategory = categoryService.getSingleCategory(categoryId);
        logger.info("user image name {}",singleCategory.getCoverImage());
        InputStream resource = imageFile.getResource(imagePath, singleCategory.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response. getOutputStream());
    }


}
