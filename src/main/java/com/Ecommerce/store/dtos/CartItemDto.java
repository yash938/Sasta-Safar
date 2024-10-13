package com.Ecommerce.store.dtos;

import com.Ecommerce.store.entities.Cart;
import com.Ecommerce.store.entities.Product;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private int cardItemId;

    private int totalPrice;
    private int quantity;

    private ProductDto product;

}
