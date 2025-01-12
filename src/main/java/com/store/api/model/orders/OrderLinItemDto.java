package com.store.api.model.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderLinItemDto {
    private String productName;
    private Long quantity;
    private BigDecimal price;
    private String productImage;
    private Long  productId;

    public OrderLinItemDto(OrderLineItem orderLineItem) {
        this.productName  = orderLineItem.getProductName();
        this.quantity=  orderLineItem.getQuantity();
        this.price=  orderLineItem.getPrice();
        this.productId =  orderLineItem.getProductId();
    }

}
