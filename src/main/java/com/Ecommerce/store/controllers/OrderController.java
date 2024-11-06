package com.Ecommerce.store.controllers;

import com.Ecommerce.store.dtos.OrderDto;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.entities.CreateOrderList;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.exceptions.AllException;
import com.Ecommerce.store.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderList createOrderList){
        OrderDto order = orderService.createOrder(createOrderList);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AllException> removeItem(int orderId){
        orderService.removeOrder(orderId);
        AllException removeOrder = new AllException("Order removed successfully", true, HttpStatus.OK, LocalDate.now());
        return new ResponseEntity<>(removeOrder,HttpStatus.OK);
    }

    @GetMapping("/{userId}")

    public ResponseEntity<List<OrderDto>> getUser(@PathVariable int userId){
        List<OrderDto> userOrder = orderService.getUserOrder(userId);
        return new ResponseEntity<>(userOrder,HttpStatus.OK);
    }


    @GetMapping("/allOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaegableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PaegableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);

    }


    @PutMapping("/update/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody CreateOrderList orderList,@PathVariable int orderId){
        OrderDto orderDto = orderService.updateOrder(orderList, orderId);
        return new ResponseEntity<>(orderDto,HttpStatus.OK);    
    }

}
