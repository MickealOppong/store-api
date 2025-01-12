package com.store.api.model.util;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Courier {

    @Id @GeneratedValue
    private Long id;
    @NotBlank(message = "This is required")
    private String courier;
    private BigDecimal price;
    private Long deliveryDays;

    public Courier(String courier, BigDecimal price,Long deliveryDays) {
        this.courier = courier;
        this.price = price;
        this.deliveryDays= deliveryDays;
    }


}
