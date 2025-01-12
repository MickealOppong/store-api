package com.store.api.controller;

import com.store.api.model.orders.UserOder;
import com.store.api.service.CartService;
import com.store.api.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService,CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<Long> createOrderFromCart(Long userId){
      try{
          return  ResponseEntity.ok().body(orderService.createOrder(userId));
      }catch (Exception e){
          return  ResponseEntity.badRequest().body(-1L);
      }
    }

    @PatchMapping("/change-courier")
    public void updateCourier(Long orderId,String courier){
    orderService.updateCourier(orderId,courier);
    }

    @PatchMapping("/change-payment")
    public void updatePaymentMethod(Long orderId,String  paymentMethod){
        orderService.updatePaymentMethod(orderId,paymentMethod);
    }

    @PutMapping("/change-delivery-address")
    public ResponseEntity<String>  updateDeliveryAddress(Long orderId,Long  deliveryAddressId){
       try{
           orderService.updateOrderDeliveryAddress(orderId,deliveryAddressId);
           return ResponseEntity.ok().body("Address updated");
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PutMapping("/change-invoice-address")
    public ResponseEntity<String> updateInvoiceAddress(Long orderId,Long  invoiceAddressId){
        try{
            orderService.updateOrderInvoiceAddress(orderId,invoiceAddressId);
            return ResponseEntity.ok().body("Address updated");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<UserOder> getOrder(Long orderId){
        Optional<UserOder>  order =orderService.getOrderBYOrderId(orderId);
        return order.map(userOder -> ResponseEntity.ok().body(userOder)).orElse(null);
    }
    @GetMapping("/order")
    public ResponseEntity<UserOder> getUserOrder(Long userId,boolean isCompleted){
        Optional<UserOder>  order =orderService.getOrderByUserId(userId,isCompleted);
        return order.map(userOder -> ResponseEntity.ok().body(userOder)).orElse(null);
    }
    @GetMapping("/orderList")
    public ResponseEntity<List<UserOder>> getOrders(String  username){
        return ResponseEntity.ok().body(orderService.getOrderList(username));
    }

    @PatchMapping("/change-status")
    public void changeOrderStatus( Long userId,boolean isCompleted){
       orderService.changeOrderStatus(userId,isCompleted);
       cartService.deleteCart(userId);
    }
}
