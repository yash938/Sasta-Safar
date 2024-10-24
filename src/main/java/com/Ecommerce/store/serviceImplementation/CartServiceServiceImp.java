package com.Ecommerce.store.serviceImplementation;

import com.Ecommerce.store.dtos.AddItemToCart;
import com.Ecommerce.store.dtos.CartDto;
import com.Ecommerce.store.entities.Cart;
import com.Ecommerce.store.entities.CartItem;
import com.Ecommerce.store.entities.Product;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.exceptions.ResourceNotFoundException;
import com.Ecommerce.store.repository.CartItemRepo;
import com.Ecommerce.store.repository.CartRepo;
import com.Ecommerce.store.repository.ProductRepo;
import com.Ecommerce.store.repository.UserRepo;
import com.Ecommerce.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class CartServiceServiceImp implements CartService {

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CartDto AddItemToCart(int userId, AddItemToCart itemToCart) {

        int quantity = itemToCart.getQuantity();
        int productId = itemToCart.getProductId();

        // Fetch the product from db
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product id is not found in the database"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User id is not found"));

        // Fetch or create a cart for the user
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCreatedAt(new Date());
            newCart.setUser(user);
            return cartRepo.save(newCart);
        });

        // Check if the product is already in the cart
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        cart.getCartItems().forEach(item -> {
            if (item.getProduct().getProductId() == productId) {
                // If item already exists, update its quantity and total price
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true);
            }
        });

        // If the item was not already in the cart, add it
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getCartItems().add(cartItem);
        }

        // Save the updated cart
        Cart updatedCart = cartRepo.save(cart);

        return modelMapper.map(updatedCart, CartDto.class);
    }


    @Override
    public void removeItemToCart(int userId, int cartItem) {
        CartItem cartItem1 = cartItemRepo.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("cart item Id is not found"));
        cartItemRepo.delete(cartItem1);

    }

    @Override
    public void clearCart(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id is not found"));
        com.Ecommerce.store.entities.Cart card = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Card is not found"));
         card.getCartItems().clear();
         cartRepo.save(card);
    }

    @Override
    public CartDto getCardByUser(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id is not found"));
        com.Ecommerce.store.entities.Cart card = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Card is not found"));
        return modelMapper.map(card,CartDto.class);
    }
}
