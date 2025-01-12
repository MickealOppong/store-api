package com.store.api.repository;

import com.store.api.model.inventory.ItemAttributeValue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemAttributeValueRepository extends CrudRepository<ItemAttributeValue,Long> {
    Optional<ItemAttributeValue> findByName(String name);

    List<ItemAttributeValue> findByItemAttributeId(Long itemAttributeId);
}
