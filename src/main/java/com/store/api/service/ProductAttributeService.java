package com.store.api.service;

import com.store.api.exceptions.InvalidOperationException;
import com.store.api.model.inventory.ItemAttribute;
import com.store.api.model.product.AttributeDTO;
import com.store.api.model.product.Product;
import com.store.api.model.product.ProductAttribute;
import com.store.api.model.product.ProductAttributeDTO;
import com.store.api.repository.ItemAttributeRepository;
import com.store.api.repository.ItemAttributeValueRepository;
import com.store.api.repository.ProductAttributeRepository;
import com.store.api.repository.ProductAttributeValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ItemAttributeRepository itemAttributeRepository;
    private final ItemAttributeValueRepository itemAttributeValueRepository;

    public ProductAttributeService(ProductAttributeRepository productAttributeRepository,
                                   ProductAttributeValueRepository productAttributeValueRepository,
                                   ItemAttributeRepository itemAttributeRepository,
                                   ItemAttributeValueRepository itemAttributeValueRepository) {
        this.productAttributeRepository = productAttributeRepository;
        this.productAttributeValueRepository = productAttributeValueRepository;
        this.itemAttributeRepository = itemAttributeRepository;
        this.itemAttributeValueRepository = itemAttributeValueRepository;
    }

    public String saveAttributeAndValues(String  name,String value, Product product){
        ItemAttribute itemAttribute =itemAttributeRepository.findByName(name)
                .orElseThrow(()->new InvalidOperationException(name+ " does not exist"));
       Optional<ProductAttribute>  retrieveAttribute=
               productAttributeRepository.findByValueAndProductId(value, product.getId());
       if(retrieveAttribute.isPresent()){
           return String.format("%s %s",name,"already exist");
       }
        ProductAttribute productAttribute = new ProductAttribute(itemAttribute.getName(),value,product);
        productAttributeRepository.save(productAttribute);
        return String.format("%s %s",name,"saved");
    }

    public List<ProductAttributeDTO> getAttributeAndValues(Long  productId){
        List<ProductAttributeDTO> productAttributeDTO = new ArrayList<>();
     for(ProductAttribute  productAttribute:productAttributeRepository.findByProductId(productId)){
       ProductAttributeDTO attributeDTO = ProductAttributeDTO.builder()
               .id(productAttribute.getId())
               .name(productAttribute.getName())
               .value(productAttribute.getValue())
               .build();
       productAttributeDTO.add(attributeDTO);
        }
      return productAttributeDTO;
    }

    public Map<String,Set<String>> getAvailableAttributes(){
        Map<String,Set<String>> attributeMap= new HashMap<>();
        for(ProductAttribute  productAttribute :productAttributeRepository.findAll()){

            attributeMap.put(productAttribute.getName(),productAttributeRepository.findAll()
                    .stream().filter(f->f.getName().equals(productAttribute.getName()))
                    .map(ProductAttribute::getValue).collect(Collectors.toSet()));
        }
        return attributeMap;
    }

 public   Set<AttributeDTO> getAttributesByProductName(String name){
        Set<AttributeDTO> attributeDTOS =new HashSet<>();

     for(ProductAttribute productAttribute :productAttributeRepository.findAttributesByProductName(name)){
         AttributeDTO attributeDTO = AttributeDTO.builder()
                 .name(productAttribute.getName())
                 .productAttributeDTOS(productAttributeRepository.findAttributesByProductName(name)
                         .stream().filter(f->f.getName().equals(productAttribute.getName()))
                         .map(ProductAttributeDTO::new).toList())
                 .build();
         attributeDTOS.add(attributeDTO);
     }
     return attributeDTOS;
 }

 public ProductAttribute getById(Long id){
      return  productAttributeRepository.findById(id)
              .orElseThrow(()->new InvalidOperationException(id+" does not exist"));
 }
}
