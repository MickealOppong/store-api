package com.store.api.service;

import com.store.api.repository.CourierRepository;
import com.store.api.model.util.Courier;
import com.store.api.model.util.CourierDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CourierService {

    private final CourierRepository courierRepository;

    public CourierService(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    public Courier  save(String name, BigDecimal price,Long deliveryDays){
        Courier courier = new Courier(name,price,deliveryDays);
       return courierRepository.save(courier);
    }

    public List<CourierDto>  getAllCourier(){
        return courierRepository.findAll().stream().map(CourierDto::new).toList();
    }

    public void updatePrice(Long id,BigDecimal price){
       Optional<Courier> courier= courierRepository.findById(id);
       if (courier.isPresent()){
           Courier item = courier.get();
           item.setPrice(price);
           courierRepository.save(item);
       }
    }
}
