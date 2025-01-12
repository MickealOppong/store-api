package com.store.api.model.product;

import com.store.api.util.LogEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "productAttributes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductAttribute extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String value;

    //product data

    private String productName;
    private Long productId;



    public ProductAttribute(String name,String value, Product product) {
        this.name = name;
        this.value = value;
        this.productId = product.getId();
        this.productName = product.getName();
    }


}
