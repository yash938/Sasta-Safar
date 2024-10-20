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
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id is not found!!"));

        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart id is not found"));
        List<CartItem> cartItems = cart.getCartItems();

        if(cartItems.size() == 0){
            throw new BadApiRequest("Invalid number of items in cart");
        }

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .user(user)
                .build();


        AtomicReference<Integer> orderAmout = new AtomicReference<>(0);
        List<OrderItems> orderItems = cartItems.stream().map(cartItem -> {


            //cartitem -> orderItem

            OrderItems orderItem = OrderItems.builder()
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice() * cartItem.getProduct().getDiscountPrice())
                    .order(order)
                    .build();

            orderAmout.set(orderAmout.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmout.get());

        cart.getCartItems().clear();

        cartRepo.save(cart);
        Order save = orderRepo.save(order);

        return modelMapper.map(save,OrderDto.class);
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
    public List<OrderDto> updateOrder(CreateOrderList orderList) {


        return null;
    }
}
