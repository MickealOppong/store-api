package com.store.api.model.orders;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderLineItem {

    @Id @GeneratedValue
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Long quantity;
    private BigDecimal  price;

}
