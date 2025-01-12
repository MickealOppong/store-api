package com.store.api.model.orders;

import com.store.api.util.LogEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderTable extends LogEntity {

    @Id@GeneratedValue
    private Long recId;
    private String username;
    private String firstName;
    private String lastName;
    private Long userId;
    private String paymentMethod;
    //
    private boolean completed;
    private boolean  paid;
    private boolean  delivered;
    private boolean shipped;

    //
    private String courier;
    private BigDecimal  courierCost;

    private Instant orderDate;
    private Instant deliveryDate;

}
