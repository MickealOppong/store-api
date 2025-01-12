package com.store.api.model.orders;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class OrderDeliveryAddress {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;

    private String lastName;

    private String companyName;

    private String street;

    private String city;

    private String postCode;

    private String apartmentNumber;

    private String houseNumber;

    private String telephone;

    private Long  orderId;
    private Long globalAddressId;


}
