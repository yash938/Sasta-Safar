package com.Ecommerce.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddItemToCart {

    private int productId;
    private int quantity;
}

