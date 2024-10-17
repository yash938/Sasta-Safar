package com.Ecommerce.store.dtos;

import com.Ecommerce.store.entities.Order;
import com.Ecommerce.store.entities.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {


    private int orderItemId;

    private int quantity;
    private int totalPrice;


    private ProductDto product;


}
