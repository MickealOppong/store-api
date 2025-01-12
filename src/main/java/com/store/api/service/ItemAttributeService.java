package com.store.api.service;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.inventory.ItemAttribute;
import com.store.api.model.inventory.ItemAttributeValue;
import com.store.api.repository.ItemAttributeRepository;
import com.store.api.repository.ItemAttributeValueRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemAttributeService {

    private final ItemAttributeRepository itemAttributeRepository;
    private final ItemAttributeValueRepository itemAttributeValueRepository;


    public ItemAttributeService(ItemAttributeRepository itemAttributeRepository,
                                ItemAttributeValueRepository itemAttributeValueRepository) {
        this.itemAttributeRepository = itemAttributeRepository;
        this.itemAttributeValueRepository = itemAttributeValueRepository;
    }

    public ItemAttribute saveAttribute(String name,String description){
        if(name != null && description !=null){
           return itemAttributeRepository.save(new ItemAttribute(name,description));
        }
        return  null;
    }

    public ItemAttributeValue saveAttributeValue(String attributeName,String name, String value){
      ItemAttribute itemAttribute= itemAttributeRepository.findByName(attributeName)
              .orElseThrow(()->new InvalidOperationException("Attribute name does not exist"));
      return itemAttributeValueRepository.save(new ItemAttributeValue(name,value,itemAttribute));
    }

    public ItemAttribute  getItemAttribute(String name){
        return itemAttributeRepository.findByName(name)
                .orElseThrow(()-> new InvalidOperationException(String.format("%s  %s",name,"does not exist")));
    }

    public  Map<String, List<String>>   getAllItemAttributesAndValues(){
        Map<String, List<String>> attributeList = new HashMap<>();
        for(ItemAttribute  attribute:itemAttributeRepository.findAll()){
            attributeList.put(attribute.getName(),itemAttributeValueRepository.findByName(attribute.getName())
                    .stream().map(ItemAttributeValue::getName).toList());
        }
        return attributeList;
    }

    public  Map<String, List<String>>   getItemAttributeAndValues(String attributeName){
        Map<String, List<String>> attributeList = new HashMap<>();
        ItemAttribute itemAttribute= getItemAttribute(attributeName);
           attributeList.put(itemAttribute.getName(), itemAttributeValueRepository.findByItemAttributeId(itemAttribute.getId())
                   .stream().map(ItemAttributeValue::getValue).toList());
     return attributeList;
    }

}
