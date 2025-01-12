package com.store.api.repository;

import com.store.api.model.inventory.ItemAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemAttributeRepository extends CrudRepository<ItemAttribute,Long> {
    Optional<ItemAttribute> findByName(String name);
}
