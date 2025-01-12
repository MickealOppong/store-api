package com.store.api.model.product;

import com.store.api.util.LogEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ProductCategory extends LogEntity {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private Long productId;
    private  int categoryHierarchy;

    public ProductCategory(String name,int categoryHierarchy, Product product) {
        this.name = name;
        this.categoryHierarchy = categoryHierarchy;
        this.productId = product.getId();
    }
}
