package com.store.api.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleProductDTO {


    private Long id;
    private String name;
    private String searchName;
    private String description;

    //states
    private boolean isNewArrival;
    private boolean isFeaturedProduct;
    private boolean isFreeShipping;

    //pricing
    private BigDecimal price;
    private BigDecimal reducedPrice;

    //quantity
    private Long quantity;

    //favourite  tracker
    private boolean   isFavourite;


    //shipping
    private BigDecimal shippingCost;
    private Instant estimatedDeliveryDate;
    private int returnDays;

    private List<String> productImages =new ArrayList<>();
    private List<String> productCategoryList =new ArrayList<>();
    private List<SingleProductAttributeDto>  productAttributeDTO = new ArrayList<>();


}
