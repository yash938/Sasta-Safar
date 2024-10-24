package com.Ecommerce.store.serviceImplementation;

import com.Ecommerce.store.Utility.Helper;
import com.Ecommerce.store.dtos.OrderDto;
import com.Ecommerce.store.dtos.PaegableResponse;
import com.Ecommerce.store.entities.*;
import com.Ecommerce.store.exceptions.BadApiRequest;
import com.Ecommerce.store.exceptions.ResourceNotFoundException;
import com.Ecommerce.store.repository.CartRepo;
import com.Ecommerce.store.repository.OrderRepo;
import com.Ecommerce.store.repository.UserRepo;
import com.Ecommerce.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderRepo orderRepo;

    @Override
    public OrderDto createOrder(CreateOrderList orderDto) {
        int cartId = orderDto.getCartId();
        int userId = orderDto.getUserId();

        // Fetch user and cart
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id is not found!!"));
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart id is not found"));
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new BadApiRequest("Invalid number of items in cart");
        }

        // Create the order entity
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderDate(new Date())
                .deliveredDate(null) // Set delivered date when necessary
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);

        // Create OrderItems from CartItems and calculate the total order amount
        List<OrderItems> orderItems = cartItems.stream().map(cartItem -> {

            // Ensure that the product is set in each OrderItem
            Product product = cartItem.getProduct();
            if (product == null) {
                throw new BadApiRequest("Product not found in cart item!");
            }

            OrderItems orderItem = OrderItems.builder()
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice() * product.getDiscountPrice())  // Assuming you multiply price by quantity or discount
                    .product(product)  // Set the product from cartItem
                    .order(order)  // Set the order reference
                    .build();

            // Calculate total order amount
            orderAmount.updateAndGet(v -> v + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        // Set the orderItems and orderAmount in the order
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        // Clear the cart after order creation
        cart.getCartItems().clear();
        cartRepo.save(cart);

        // Save the order
        Order savedOrder = orderRepo.save(order);

        // Return the mapped OrderDto
        return modelMapper.map(savedOrder, OrderDto.class);
    }


    @Override
    public void removeOrder(int orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Id is not found" + orderId));

        orderRepo.delete(order);

    }

    @Override
    public List<OrderDto> getUserOrder(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id is not found"));
        List<Order> orders = orderRepo.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PaegableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending()) ;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Order> all = orderRepo.findAll(pageRequest);
        return Helper.getPaegable(all,OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(CreateOrderList orderList,int orderId) {

        Order ordersId = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order Id is not found"));
        ordersId.setOrderDate(new Date());
        ordersId.setBillingPhone(orderList.getBillingPhone());
        ordersId.setBillingAddress(orderList.getBillingAddress());
        ordersId.setBillingName(orderList.getBillingName());
        ordersId.setPaymentStatus(orderList.getPaymentStatus());
        ordersId.setOrderStatus(orderList.getOrderStatus());

        Order updateOrder = orderRepo.save(ordersId);
        return modelMapper.map(updateOrder,OrderDto.class);
    }
}
