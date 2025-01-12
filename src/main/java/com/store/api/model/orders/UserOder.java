package com.store.api.model.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserOder {

    private Long id;
    private String username;
    //
    private String courier;
    private BigDecimal courierCost;

    //
    private boolean completed;
    private boolean  paid;
    private boolean  delivered;
    private boolean shipped;
    //
    private String paymentMethod;

    private Instant orderDate;
    private Instant deliveryDate;
    //
    private BigDecimal orderTotal;

    private List<OrderLinItemDto> orderLineItems = new ArrayList<>();
    private OrderDeliveryAddress orderDeliveryAddress;
    private OrderInvoiceAddress  orderInvoiceAddress;



}
