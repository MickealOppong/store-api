package com.store.api.model.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InvoiceAddressPersonDto {

    private Long id;
    @NotBlank(message = "This field is  required")
    @Size(min = 2, max = 30, message
            = "First name must be between 2 and 30 characters")
    private String firstName;


    @NotBlank(message = "This field is required")
    @Size(min = 2, max = 30, message
            = "Last name must be between 2 and 30 characters")
    private String lastName;


    @NotBlank(message = "This field is required")
    @Size(min = 2, max = 30, message
            = "Street must be between 2 and 30 characters")
    private String street;


    @NotBlank(message = "This field is required")
    @Size(min = 2, max = 30, message
            = "City must be between 2 and 30 characters")
    private String city;

    @NotBlank(message = "This field is required")
    @Pattern(regexp = "^(\\d{2})-(\\d{3})")
    private String postCode;

    @NotBlank(message = "This field is required")
    private String apartmentNumber;


    private String telephone;

    @NotBlank(message = "This field is required")
    private String houseNumber;

    private String addressType;

    public InvoiceAddressPersonDto(GlobalAddress globalAddress) {
        this.addressType = globalAddress.getAddressType();
        this.lastName= globalAddress.getLastName();
        this.firstName  = globalAddress.getFirstName();
        this.street =  globalAddress.getStreet();
        this.apartmentNumber  = globalAddress.getApartmentNumber();
        this.houseNumber = globalAddress.getHouseNumber();
        this.postCode = globalAddress.getPostCode();
        this.city = globalAddress.getCity();
        this.id = globalAddress.getId();
        this.telephone =  globalAddress.getTelephone();
    }
}
