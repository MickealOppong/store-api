package com.store.api.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAttributeDTO {

    private Long id;
    private String name;
    private String value;
    private Long productId;

    public ProductAttributeDTO(ProductAttribute productAttribute) {
        this.id = productAttribute.getId();
        this.productId = productAttribute.getProductId();
        this.name = productAttribute.getName();
        this.value = productAttribute.getValue();
    }


}
