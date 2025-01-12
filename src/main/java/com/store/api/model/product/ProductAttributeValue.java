package com.store.api.model.product;

import com.store.api.util.LogEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAttributeValue extends LogEntity {


    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;

    private Long productAttributeId;

    public ProductAttributeValue(String name, String value, ProductAttribute productAttribute) {
        this.name = name;
        this.value = value;
        this.productAttributeId = productAttribute.getId();
    }
}
