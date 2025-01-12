package com.store.api.repository;

import com.store.api.model.inventory.ItemAttribute;
import com.store.api.model.product.ProductAttributeValue;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductAttributeValueRepository extends CrudRepository<ProductAttributeValue,Long> {
    Optional<ItemAttribute> findByName(String name);
    List<ProductAttributeValue> findByProductAttributeId(Long productAttributeId);
}
