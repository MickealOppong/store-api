package com.store.api.controller;

import com.store.api.exceptions.ProductNotFoundException;
import com.store.api.model.product.AttributeDTO;
import com.store.api.model.product.ProductsDTO;
import com.store.api.model.product.SingleProductDTO;
import com.store.api.service.ProductAttributeService;
import com.store.api.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {

    private final ProductService productService;
    private final ProductAttributeService productAttributeService;

    public StoreController(ProductService productService, @Lazy ProductAttributeService productAttributeService) {
        this.productService = productService;
        this.productAttributeService =productAttributeService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductsDTO>> all(){
        return ResponseEntity.ok().body(productService.all());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SingleProductDTO> product(@RequestParam Long id){
        try{
            return ResponseEntity.ok().body(productService.getById(id));
        }catch (ProductNotFoundException | NullPointerException | InvalidDataAccessApiUsageException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/attributes")
    public Map<String, Set<String>> attributes(){
        return productAttributeService.getAvailableAttributes();
    }

    @GetMapping("/item")
    public  Set<AttributeDTO> getProduct(String productName){
       return productAttributeService.getAttributesByProductName(productName);
    }

}
