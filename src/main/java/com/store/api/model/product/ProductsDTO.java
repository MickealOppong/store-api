package com.store.api.model.product;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class ProductsDTO {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    //pricing
    private BigDecimal price;
    private BigDecimal reducedPrice;
    //images
    private List<String> productImages = new ArrayList<>();


    //favourite
    private boolean  isFavourite;

}
