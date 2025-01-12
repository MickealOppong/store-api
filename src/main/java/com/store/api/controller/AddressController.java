package com.store.api.controller;


import com.store.api.enums.AddressType;
import com.store.api.model.Address.*;
import com.store.api.service.GlobalAddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {


    private final GlobalAddressService globalAddressService;


    public AddressController(GlobalAddressService globalAddressService){
        this.globalAddressService = globalAddressService;
    }

    @PostMapping("/delivery/{userId}")
    public ResponseEntity<String> addDeliveryAddress(@RequestParam Long userId,@RequestBody @Valid AddressDTO addressDTO){

           //persist via service  class
           globalAddressService.saveDeliveryAddress(userId,addressDTO.getFirstName(),addressDTO.getLastName(),
                   addressDTO.getStreet(), addressDTO.getCity(),addressDTO.getPostCode(),addressDTO.getApartmentNumber(),
                   addressDTO.getHouseNumber(),addressDTO.getCompanyName(),addressDTO.getTelephone(), AddressType.DELIVERY.name());
           return  ResponseEntity.ok().body("Address created");
    }

    @PostMapping("/invoice/person/{userId}")
    public ResponseEntity<String> addDInvoiceAddress(@RequestParam Long userId,@RequestBody @Valid InvoiceAddressPersonDto addressDTO){

        //persist via service  class
        globalAddressService.saveInvoiceAddressPerson(userId,addressDTO.getFirstName(),addressDTO.getLastName(),addressDTO.getStreet(),
                addressDTO.getCity(),addressDTO.getPostCode(),addressDTO.getApartmentNumber(),
                addressDTO.getHouseNumber(),addressDTO.getTelephone(),AddressType.INVOICE.name());
        return  ResponseEntity.ok().body("Address created");
    }

    @PostMapping("/invoice/company/{userId}")
    public ResponseEntity<String> addDInvoiceAddress(@RequestParam Long userId,@RequestBody @Valid InvoiceAddressCompanyDto addressDTO){

        //persist via service  class
        globalAddressService.saveInvoiceAddressCompany(userId,addressDTO.getStreet(), addressDTO.getCity(),addressDTO.getPostCode(),addressDTO.getApartmentNumber(),
                addressDTO.getHouseNumber(),addressDTO.getCompanyName(),addressDTO.getCompanyNIP(),addressDTO.getTelephone(),AddressType.INVOICE.name());
        return  ResponseEntity.ok().body("Record created");

    }

    @PostMapping("/order-delivery/{orderID}")
    public ResponseEntity<String> orderDeliveryAddress(@RequestParam Long orderId,@RequestBody @Valid AddressDTO addressDTO){
        //persist via service  class
        globalAddressService.createOrderDeliveryAddress(orderId,addressDTO.getFirstName(),addressDTO.getLastName(),addressDTO.getStreet(),
                addressDTO.getCity(),addressDTO.getPostCode(),addressDTO.getApartmentNumber(),
                addressDTO.getHouseNumber(),addressDTO.getCompanyName(),addressDTO.getCompanyNIP(),addressDTO.getTelephone(),AddressType.DELIVERY.name());
        return  ResponseEntity.ok().body("Record created");
    }

    @PostMapping("/person-invoice/{orderID}")
    public ResponseEntity<String> orderInvoiceAddressPerson(@RequestParam Long orderId,@RequestBody @Valid InvoiceAddressPersonDto addressDTO){

        //persist via service  class
        globalAddressService.createOrderInvoiceAddressPerson(orderId,addressDTO.getFirstName(),addressDTO.getLastName() ,addressDTO.getStreet(), addressDTO.getCity(),addressDTO.getPostCode(),addressDTO.getApartmentNumber(),
                addressDTO.getHouseNumber(),addressDTO.getTelephone(),AddressType.INVOICE.name());
        return  ResponseEntity.ok().body("Record created");
    }

    @PostMapping("/company-invoice/{orderID}")
    public ResponseEntity<String> orderInvoiceAddress(@RequestParam Long orderId,@RequestBody @Valid InvoiceAddressCompanyDto addressDTO){

        //persist via service  class
        globalAddressService.createOrderInvoiceAddressCompany(orderId,addressDTO.getCompanyName(),addressDTO.getCompanyNIP() ,addressDTO.getStreet(), addressDTO.getCity(),addressDTO.getPostCode(),addressDTO.getApartmentNumber(),
                addressDTO.getHouseNumber(),addressDTO.getTelephone(),AddressType.INVOICE.name());
        return  ResponseEntity.ok().body("Record created");
    }
    @GetMapping("/delivery")
    public ResponseEntity<List<AddressDTO>> getDeliveryAddressList(@RequestParam Long userId){
        return ResponseEntity.ok().body(globalAddressService.getAllAddressTypeDelivery(userId));
    }

    @GetMapping("/address")
    public ResponseEntity<GlobalAddress> getAddress(@RequestParam Long id){
        return ResponseEntity.ok().body(globalAddressService.getAddress(id));
    }

    @GetMapping("/invoice-person")
    public ResponseEntity<List<InvoiceAddressPersonDto>> getPersonInvoiceAddressList(@RequestParam Long userId){
        return ResponseEntity.ok().body(globalAddressService.getPersonInvoiceAddress(userId));
    }

    @GetMapping("/invoice-company")
    public ResponseEntity<List<InvoiceAddressCompanyDto>> getCompanyInvoiceAddressList(@RequestParam Long userId){
        return ResponseEntity.ok().body(globalAddressService.getCompanyInvoiceAddress(userId));
    }
    @GetMapping("/invoice-address")
    public ResponseEntity<List<InvoiceAddressDto>> getInvoiceAddressList(@RequestParam Long userId){
        return ResponseEntity.ok().body(globalAddressService.getInvoiceAddress(userId));
    }
    @PatchMapping("/edit")
    public ResponseEntity<Boolean>  updateAddress(@RequestParam Long id,@RequestBody AddressDTO addressDto){
        GlobalAddress address =  globalAddressService.updateAddress(id,addressDto.getFirstName(), addressDto.getLastName(), addressDto.getStreet()
                ,addressDto.getCity(),addressDto.getPostCode(),
                addressDto.getApartmentNumber(),addressDto.getHouseNumber(),addressDto.getCompanyName(),addressDto.getCompanyNIP(),addressDto.getTelephone());
        if(address==null){
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok().body(true);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@RequestParam Long id){
        globalAddressService.removeAddress(id);;
    }
}
