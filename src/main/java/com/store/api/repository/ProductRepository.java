package com.store.api.repository;

import com.store.api.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT * FROM products p where p.name like %?1%",nativeQuery = true)
    List<Product> findProductByName(String name);

    List<Product> findAll();

    @Query(value = "SELECT * FROM  products p where p.is_featured_product=true ORDER BY name RAND() Limit 20",nativeQuery = true)
    List<Product> getFeaturedProducts();

    @Query(value = "SELECT * FROM  products p where p.is_new_arrival=true ORDER BY name RAND() Limit 20",nativeQuery = true)
    List<Product> getNewArrivalProducts();


}
