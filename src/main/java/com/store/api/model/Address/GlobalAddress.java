package com.store.api.model.Address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.api.model.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalAddress {

    @Id @GeneratedValue
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

    private String addressType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userid",referencedColumnName = "id")
    private AppUser appUser;

    public GlobalAddress(String  firstName, String lastName, String street, String city, String postCode,
                         String apartmentNumber, String houseNumber, String companyName,String  companyNIP, String  telephone, String addressType) {
        this.firstName =firstName;
        this.lastName  =lastName;
        this.street = street;
        this.city =city;
        this.postCode = postCode;
        this.companyName = companyName;
        this.apartmentNumber = apartmentNumber;
        this.houseNumber =  houseNumber;
        this.telephone = telephone;
        this.companyNIP = companyNIP;
        this.addressType = addressType;
    }



}
