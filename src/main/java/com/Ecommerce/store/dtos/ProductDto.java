package com.Ecommerce.store.dtos;

import com.Ecommerce.store.entities.Categories;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductDto {

    private int productId;

    @Size(min = 2,max = 500)
    @NotBlank(message = "title is required")
    private String title;

    @Size(min = 3,max = 10000)
    @NotBlank(message = "description is required")
    private String description;


    @Min(value = 1,message = "price must be atleast 1")
    private int price;

    private int discountPrice;

    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;
    private CategotyDto categories;
}
