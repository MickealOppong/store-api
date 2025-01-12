package com.store.api.repository;

import com.store.api.model.product.ProductCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends CrudRepository<ProductCategory,Long> {

    Optional<ProductCategory> findByName(String name);
    List<ProductCategory> findByProductIdOrderByCategoryHierarchy(Long productId);
}
