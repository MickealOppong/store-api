package com.store.api.service;

import com.store.api.enums.AddressType;
import com.store.api.model.Address.GlobalAddress;
import com.store.api.model.cart.CartLineItem;
import com.store.api.model.cart.CartTable;
import com.store.api.model.orders.*;
import com.store.api.model.util.Courier;
import com.store.api.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final CartTableRepository cartTableRepository;
    private final CartLineItemRepository cartLineItemRepository;
    private final PhotoService photoService;
    private final UserDetailsServiceImpl userDetailsService;
    private final CourierRepository courierRepository;
    private final AddressRepository  addressRepository;
    private final OrderInvoiceAddressRepository orderInvoiceAddressRepository;
    private final OrderDeliveryAddressRepository orderDeliveryAddressRepository;
    private final PaymentMethodService paymentMethodService;

    public OrderService(OrderTableRepository orderTableRepository,
                        OrderLineItemRepository orderLineItemRepository, CartTableRepository cartTableRepository,
                        CartLineItemRepository cartLineItemRepository,
                        PhotoService photoService, UserDetailsServiceImpl userDetailsService,
                        CourierRepository courierRepository,
                        AddressRepository addressRepository,
                        OrderDeliveryAddressRepository orderDeliveryAddressRepository,
                        PaymentMethodService  paymentMethodService,
                        OrderInvoiceAddressRepository orderInvoiceAddressRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.cartTableRepository = cartTableRepository;
        this.cartLineItemRepository = cartLineItemRepository;
        this.photoService = photoService;
        this.userDetailsService = userDetailsService;
        this.courierRepository = courierRepository;
        this.addressRepository =  addressRepository;
        this.orderDeliveryAddressRepository = orderDeliveryAddressRepository;
       this.paymentMethodService =  paymentMethodService;
       this.orderInvoiceAddressRepository = orderInvoiceAddressRepository;
    }


    public Long createOrder(Long userId){
        //order  already exist , updating product
       OrderTable order = orderTableRepository.findByUserIdAndCompleted(userId,false).orElse(null);

        if(order!=null){
            //delete all  items
            orderLineItemRepository.deleteAll();

            //save items from cart list
            for(CartLineItem  cartItem :cartLineItemRepository.findAllByRecId(order.getRecId())){
                OrderLineItem orderLineItem = OrderLineItem.builder()
                        .productId(cartItem.getProductId())
                        .price(cartItem.getPrice())
                        .quantity(cartItem.getQuantity())
                        .orderId(order.getRecId())
                        .productName(cartItem.getProductName())
                        .build();
                orderLineItemRepository.save(orderLineItem);

                //check for delivery address
               OrderDeliveryAddress retrieveDeliveryAddress= orderDeliveryAddressRepository
                       .findByOrderId(order.getRecId()).orElse(null);

               if(retrieveDeliveryAddress==null) {

                   //set default delivery address
                   List<GlobalAddress> deliveryAddress = addressRepository
                           .findByAddressTypeAndUserid(AddressType.DELIVERY.name(), userId);

                   if (!deliveryAddress.isEmpty()) {
                       GlobalAddress address = deliveryAddress.get(0);
                       OrderDeliveryAddress defaultDeliveryAddress = OrderDeliveryAddress.builder()
                               .firstName(address.getFirstName())
                               .lastName(address.getLastName())
                               .city(address.getCity())
                               .street(address.getStreet())
                               .houseNumber(address.getHouseNumber())
                               .apartmentNumber(address.getApartmentNumber())
                               .postCode(address.getPostCode())
                               .companyName(address.getCompanyName())
                               .orderId(order.getRecId())
                               .globalAddressId(address.getId())
                               .build();
                       orderDeliveryAddressRepository.save(defaultDeliveryAddress);
                   }
               }
                //check for default  invoice address
                OrderInvoiceAddress retrieveInvoiceAddress= orderInvoiceAddressRepository
                        .findByOrderId(order.getRecId()).orElse(null);
               if(retrieveInvoiceAddress==null) {

                   //set default delivery  invoice address
                   List<GlobalAddress> invoiceAddress = addressRepository
                           .findByAddressTypeAndUserid(AddressType.INVOICE.name(), userId);

                   if (!invoiceAddress.isEmpty()) {
                       GlobalAddress address = invoiceAddress.get(0);
                       OrderInvoiceAddress defaultInvoiceAddress = OrderInvoiceAddress.builder()
                               .companyNIP(address.getCompanyNIP())
                               .city(address.getCity())
                               .street(address.getStreet())
                               .houseNumber(address.getHouseNumber())
                               .apartmentNumber(address.getApartmentNumber())
                               .postCode(address.getPostCode())
                               .companyName(address.getCompanyName())
                               .orderId(order.getRecId())
                               .globalAddressId(address.getId())
                               .build();
                       orderInvoiceAddressRepository.save(defaultInvoiceAddress);
                   }
               }

            }
            return order.getRecId();
        }else {
            //new order
            CartTable userCartTable = cartTableRepository.findByUserId(userId).orElse(null);
            OrderTable savedOrder = null;

            if (userCartTable !=null) {

                //set default courier
                Optional<Courier> courier = courierRepository.findFirstItem();

                //create order header
                OrderTable orderTable = OrderTable.builder()
                        .username(userCartTable.getUsername())
                        .userId(userCartTable.getUserId())
                        .firstName(userCartTable.getFirstName())
                        .lastName(userCartTable.getLastName())
                        .courier(courier.map(Courier::getCourier).orElse("Free shipping"))
                        .courierCost(courier.map(Courier::getPrice).orElse(BigDecimal.valueOf(0.00)))
                        .paymentMethod(paymentMethodService.getOne())
                        .orderDate(Instant.now())
                        .deliveryDate(Instant.now().plus(courier.map(Courier::getDeliveryDays).orElse(0L), ChronoUnit.DAYS))
                        .delivered(false)
                        .paid(false)
                        .completed(false)
                        .shipped(false)
                        .build();

                 savedOrder = orderTableRepository.save(orderTable);

                //set default delivery  address
                List<GlobalAddress> deliveryAddress = addressRepository
                        .findByAddressTypeAndUserid(AddressType.DELIVERY.name(), userId);

                if (!deliveryAddress.isEmpty()) {
                    GlobalAddress address = deliveryAddress.get(0);
                    OrderDeliveryAddress defaultDeliveryAddress = OrderDeliveryAddress.builder()
                            .firstName(address.getFirstName())
                            .lastName(address.getLastName())
                            .city(address.getCity())
                            .street(address.getStreet())
                            .houseNumber(address.getHouseNumber())
                            .apartmentNumber(address.getApartmentNumber())
                            .postCode(address.getPostCode())
                            .companyName(address.getCompanyName())
                            .orderId(savedOrder.getRecId())
                            .build();
                    orderDeliveryAddressRepository.save(defaultDeliveryAddress);
                }

                //set default delivery  invoice address

                List<GlobalAddress> invoiceAddress = addressRepository
                        .findByAddressTypeAndUserid(AddressType.INVOICE.name(),userId);

                if (!invoiceAddress.isEmpty()) {
                    GlobalAddress address = invoiceAddress.get(0);
                    OrderInvoiceAddress defaultInvoiceAddress = OrderInvoiceAddress.builder()
                            .companyNIP(address.getCompanyNIP())
                            .city(address.getCity())
                            .street(address.getStreet())
                            .houseNumber(address.getHouseNumber())
                            .apartmentNumber(address.getApartmentNumber())
                            .postCode(address.getPostCode())
                            .companyName(address.getCompanyName())
                            .orderId(savedOrder.getRecId())
                            .build();
                    orderInvoiceAddressRepository.save(defaultInvoiceAddress);
                }

                //create product  lines

                for (CartLineItem cartItem : cartLineItemRepository.findAllByRecId(userCartTable.getId())) {
                    OrderLineItem orderLineItem = OrderLineItem.builder()
                            .productId(cartItem.getProductId())
                            .price(cartItem.getPrice())
                            .quantity(cartItem.getQuantity())
                            .orderId(savedOrder.getRecId())
                            .productName(cartItem.getProductName())
                            .build();
                    orderLineItemRepository.save(orderLineItem);
                }
            }
            return savedOrder.getRecId();
        }
    }

    public Optional<UserOder> getOrderBYOrderId(Long orderId){
      OrderTable  retrievedOrder =orderTableRepository.findById(orderId).orElse(null);

      if(retrievedOrder!=null){

          for(OrderLinItemDto  item:  orderLineItemRepository.findAllByOrderId(retrievedOrder.getRecId())
                  .stream().map(OrderLinItemDto::new).toList()){

              //check whether photo exist
             List<String> productImages= photoService.getProductImages(item.getProductId());
             //load  product  photo
             if(!productImages.isEmpty()){
                 item.setProductImage(photoService.getProductImages(item.getProductId()).get(0));
             }

          }
         UserOder oder= UserOder.builder()
                  .id(retrievedOrder.getRecId())
                  .username(retrievedOrder.getUsername())
                  .courier(retrievedOrder.getCourier())
                  .courierCost(retrievedOrder.getCourierCost())
                  .paymentMethod(retrievedOrder.getPaymentMethod())
                 .orderTotal(getOrderTotal( orderLineItemRepository.findAllByOrderId(retrievedOrder.getRecId())
                         .stream().map(OrderLinItemDto::new).toList()))
                 .shipped(retrievedOrder.isShipped())
                 .delivered(retrievedOrder.isDelivered())
                 .completed(retrievedOrder.isCompleted())
                 .paid(retrievedOrder.isPaid())
                 .orderDate(retrievedOrder.getOrderDate())
                 .orderDeliveryAddress(orderDeliveryAddressRepository.findByOrderId(retrievedOrder.getRecId()).orElse(null))
                 .orderInvoiceAddress(orderInvoiceAddressRepository.findByOrderId(retrievedOrder.getRecId()).orElse(null))
                  .orderLineItems( orderLineItemRepository.findAllByOrderId(retrievedOrder.getRecId())
                          .stream().map(OrderLinItemDto::new).toList())
                  .build();
         return Optional.of(oder);
      }
      return Optional.empty();
    }

    public Optional<UserOder> getOrderByUserId(Long userId,boolean  status){
        OrderTable  retrievedOrder =orderTableRepository.findByUserIdAndCompleted(userId,status).orElse(null);

        if(retrievedOrder!=null){

            for(OrderLinItemDto  item:  orderLineItemRepository.findAllByOrderId(retrievedOrder.getRecId())
                    .stream().map(OrderLinItemDto::new).toList()){

                //check whether photo exist
                List<String> productImages= photoService.getProductImages(item.getProductId());
                //load  product  photo
                if(!productImages.isEmpty()){
                    item.setProductImage(photoService.getProductImages(item.getProductId()).get(0));
                }

            }
            UserOder oder= UserOder.builder()
                    .id(retrievedOrder.getRecId())
                    .username(retrievedOrder.getUsername())
                    .courier(retrievedOrder.getCourier())
                    .courierCost(retrievedOrder.getCourierCost())
                    .paymentMethod(retrievedOrder.getPaymentMethod())
                    .orderTotal(getOrderTotal( orderLineItemRepository.findAllByOrderId(retrievedOrder.getRecId())
                            .stream().map(OrderLinItemDto::new).toList()))
                    .shipped(retrievedOrder.isShipped())
                    .delivered(retrievedOrder.isDelivered())
                    .completed(retrievedOrder.isCompleted())
                    .paid(retrievedOrder.isPaid())
                    .orderDate(retrievedOrder.getOrderDate())
                    .deliveryDate(retrievedOrder.getDeliveryDate())
                    .orderDeliveryAddress(orderDeliveryAddressRepository.findByOrderId(retrievedOrder.getRecId()).orElse(null))
                    .orderInvoiceAddress(orderInvoiceAddressRepository.findByOrderId(retrievedOrder.getRecId()).orElse(null))
                    .orderLineItems( orderLineItemRepository.findAllByOrderId(retrievedOrder.getRecId())
                            .stream().map(OrderLinItemDto::new).toList())
                    .build();
            return Optional.of(oder);
        }
        return Optional.empty();
    }

    public List<UserOder> getOrderList(String  username){
        List<UserOder>   userOrderList = new ArrayList<>();
        List<OrderTable>  orderList =orderTableRepository.findByUsername(username);
       for(OrderTable orderTable :orderList){

            List<OrderLinItemDto> itemList = orderLineItemRepository.findAllByOrderId(orderTable.getRecId())
                    .stream().map(OrderLinItemDto::new).toList();

            for(OrderLinItemDto  item:  itemList){

                //check whether photo exist
                List<String> productImages= photoService.getProductImages(item.getProductId());
                //load  product  photo
                if(!productImages.isEmpty()){
                    item.setProductImage(photoService.getProductImages(item.getProductId()).get(0));
                }
                UserOder singleOrder= UserOder.builder()
                        .id(orderTable.getRecId())
                        .username(orderTable.getUsername())
                        .courier(orderTable.getCourier())
                        .courierCost(orderTable.getCourierCost())
                        .paymentMethod(orderTable.getPaymentMethod())
                        .orderTotal(getOrderTotal(itemList))
                        .shipped(orderTable.isShipped())
                        .delivered(orderTable.isDelivered())
                        .completed(orderTable.isCompleted())
                        .paid(orderTable.isPaid())
                        .orderDeliveryAddress(orderDeliveryAddressRepository.findByOrderId(orderTable.getRecId()).orElse(null))
                        .orderInvoiceAddress(orderInvoiceAddressRepository.findByOrderId(orderTable.getRecId()).orElse(null))
                        .orderLineItems(itemList)
                        .build();
                userOrderList.add(singleOrder);
            }

        }
       return userOrderList;
    }

    private BigDecimal getOrderTotal(List<OrderLinItemDto>  orderLineItems){
        BigDecimal total = new BigDecimal("0.00");
        for(OrderLinItemDto item:orderLineItems){
            BigDecimal  price  =item.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal  subTotal = price.multiply(quantity);
            total  = total.add(subTotal);
        }
        return total;
    }


    public void updateCourier(Long orderId,String courier){
        OrderTable userOrder = orderTableRepository
                .findById(orderId).orElse(null);
        if(userOrder !=null){
            Courier orderCourier = courierRepository.findByCourier(courier).orElse(null);
            if(orderCourier != null){
                userOrder.setCourier(orderCourier.getCourier());
                userOrder.setCourierCost(orderCourier.getPrice());
                orderTableRepository.save(userOrder);
            }
        }
    }

    public void updatePaymentMethod(Long orderId,String paymentMethod){
      OrderTable userOrder = orderTableRepository
                .findById(orderId).orElse(null);
        if(userOrder != null){
            String paymentMethodSelected = paymentMethodService.getPaymentMethod(paymentMethod);
               if(!paymentMethodSelected.isEmpty()){
                   userOrder.setPaymentMethod(paymentMethodSelected);
                   orderTableRepository.save(userOrder);
               }
        }
    }

    public void  updateOrderDeliveryAddress(Long orderId, Long deliveryAddressId){
      GlobalAddress address=  addressRepository.findById(deliveryAddressId).orElse(null);
      if(address!=null){
          OrderTable userOrder = orderTableRepository
                  .findById(orderId).orElse(null);

          if(userOrder !=null){

            OrderDeliveryAddress  deliveryAddress = orderDeliveryAddressRepository
                    .findByOrderId(userOrder.getRecId()).orElse(null);

            if(deliveryAddress!=null){
                deliveryAddress.setFirstName(address.getFirstName());
                deliveryAddress.setLastName(address.getLastName());
                deliveryAddress.setCompanyName(address.getCompanyName());
                deliveryAddress.setStreet(address.getStreet());
                deliveryAddress.setCity(address.getCity());
                deliveryAddress.setHouseNumber(address.getHouseNumber());
                deliveryAddress.setApartmentNumber(address.getApartmentNumber());
                deliveryAddress.setPostCode(address.getPostCode());
                deliveryAddress.setGlobalAddressId(address.getId());
                deliveryAddress.setTelephone(address.getTelephone());
              orderDeliveryAddressRepository.save(deliveryAddress);
            }
          }
      }
    }

    public void updateOrderInvoiceAddress(Long orderId,Long invoiceAddressId){
        GlobalAddress address=  addressRepository.findById(invoiceAddressId).orElse(null);
        if(address!=null){
           OrderTable userOrder = orderTableRepository
                    .findById(orderId).orElse(null);

            if(userOrder != null){

                      OrderInvoiceAddress  invoiceAddress = orderInvoiceAddressRepository
                        .findByOrderId(userOrder.getRecId()).orElse(null);

                if(invoiceAddress!=null){
                    invoiceAddress.setCompanyNIP(address.getCompanyNIP());
                    invoiceAddress.setFirstName(address.getFirstName());
                    invoiceAddress.setLastName(address.getLastName());
                    invoiceAddress.setCompanyName(address.getCompanyName());
                    invoiceAddress.setStreet(address.getStreet());
                    invoiceAddress.setCity(address.getCity());
                    invoiceAddress.setHouseNumber(address.getHouseNumber());
                    invoiceAddress.setApartmentNumber(address.getApartmentNumber());
                    invoiceAddress.setPostCode(address.getPostCode());
                    invoiceAddress.setGlobalAddressId(address.getId());
                    invoiceAddress.setTelephone(address.getTelephone());
                 orderInvoiceAddressRepository.save(invoiceAddress);
                }
            }

        }
    }


    public void changeOrderStatus(Long userId,boolean  isCompleted){
       OrderTable  orderTable  = orderTableRepository.findByUserIdAndCompleted(userId,false).orElse(null);
       if(orderTable!=null){
           orderTable.setCompleted(isCompleted);
           orderTableRepository.save(orderTable);
       }
    }
    private Optional<Boolean> isOrderCompleted(Long orderId){
       OrderTable order  =orderTableRepository.findById(orderId).orElse(null);
       if(order!=null){
           return Optional.of( order.isCompleted());
       }
       return Optional.empty();
    }

}
