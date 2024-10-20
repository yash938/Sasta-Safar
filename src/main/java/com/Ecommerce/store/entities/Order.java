package com.Ecommerce.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "order_id")
     private int orderId;

     //PENDING,DISPATCH,DELIVERED
     private String orderStatus;

     private String paymentStatus;

     private int orderAmount;
     @Column(length = 1000)
     private String billingAddress;

     private String billingName;
     private long billingPhone;
     private Date orderDate;
     private Date deliveredDate;

     //agar order fetch kare toh user info bhi aajae fetch lagane pr
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "user_id")
     private User user;

     @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
     private List<OrderItems> orderItems = new ArrayList<>();

}
