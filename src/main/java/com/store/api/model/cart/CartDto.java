package com.store.api.model.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {


    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal shippingCost;
    private Long quantity;
    private boolean  include;

    private List<String >  productImages =  new ArrayList<>();


    public CartDto(CartLineItem cartLineItem) {
        this.id = cartLineItem.getId();
        this.productId = cartLineItem.getProductId();
        this.productName= cartLineItem.getProductName();
        this.price = cartLineItem.getPrice();
        this.shippingCost = cartLineItem.getShippingCost();
        this.quantity = cartLineItem.getQuantity();
        this.include = cartLineItem.isInclude();
    }


}
