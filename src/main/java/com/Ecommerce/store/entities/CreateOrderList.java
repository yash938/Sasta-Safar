package com.Ecommerce.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Cart Id is required")
    private int cartId;

    @NotBlank(message = "User Id is required")
    private int userId;
    private String orderStatus ="PENDING" ;

    private String paymentStatus = "NOTPAID";
    @NotBlank(message = "Address is required")
    private String billingAddress;

    @NotBlank(message = "Name is required")
    private String billingName;

    @NotBlank(message = "Phone no is required")
    private long billingPhone;


    //agar order fetch kare toh user info bhi aajae fetch lagane pr

}
