package com.store.api.repository;

import com.store.api.model.cart.CartLineItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartLineItemRepository extends CrudRepository<CartLineItem,Long> {

    Optional<CartLineItem> findByProductName(String productName);
    @Query(value = "SELECT * FROM cart_line_item u WHERE u.product_id=?1 AND u.rec_id=?2",nativeQuery = true)
    Optional<CartLineItem> findByProductIdAndRecId(Long productId,Long recId);
    List<CartLineItem> findAll();

    @Query(value = "SELECT * FROM cart_line_item u WHERE u.rec_id=?1",nativeQuery = true)
    List<CartLineItem> findAllByRecId(Long recId);

    @Modifying
    @Query(value = "DELETE FROM cart_line_item u WHERE u.rec_id=?1",nativeQuery = true)
    void deleteByRecId(Long recId);

    List<CartLineItem> findAllById(Long id);
}
