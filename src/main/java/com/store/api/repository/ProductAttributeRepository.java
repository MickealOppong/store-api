package com.store.api.repository;

import com.store.api.model.product.ProductAttribute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductAttributeRepository extends CrudRepository<ProductAttribute,Long> {
    Optional<ProductAttribute> findByName(String name);

    @Query(value = "SELECT * FROM product_attributes p where p.product_name like %?1%",nativeQuery = true)
    List<ProductAttribute>  findAttributesByProductName(String name);



    Optional<ProductAttribute> findByValueAndProductId(String value,Long productId);
    List<ProductAttribute> findByProductId(Long productId);
    List<ProductAttribute> findAll();

}

