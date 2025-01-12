package com.store.api.model.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StoreProducts {

    @Id
    @GeneratedValue
    private Long recId;
    private Long productId;
    private String name;
    private String description;


    //states
    private boolean isNewArrival;
    private boolean isFeaturedProduct;
    private boolean isRecommended;
    private boolean isFreeShipping;

    //pricing
    private BigInteger price;
    private BigInteger reducedPrice;

    //quantity
    private Long quantity;


    //shipping
    private BigInteger shippingCost;
    private Instant estimatedDeliveryDate;
    private int returnDays;

}
