package com.store.api.model.orders;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderInvoiceAddress {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;

    private String lastName;

    private String companyName;
    private String companyNIP;

    private String street;

    private String city;

    private String postCode;

    private String apartmentNumber;

    private String houseNumber;

    private String telephone;

    private Long  orderId;
    private Long globalAddressId;
}
