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

        //fetch the product from db
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product id is not found on database"));
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id is not found"));


        Cart cart = null;

        try{
           cart= cartRepo.findByUser(user).get();
        }catch (NoSuchElementException e){
            cart= new Cart();
            cart.setCreatedAt(new Date());
            cart = cartRepo.save(cart);
        }

//        boolean updated= false;
        AtomicReference<Boolean> updated = new AtomicReference<Boolean>(false);
        //perform cart operation
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItem> updatedItems = cartItems.stream().map(item -> {
            if (item.getProduct().getProductId() == productId) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true);

            }

            return item;
        }).collect(Collectors.toList());

        cart.setCartItems(updatedItems);


        //create items
       if(!updated.get()){
           CartItem build = CartItem.builder()
                   .quantity(quantity)
                   .totalPrice(quantity * product.getPrice())
                   .cart(cart)
                   .product(product).build();

           cart.getCartItems().add(build);
       }
        cart.setUser(user);

        Cart updatedCart = cartRepo.save(cart);

        return modelMapper.map(updatedCart,CartDto.class);


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
