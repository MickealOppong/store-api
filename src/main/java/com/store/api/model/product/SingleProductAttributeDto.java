package com.store.api.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleProductAttributeDto {

    private String attributeName;
    private String attributeValue;

    public SingleProductAttributeDto(ProductAttributeDTO productAttributeDTO) {
        this.attributeName=productAttributeDTO.getName();
        this.attributeValue = productAttributeDTO.getValue();
    }
}
