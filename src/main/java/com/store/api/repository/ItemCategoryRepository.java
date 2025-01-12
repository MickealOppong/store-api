package com.store.api.repository;

import com.store.api.model.inventory.ItemCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCategoryRepository extends CrudRepository<ItemCategory,Long> {
    Optional<ItemCategory> findByName(String name);
    List<ItemCategory> findAll();
}
