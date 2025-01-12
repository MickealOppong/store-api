package com.store.api.model.favourite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteDto {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private  String  productImage;

    public FavouriteDto(FavouriteLineItem favouriteLineItem) {
        this.id = favouriteLineItem.getId();
        this.productId = favouriteLineItem.getProductId();
        this.productName= favouriteLineItem.getProductName();
    }
}
