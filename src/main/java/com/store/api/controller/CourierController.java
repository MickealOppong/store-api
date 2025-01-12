package com.store.api.controller;

import com.store.api.service.CourierService;
import com.store.api.model.util.Courier;
import com.store.api.model.util.CourierDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/courier")
public class CourierController {

    private  final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping
    public ResponseEntity<List<CourierDto>> all(){
      return ResponseEntity.ok().body(  courierService.getAllCourier());
    }

    @PostMapping
    public ResponseEntity<String>  add(String name, Double price){
       Courier  courier= courierService.save(name,BigDecimal.valueOf(price),2L);
       if(courier!=null){
           return ResponseEntity.ok().body("created");
       }
        return ResponseEntity.badRequest().body("failed");
    }

    @PatchMapping
    public void updatePrice(Long id,Double price){
        courierService.updatePrice(id,BigDecimal.valueOf(price));
    }

}
