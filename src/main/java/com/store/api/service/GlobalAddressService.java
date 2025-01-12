package com.store.api.service;

import com.store.api.enums.AddressType;
import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.Address.*;
import com.store.api.model.orders.OrderDeliveryAddress;
import com.store.api.model.orders.OrderInvoiceAddress;
import com.store.api.model.orders.OrderTable;
import com.store.api.model.user.AppUser;
import com.store.api.repository.AddressRepository;
import com.store.api.repository.OrderDeliveryAddressRepository;
import com.store.api.repository.OrderInvoiceAddressRepository;
import com.store.api.repository.OrderTableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GlobalAddressService {

    private final AddressRepository  addressRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final OrderDeliveryAddressRepository orderDeliveryAddressRepository;
    private final OrderInvoiceAddressRepository orderInvoiceAddressRepository;
    private final OrderTableRepository orderTableRepository;

    public GlobalAddressService(AddressRepository addressRepository,
                                UserDetailsServiceImpl userDetailsService,
                                OrderDeliveryAddressRepository orderDeliveryAddressRepository,
                                OrderInvoiceAddressRepository orderInvoiceAddressRepository,
                                OrderTableRepository orderTableRepository){
        this.userDetailsService = userDetailsService;
        this.addressRepository = addressRepository;
        this.orderDeliveryAddressRepository=orderDeliveryAddressRepository;
        this.orderInvoiceAddressRepository =  orderInvoiceAddressRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void saveInvoiceAddressCompany(Long userId,String street, String city,String postCode, String apartmentNumber,
                     String houseNumber, String companyName,String companyNIP,String  telephone,String addressType){
        //get current request user
        AppUser appUser = userDetailsService.getUserById(userId);

        //create address object
        GlobalAddress address = GlobalAddress.builder()
                .companyName(companyName)
                .companyNIP(companyNIP)
                .street(street)
                .city(city)
                .postCode(postCode)
                .houseNumber(houseNumber)
                .apartmentNumber(apartmentNumber)
                .appUser(appUser)
                .addressType(addressType)
                .telephone(telephone)
                .build();
        addressRepository.save(address);
    }


    public void saveDeliveryAddress(Long userId,String firstName,String lastName,String street, String city,String code, String apartmentNumber,
                     String houseNumber,String companyName, String  telephone,String addressType){
        //get user details from database
        AppUser appUser = userDetailsService.getUserById(userId);

        //create address object
        GlobalAddress address = GlobalAddress.builder()
                .addressType(addressType)
                .firstName(firstName)
                .lastName(lastName)
                .street(street)
                .companyName(companyName)
                .city(city)
                .postCode(code)
                .houseNumber(houseNumber)
                .apartmentNumber(apartmentNumber)
                .appUser(appUser)
                .telephone(telephone)
                .build();

        //assign address to request user
        address.setAppUser(appUser);
        addressRepository.save(address);
    }
    public void saveInvoiceAddressPerson(Long userId,String firstName,String lastName,String street, String city,String code, String apartmentNumber,
                     String houseNumber,String  telephone,String addressType){

        //get user details from database
        AppUser appUser = userDetailsService.getUserById(userId);

        //create address object
        GlobalAddress address = GlobalAddress.builder()
                .addressType(addressType)
                .street(street)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .postCode(code)
                .houseNumber(houseNumber)
                .apartmentNumber(apartmentNumber)
                .appUser(appUser)
                .telephone(telephone)
                .build();

        //assign address to request user
        address.setAppUser(appUser);
        addressRepository.save(address);
    }



    public GlobalAddress getAddress(Long id){
        return addressRepository.findById(id)
                .orElseThrow((()->new InvalidOperationException("record does not exist")));
    }


    public List<AddressDTO>  getAllAddressTypeDelivery(Long userId){

        return  addressRepository.findAll().stream()
                .filter(type->type.getAddressType().equals(AddressType.DELIVERY.name()))
                .filter(user->user.getAppUser().getId().equals(userId)).map(AddressDTO::new).toList();
    }
    public List<InvoiceAddressCompanyDto>  getCompanyInvoiceAddress(Long userId){
       return addressRepository.findAll().stream()
                .filter(type->type.getAddressType().equals(AddressType.INVOICE.name()))
                .filter(nip-> nip.getCompanyNIP() != null).filter(user->user.getAppUser().getId().equals(userId))
               .map(InvoiceAddressCompanyDto::new).toList();

    }

    public List<InvoiceAddressPersonDto>  getPersonInvoiceAddress(Long userId){
    return addressRepository.findAll().stream()
                .filter(type->type.getAddressType().equals(AddressType.INVOICE.name()))
                .filter(nip-> nip.getCompanyNIP() == null).filter(user->user.getAppUser().getId().equals(userId))
           .map(InvoiceAddressPersonDto::new).toList();

    }

    public List<InvoiceAddressDto>  getInvoiceAddress(Long userId){
        return addressRepository.findAll().stream()
                .filter(type->type.getAddressType().equals(AddressType.INVOICE.name()))
                .filter(user->user.getAppUser().getId().equals(userId))
                .map(InvoiceAddressDto::new).toList();

    }
    public GlobalAddress updateAddress(Long id, String firstName, String lastName,
                                       String street, String city, String postCode, String apartmentNumber,
                                       String houseNumber, String companyName,String companyNIP,String  telephone){
        GlobalAddress globalAddress= addressRepository.findById(id).orElse(null);
       if(globalAddress!=null){
           globalAddress.setFirstName(firstName);
           globalAddress.setLastName(lastName);
           globalAddress.setStreet(street);
           globalAddress.setCity(city);
           globalAddress.setPostCode(postCode);
           globalAddress.setApartmentNumber(apartmentNumber);
           globalAddress.setHouseNumber(houseNumber);
           globalAddress.setTelephone(telephone);
           globalAddress.setCompanyName(companyName);
           globalAddress.setCompanyNIP(companyNIP);

         GlobalAddress  updatedAddress= addressRepository.save(globalAddress);


        OrderDeliveryAddress orderDeliveryAddress =  orderDeliveryAddressRepository
                .findByGlobalAddressId(globalAddress.getId()).orElse(null);

        if(orderDeliveryAddress !=null){
              orderDeliveryAddress.setFirstName(updatedAddress.getFirstName());
              orderDeliveryAddress.setLastName(updatedAddress.getLastName());
              orderDeliveryAddress.setCompanyName(updatedAddress.getCompanyName());
            orderDeliveryAddress.setPostCode(updatedAddress.getPostCode());
            orderDeliveryAddress.setStreet(updatedAddress.getStreet());
            orderDeliveryAddress.setHouseNumber(updatedAddress.getHouseNumber());
            orderDeliveryAddress.setApartmentNumber(updatedAddress.getApartmentNumber());
            orderDeliveryAddress.setCity(updatedAddress.getCity());
            orderDeliveryAddress.setTelephone(updatedAddress.getTelephone());

            orderDeliveryAddressRepository.save(orderDeliveryAddress);
        }

        OrderInvoiceAddress  orderInvoiceAddress = orderInvoiceAddressRepository
                .findByGlobalAddressId(globalAddress.getId()).orElse(null);

           if(orderInvoiceAddress !=null){
              orderInvoiceAddress.setCompanyNIP(companyNIP);
               orderInvoiceAddress.setCompanyName(updatedAddress.getCompanyName());
               orderInvoiceAddress.setPostCode(updatedAddress.getPostCode());
               orderInvoiceAddress.setStreet(updatedAddress.getStreet());
               orderInvoiceAddress.setHouseNumber(updatedAddress.getHouseNumber());
               orderInvoiceAddress.setApartmentNumber(updatedAddress.getApartmentNumber());
               orderInvoiceAddress.setCity(updatedAddress.getCity());
               orderInvoiceAddress.setTelephone(updatedAddress.getTelephone());

               orderInvoiceAddressRepository.save(orderInvoiceAddress);
           }
           return  updatedAddress;
       }
       return null;
    }


    public void createOrderDeliveryAddress(Long orderId, String firstName, String lastName,
                                            String street, String city, String postCode, String apartmentNumber,
                                            String houseNumber, String companyName, String companyNIP, String  telephone,String addressType){
        OrderTable orderTable = orderTableRepository.findById(orderId).orElse(null);
        if(orderTable !=null) {
            AppUser appUser = userDetailsService.getUserByUsername(orderTable.getUsername());

            //create address object
            GlobalAddress address = GlobalAddress.builder()
                    .addressType(addressType)
                    .street(street)
                    .firstName(firstName)
                    .lastName(lastName)
                    .city(city)
                    .postCode(postCode)
                    .companyName(companyName)
                    .companyNIP(companyNIP)
                    .houseNumber(houseNumber)
                    .apartmentNumber(apartmentNumber)
                    .appUser(appUser)
                    .telephone(telephone)
                    .build();
                GlobalAddress newAddress = addressRepository.save(address);

            orderDeliveryAddressRepository
                    .findByOrderId(orderId).ifPresent(orderDeliveryAddress -> orderDeliveryAddressRepository
                            .deleteById(orderDeliveryAddress.getId()));

            OrderDeliveryAddress newDeliveryAddress = OrderDeliveryAddress.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .companyName(companyName)
                        .street(street)
                        .city(city)
                        .houseNumber(houseNumber)
                        .apartmentNumber(apartmentNumber)
                        .postCode(postCode)
                        .telephone(telephone)
                        .orderId(orderId)
                        .globalAddressId(newAddress.getId())
                        .build();
                    orderDeliveryAddressRepository.save(newDeliveryAddress);
        }
    }

    public void createOrderInvoiceAddressPerson(Long orderId, String firstName, String lastName,
                                                    String street, String city, String postCode, String apartmentNumber,
                                                    String houseNumber, String  telephone,String addressType){
        OrderTable orderTable = orderTableRepository.findById(orderId).orElse(null);
        if(orderTable !=null) {
            AppUser appUser = userDetailsService.getUserByUsername(orderTable.getUsername());

            //create address object
            GlobalAddress address = GlobalAddress.builder()
                    .addressType(addressType)
                    .street(street)
                    .firstName(firstName)
                    .lastName(lastName)
                    .city(city)
                    .postCode(postCode)
                    .houseNumber(houseNumber)
                    .apartmentNumber(apartmentNumber)
                    .appUser(appUser)
                    .telephone(telephone)
                    .build();
            GlobalAddress newAddress = addressRepository.save(address);

            orderInvoiceAddressRepository
                    .findByOrderId(orderId).ifPresent(orderInvoiceAddress -> orderInvoiceAddressRepository
                            .deleteById(orderInvoiceAddress.getId()));

            OrderInvoiceAddress newDeliveryAddress = OrderInvoiceAddress.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .street(street)
                    .city(city)
                    .houseNumber(houseNumber)
                    .apartmentNumber(apartmentNumber)
                    .telephone(telephone)
                    .postCode(postCode)
                    .orderId(orderId)
                    .globalAddressId(newAddress.getId())
                    .build();
            orderInvoiceAddressRepository.save(newDeliveryAddress);
        }
    }

    public void createOrderInvoiceAddressCompany(Long orderId, String companyName, String companyNIP,
                                                String street, String city, String postCode, String apartmentNumber,
                                                String houseNumber, String  telephone,String addressType){
        OrderTable orderTable = orderTableRepository.findById(orderId).orElse(null);
        if(orderTable !=null) {
            AppUser appUser = userDetailsService.getUserByUsername(orderTable.getUsername());

            //create address object
            GlobalAddress address = GlobalAddress.builder()
                    .addressType(addressType)
                    .street(street)
                    .city(city)
                    .postCode(postCode)
                    .companyName(companyName)
                    .companyNIP(companyNIP)
                    .houseNumber(houseNumber)
                    .apartmentNumber(apartmentNumber)
                    .appUser(appUser)
                    .telephone(telephone)
                    .build();
            GlobalAddress newAddress = addressRepository.save(address);

            orderInvoiceAddressRepository
                    .findByOrderId(orderId).ifPresent(orderInvoiceAddress -> orderInvoiceAddressRepository
                            .deleteById(orderInvoiceAddress.getId()));

            OrderInvoiceAddress newDeliveryAddress = OrderInvoiceAddress.builder()
                    .companyNIP(companyNIP)
                    .companyName(companyName)
                    .street(street)
                    .city(city)
                    .houseNumber(houseNumber)
                    .apartmentNumber(apartmentNumber)
                    .telephone(telephone)
                    .postCode(postCode)
                    .orderId(orderId)
                    .globalAddressId(newAddress.getId())
                    .build();
            orderInvoiceAddressRepository.save(newDeliveryAddress);
        }
    }

    public void removeAddress(Long id){
        addressRepository.findById(id).ifPresent(address->addressRepository.deleteById(address.getId()));
    }
}
