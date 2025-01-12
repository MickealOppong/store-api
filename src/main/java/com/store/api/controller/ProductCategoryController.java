package com.store.api.controller;

import com.store.api.model.inventory.ItemCategory;
import com.store.api.model.product.ProductCategoryDTO;
import com.store.api.service.ItemCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class ProductCategoryController {

    private final ItemCategoryService itemCategoryService;

    public ProductCategoryController(ItemCategoryService itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
    }

    @PostMapping("/product-category")
    public ResponseEntity<String> add(@RequestBody ProductCategoryDTO productCategoryDTO){
       ItemCategory category = itemCategoryService.addCategory(productCategoryDTO);
       if(category != null){
           return ResponseEntity.ok().body("ItemCategory created");
       }
        return ResponseEntity.badRequest().body("ItemCategory created");
    }


    @GetMapping("/all")
    public Map<String,List<String>>  all(){
       return null;
    }
}
