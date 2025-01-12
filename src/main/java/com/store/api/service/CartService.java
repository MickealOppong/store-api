package com.store.api.service;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.cart.CartDto;
import com.store.api.model.cart.CartLineItem;
import com.store.api.model.cart.CartTable;
import com.store.api.model.cart.UserCart;
import com.store.api.model.product.Product;
import com.store.api.model.user.AppUser;
import com.store.api.repository.CartLineItemRepository;
import com.store.api.repository.CartTableRepository;
import com.store.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartService {

    private final CartTableRepository cartTableRepository;
    private final CartLineItemRepository cartLineItemRepository;
    private final ProductService  productService;
    private final UserRepository userRepository;
    private final PhotoService photoService;

    public CartService(CartTableRepository cartTableRepository,
                       CartLineItemRepository cartLineItemRepository,ProductService productService,
                       UserRepository userRepository,PhotoService photoService) {
        this.cartTableRepository = cartTableRepository;
        this.cartLineItemRepository = cartLineItemRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.photoService  = photoService;
    }

    public void updateUser(AppUser appUser,String sessionId){
       Optional<CartTable> userCart=  cartTableRepository.findByUsername(appUser.getUsername());
          if(userCart.isEmpty()){
              Optional<CartTable> sessionCart = cartTableRepository.findBySessionId(sessionId);
              if (sessionCart.isPresent()) {
                  CartTable session = sessionCart.get();
                  session.setFirstName(appUser.getFirstName());
                  session.setLastName(appUser.getLastName());
                  session.setUserId(appUser.getId());
                  session.setUsername(appUser.getUsername());
                  cartTableRepository.save(session);
              }
          }
    }
    public void addToCart(Long productId,Long quantity,String sessionId,Long userId){
        //check if it  isa logged-in user creating cart
         AppUser appUser= userRepository.findById(userId).orElse(null);
         //yes user exist in contextHolder
         if(appUser!=null){
             //check whether uer  already has created a cart  record
            CartTable cartTable= cartTableRepository.findByUserId(appUser.getId()).orElse(null);

            //cart  record exist for contextHolder
           if(cartTable !=null){
               //find  the product from cart  items
             CartLineItem item= cartLineItemRepository.findByProductIdAndRecId(productId,cartTable.getId()).orElse(null);
               //check whether product already exist
             if(item !=null){
                 //if product exist update quantity
                    item.setQuantity(item.getQuantity()+quantity);
                cartLineItemRepository.save(item);
             }else{
                 // product does not exist
                 //user exist new product so   add new item by using the product id to retrieve product  data
                 Product product = productService.getProductById(productId);
                 //use the retrieve product data to create cart  line
                 CartLineItem cartLineItem= CartLineItem.builder()
                         .cartTable(cartTable)
                         .productName(product.getName())
                         .productId(product.getId())
                         .shippingCost(product.getShippingCost())
                         .price(product.getPrice())
                         .include(cartTable.isIncludeAllItems())
                         .quantity(quantity)
                         .build();
                 cartLineItemRepository.save(cartLineItem);
             }

           }else{
               //user in  context and creating  first cart item
               //add new user to cart list
               CartTable newUser = CartTable.builder()
                       .userId(appUser.getId())
                       .lastName(appUser.getLastName())
                       .firstName(appUser.getFirstName())
                       .username(appUser.getUsername())
                       .sessionId(sessionId)
                       .includeAllItems(true)
                       .build();
               cartTableRepository.save(newUser);
               //add item to cart
               Product product = productService.getProductById(productId);
               CartLineItem cartLineItem= CartLineItem.builder()
                       .cartTable(newUser)
                       .productName(product.getName())
                       .productId(product.getId())
                       .shippingCost(product.getShippingCost())
                       .price(product.getPrice())
                       .include(newUser.isIncludeAllItems())
                       .quantity(quantity)
                       .build();
               cartLineItemRepository.save(cartLineItem);
           }
         }else{
             //here guest user creating cart
             //find using session id
             CartTable cartTable= cartTableRepository.findBySessionId(sessionId).orElse(null);
             //guest  user  already has created cart
             if(cartTable !=null){
                 //find the product and update quantity
                 CartLineItem item= cartLineItemRepository.findByProductIdAndRecId(productId, cartTable.getId()).orElse(null);
                 if(item !=null){
                     //product already in  cart update quantity
                     item.setQuantity(item.getQuantity()+quantity);
                       cartLineItemRepository.save(item);
                 }else{
                     //guest user  creating first item
                     // add new item to cart
                     Product product = productService.getProductById(productId);
                     CartLineItem cartLineItem= CartLineItem.builder()
                             .cartTable(cartTable)
                             .productName(product.getName())
                             .productId(product.getId())
                             .shippingCost(product.getShippingCost())
                             .price(product.getPrice())
                             .include(cartTable.isIncludeAllItems())
                             .quantity(quantity)
                             .build();
                      cartLineItemRepository.save(cartLineItem);
                 }

             }else{
                 //add new user and item
                 Product product = productService.getProductById(productId);
                 CartTable guestUser = CartTable.builder()
                         .username("guest@mail.com")
                         .lastName("User")
                         .firstName("Guest")
                         .sessionId(sessionId)
                         .includeAllItems(true)
                         .build();
                 cartTableRepository.save(guestUser);
                 CartLineItem cartLineItem= CartLineItem.builder()
                         .cartTable(guestUser)
                         .productName(product.getName())
                         .productId(product.getId())
                         .shippingCost(product.getShippingCost())
                         .price(product.getPrice())
                         .include(guestUser.isIncludeAllItems())
                         .quantity(quantity)
                         .build();
                 cartLineItemRepository.save(cartLineItem);
             }
         }
    }

    public boolean deleteCart(Long userId){
       CartTable userCart = cartTableRepository.findByUserId(userId).orElse(null);
        if(userCart!=null){
            cartLineItemRepository.findAllByRecId(userCart.getId()).forEach(item->cartLineItemRepository.deleteById(item.getId()));
            cartTableRepository.deleteById(userCart.getId());
            return true;
        }
       return false;
    }

    private Optional<Long> getCartId(String username){

       AppUser user= userRepository.findByUsername(username).orElse(null);
       if(user!=null){
         return  cartTableRepository.findByUsername(user.getUsername()).map(CartTable::getId);
       }
       return Optional.empty();
    }
    public void deleteCartItem(Long productId,Long recId){
       CartLineItem item= cartLineItemRepository.findByProductIdAndRecId(productId,recId).orElseThrow(()->new InvalidOperationException("Record does not exist"));
       cartLineItemRepository.delete(item);
        long  itemsCount = cartLineItemRepository.count();
        if(itemsCount==0){
            cartTableRepository.deleteById(recId);
        }
    }
    public Long getCartQuantity(Long userId,String sessionId){

            CartTable user = cartTableRepository.findByUserId(userId).orElse(null);
            if(user !=null) {
                return cartLineItemRepository.findAllByRecId(user.getId()).stream().mapToLong(CartLineItem::getQuantity).sum();
            }
            CartTable sessionCart=  cartTableRepository.findBySessionId(sessionId).orElse(null);
            if(sessionCart!=null) {
             return cartLineItemRepository.findAllByRecId(sessionCart.getId()).stream().mapToLong(CartLineItem::getQuantity).sum();
            }
        return 0L;
    }

    public Optional<UserCart> getUserCart(Long  userId, String sessionId){
        CartTable userCart = cartTableRepository.findByUserId(userId).orElse(null);
      if(userCart!=null){
          List<CartDto> cartDto = cartLineItemRepository.findAllByRecId(userCart.getId())
                  .stream().map(CartDto::new).toList();
          for(CartDto cartItem:cartDto){
              cartItem.setProductImages(photoService.getProductImages(cartItem.getProductId()));
          }
          return Optional.of(UserCart.builder()
                  .id(userCart.getId())
                  .includeAllItems(userCart.isIncludeAllItems())
                  .cartList(cartDto)
                  .build());
      }
        CartTable sessionCart=  cartTableRepository.findBySessionId(sessionId).orElse(null);
      if (sessionCart!=null){
          List<CartDto> cartDto = cartLineItemRepository.findAllByRecId(sessionCart.getId())
                  .stream().map(CartDto::new).toList();
          for(CartDto cartItem:cartDto){
              cartItem.setProductImages(photoService.getProductImages(cartItem.getProductId()));
          }
          return Optional.of(UserCart.builder()
                  .id(sessionCart.getId())
                  .includeAllItems(sessionCart.isIncludeAllItems())
                  .cartList(cartDto)
                  .build());
      }
      return  Optional.empty();
    }

    /*
    public Optional<UserCart> getUserCart(Long id){
        Optional<CartTable> userCart = cartTableRepository.findById(id);
        if(userCart.isPresent()){
            List<CartDto> cartDto = cartLineItemRepository.findAllByRecId(userCart.get().getId())
                    .stream().map(CartDto::new).toList();
            for(CartDto cartItem:cartDto){
                cartItem.setProductImages(photoService.getProductImages(cartItem.getProductId()));
            }
            return Optional.of(UserCart.builder()
                    .id(userCart.get().getId())
                    .includeAllItems(userCart.get().isIncludeAllItems())
                    .cartList(cartDto)
                    .build());
        }
        return Optional.empty();
    }

     */



    public  void updateCart(Long cartId,Long productId,Long quantity){
        List<CartLineItem> cartItems=  cartLineItemRepository.findAllByRecId(cartId);
            for(CartLineItem item : cartItems){
                if(item.getProductId().equals(productId)){
                    item.setQuantity(quantity);
                     cartLineItemRepository.save(item);
                     return;
                }
            }

    }

    public  void updateCartStatus(String sessionId,Long id,Long productId,boolean  includeItem){

        CartTable  cartTable =cartTableRepository.findBySessionId(sessionId)
                .orElseThrow(()->new InvalidOperationException("No record exist"));
        //if one line item is false, change include  all items
        List<CartLineItem> cartItems=  cartLineItemRepository.findAllById(id);
        //change line item
        for(CartLineItem item : cartItems){
            if(item.getProductId().equals(productId)){
                item.setInclude(includeItem);
                cartLineItemRepository.save(item);
            }
        }
        for(CartLineItem item : cartItems){
            if(!item.isInclude()){
                cartTable.setIncludeAllItems(false);
                cartTableRepository.save(cartTable);
            }else if(!cartTable.isIncludeAllItems()){
                cartTable.setIncludeAllItems(true);
                cartTableRepository.save(cartTable);
            }
        }

    }

    public  CartTable updateAllCartStatus(Long cartId,boolean includeAllItems){

        CartTable  cartTable =cartTableRepository.findById(cartId)
                .orElseThrow(()->new InvalidOperationException("No record exist"));
        cartTable.setIncludeAllItems(includeAllItems);
        cartTableRepository.save(cartTable);

        for(CartLineItem item : cartLineItemRepository.findAllByRecId(cartTable.getId())){
                   item.setInclude(includeAllItems);
                   cartLineItemRepository.save(item);
        }
        return cartTable;
    }

    public Long getCartQuantity(Long cartId){
        Long quantity = 0L;
       CartTable userCartTable = cartTableRepository.findById(cartId).orElse(null);
        if(userCartTable!=null){
            for(CartLineItem product:cartLineItemRepository.findAllByRecId(userCartTable.getId())){
                quantity += product.getQuantity();
            }
        }
        return quantity;
    }

    public boolean getStatus(String sessionId){
       return cartTableRepository.findBySessionId(sessionId)
                .orElseThrow(()->new InvalidOperationException("No record exist")).isIncludeAllItems();
    }
}
