package com.store.api.service;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.util.PaymentMethod;
import com.store.api.model.util.PaymentMethodDto;
import com.store.api.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository  paymentMethodRepository;
    private  final PhotoService photoService;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, PhotoService photoService) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.photoService = photoService;
    }

    public String save(String paymentMethod, MultipartFile image){
       if( paymentMethodRepository.findByPaymentMethod(paymentMethod).isPresent()){
           throw new InvalidOperationException("Payment method  already exist");
       }
        if(Objects.requireNonNull(image.getOriginalFilename()).length()!=0 && paymentMethod!=null){
            PaymentMethod  method = PaymentMethod.builder()
                    .paymentMethod(paymentMethod)
                    .build();
            PaymentMethod savedItem= paymentMethodRepository.save(method);
            photoService.savePaymentMethodImage(image, savedItem.getPaymentMethod());
            return "Saved";
        }else {
            throw  new InvalidOperationException("Image or payment method type is required");
        }
    }

    public List<PaymentMethodDto> getPaymentMethods(){
        List<PaymentMethodDto>  paymentMethodList = new ArrayList<>();
       for(PaymentMethod method: paymentMethodRepository.findAll()){
           PaymentMethodDto paymentMethodDto = PaymentMethodDto.builder()
                   .id(method.getId())
                   .paymentMethod(method.getPaymentMethod())
                   .image(photoService.getPaymentImage(method.getId()))
                   .build();
           paymentMethodList.add(paymentMethodDto);
       }
       return paymentMethodList;
    }


    public String getOne(){
       return paymentMethodRepository.findFirstItem().map(PaymentMethod::getPaymentMethod).orElse("");
    }


    public String getPaymentMethod(Long id){
        return paymentMethodRepository.findById(id).map(PaymentMethod::getPaymentMethod).orElse(null);
    }

    public String getPaymentMethod(String paymentMethod){
        return paymentMethodRepository.findByPaymentMethod(paymentMethod).map(PaymentMethod::getPaymentMethod).orElse(null);
    }
}
