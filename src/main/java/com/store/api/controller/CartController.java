package com.store.api.controller;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.cart.CartTable;
import com.store.api.model.cart.UserCart;
import com.store.api.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Long> addToCart(@RequestParam Long productId,@RequestParam Long quantity,@RequestParam String sessionId,
                                            @RequestParam Long userId){
       try{
                cartService.addToCart(productId,quantity,sessionId,userId);
              return ResponseEntity.ok().body(cartService.getCartQuantity(userId,sessionId));
       }catch (Exception e){
           return ResponseEntity.ok(0L);
       }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCart(Long id){
        try{
            cartService.deleteCart(id);
                return ResponseEntity.badRequest().body("Cart deleted");
        }catch (Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> removeUserCart(Long cartId){
        boolean returnValue =cartService.deleteCart(cartId);
        if(returnValue){
            return ResponseEntity.ok("Cart  deleted");
        }
        return ResponseEntity.badRequest().body("Error");
    }

    @DeleteMapping("/delete-item")
    public ResponseEntity<Long> removeCartItem(Long productId,Long cartId){
     try {
         cartService.deleteCartItem(productId,cartId);
         return ResponseEntity.ok(cartService.getCartQuantity(cartId));
     }catch (InvalidOperationException e){
         return ResponseEntity.ok(cartService.getCartQuantity(cartId));
     }
    }

    @GetMapping
    public ResponseEntity<Optional<UserCart>> userCart(Long userId, String sessionId){
     try{
         Optional<UserCart>  userCart  =cartService.getUserCart(userId,sessionId);
         if(userCart.isPresent()){
             return ResponseEntity.ok().body(Optional.of(userCart.get()));
         }
       return ResponseEntity.ok().body(Optional.empty());
     }catch (InvalidOperationException e){
         return ResponseEntity.badRequest().body(Optional.empty());
     }
    }

    @PatchMapping("/quantity")
    public ResponseEntity<Long> updateCart( Long cartId,Long productId,Long quantity){
        try{
            cartService.updateCart(cartId,productId,quantity);
            return ResponseEntity.ok(cartService.getCartQuantity(cartId));
        }catch (NullPointerException| InvalidDataAccessApiUsageException e){
            return ResponseEntity.badRequest().body(0L);
        }
    }

    @PatchMapping("/change-status")
    public ResponseEntity<String> updateCartStatus( String sessionId,Long cartId,Long productId,boolean  includeItem){
        try{
            cartService.updateCartStatus(sessionId,cartId,productId,includeItem);
            return ResponseEntity.ok("Cart updated");
        }catch (NullPointerException| InvalidDataAccessApiUsageException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cart-quantity")
    public ResponseEntity<Long> userCartQuantity(@RequestParam Long userId, @RequestParam String sessionId){
        return ResponseEntity.ok().body(cartService.getCartQuantity(userId,sessionId));
    }
    @PatchMapping("/change-all")
    public ResponseEntity<String> updateAllCartStatus(Long cartId, boolean includeAllItems){
        try{
            CartTable cartTable  = cartService.updateAllCartStatus(cartId,includeAllItems);
            if(cartTable!=null){
                return ResponseEntity.ok("Status updated");
            }
            return ResponseEntity.ok(null);
        }catch (NullPointerException| InvalidDataAccessApiUsageException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
