package com.Ecommerce.store.entities;

import jakarta.persistence.*;
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

    private int cartId;
    private int userId;
    private String orderStatus ="PENDING" ;

    private String paymentStatus = "NOTPAID";
    private String billingAddress;

    private String billingName;
    private long billingPhone;


    //agar order fetch kare toh user info bhi aajae fetch lagane pr

}
