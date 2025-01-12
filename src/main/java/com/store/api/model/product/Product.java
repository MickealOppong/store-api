package com.store.api.model.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product{

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String searchName;
    private String description;


    //states
    private boolean isNewArrival;
    private boolean isFeaturedProduct;
    private boolean isRecommended;
    private boolean isFreeShipping;

    //pricing
    private BigDecimal price;
    private BigDecimal reducedPrice;

    //quantity
    private Long quantity;


    //shipping
    private BigDecimal shippingCost;
    private Instant estimatedDeliveryDate;
    private int returnDays;

    //photos

}
