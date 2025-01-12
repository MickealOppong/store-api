package com.store.api.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeDTO {

    private String name;

    private List<ProductAttributeDTO> productAttributeDTOS = new ArrayList<>();

}
