package com.store.api.model.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartLineItem {

    @Id @GeneratedValue
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal shippingCost;
    private Long quantity;
    private boolean  include;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name ="recId",referencedColumnName = "id")
    private CartTable cartTable;



}
