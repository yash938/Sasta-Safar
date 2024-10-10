package com.Ecommerce.store.controllers;

import com.Ecommerce.store.dtos.CategotyDto;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.dtos.ProductDto;
import com.Ecommerce.store.exceptions.AllException;
import com.Ecommerce.store.exceptions.ImageResponse;
import com.Ecommerce.store.services.CategoryService;
import com.Ecommerce.store.services.ImageFile;
import com.Ecommerce.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
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


@RestController
@RequestMapping("/product")
public class ProductController {
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ImageFile imageFile;

    @Value("${product.image.path}")
    private String imagePath;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;



    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){
        ProductDto product = productService.createProduct(productDto);
        return  new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody  ProductDto productDto, @PathVariable  int productId){
        ProductDto productDto1 = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<AllException> deleteProduct(@PathVariable int productId){
        productService.deleteProduct(productId);
        AllException productIsDeletedSuccessfully = new AllException("Product is deleted successfully", true, HttpStatus.OK, LocalDate.now());
        return new ResponseEntity<>(productIsDeletedSuccessfully,HttpStatus.OK);
    }

    @GetMapping("/getSingle/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable int productId){
        ProductDto singleProduct = productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PaegableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PaegableResponse<ProductDto> allProduct = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct,HttpStatus.OK);
    }


    @GetMapping("/allLive")
    public ResponseEntity<PaegableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PaegableResponse<ProductDto> liveProduct = productService.getLiveProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(liveProduct,HttpStatus.OK);
    }


    @GetMapping("/byTitle/{title}")
    public ResponseEntity<PaegableResponse<ProductDto>> getTitleName(
            @PathVariable String title,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PaegableResponse<ProductDto> productDtoPaegableResponse = productService.searchByTitle(title, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoPaegableResponse,HttpStatus.OK);

    }


    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable int productId, @RequestParam("productImage")MultipartFile multipartFile){
        String fileUpload = imageFile.UploadFile(multipartFile, imagePath);
        ProductDto singleProduct = productService.getSingleProduct(productId);
        singleProduct.setProductImage(fileUpload);
        ProductDto productDto = productService.updateProduct(singleProduct, productId);
        ImageResponse imageResponse = new ImageResponse(fileUpload, "Image is successfully Uploaded", true, HttpStatus.OK, LocalDate.now());
        return new ResponseEntity<>(imageResponse,HttpStatus.OK);
    }

    @GetMapping("/image/{productId}")
    public void ServeImage(@PathVariable int productId, HttpServletResponse response) throws IOException {
        ProductDto singleProduct = productService.getSingleProduct(productId);
        logger.info("user image name {}",singleProduct.getProductImage());
        InputStream resource = imageFile.getResource(imagePath, singleProduct.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response. getOutputStream());
    }



}
