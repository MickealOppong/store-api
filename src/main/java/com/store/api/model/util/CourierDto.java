package com.store.api.model.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourierDto {
    private Long id;
    private String courier;
    private BigDecimal price;
    private Instant deliveryDate;

    public CourierDto(Courier courier){
        this.id =  courier.getId();
        this.courier =  courier.getCourier();
        this.price=  courier.getPrice();
        this.deliveryDate =  Instant.now().plus(courier.getDeliveryDays(), ChronoUnit.DAYS);
    }
}
