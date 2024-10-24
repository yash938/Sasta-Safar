package com.Ecommerce.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderList

{

    @NotNull(message = "Cart Id is required")
    private int cartId;

    @NotNull(message = "User Id is required")
    private int userId;
    private String orderStatus ="PENDING" ;

    private String paymentStatus = "NOTPAID";
    @NotBlank(message = "Address is required")
    private String billingAddress;

    @NotBlank(message = "Name is required")
    private String billingName;

    @NotNull(message = "Phone no is required")
    @Column(length = 10)
    private long billingPhone;


    //agar order fetch kare toh user info bhi aajae fetch lagane pr

}
