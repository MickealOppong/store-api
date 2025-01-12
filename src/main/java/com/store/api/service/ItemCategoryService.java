package com.store.api.service;

import com.store.api.model.inventory.ItemCategory;
import com.store.api.model.product.ProductCategoryDTO;
import com.store.api.repository.ItemCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ItemCategoryService {


    private final ItemCategoryRepository itemCategoryRepository;



    public ItemCategoryService(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;

    }

    public ItemCategory addCategory(ProductCategoryDTO productCategoryDTO){
        log.info(productCategoryDTO+"");
        ItemCategory itemCategory = ItemCategory.builder()
                .name(productCategoryDTO.getName())
                .build();
        return itemCategoryRepository.save(itemCategory);
    }

    public ItemCategory getItemCategoryByName(String name){
          return  itemCategoryRepository.findByName(name).orElse(null);
    }

}
