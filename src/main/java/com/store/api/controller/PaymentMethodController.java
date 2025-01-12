package com.store.api.controller;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.util.PaymentMethodDto;
import com.store.api.service.PaymentMethodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService  paymentMethodService){
        this.paymentMethodService= paymentMethodService;
    }

    @PostMapping
    public String addMethodOfPayment(String paymentMethod, MultipartFile image){
        try {
            paymentMethodService.save(paymentMethod,image);
            return "saved";
        }catch (InvalidOperationException e){
            return e.getMessage();
        }
    }

    @GetMapping
    public List<PaymentMethodDto> getPaymentMethodList(){
        return paymentMethodService.getPaymentMethods();
    }

}
