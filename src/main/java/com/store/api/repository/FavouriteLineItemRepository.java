package com.store.api.repository;

import com.store.api.model.favourite.FavouriteLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavouriteLineItemRepository extends JpaRepository<FavouriteLineItem,Long> {

    Optional<FavouriteLineItem> findByProductName(String productName);
    @Query(value = "SELECT * FROM favourite_line_item u WHERE u.product_id=?1 AND u.rec_id=?2",nativeQuery = true)
    Optional<FavouriteLineItem> findByProductIdAndRecId(Long productId,Long recId);


    @Query(value = "SELECT * FROM favourite_line_item u WHERE u.rec_id=?1",nativeQuery = true)
    List<FavouriteLineItem> findAllByRecId(Long recId);

    @Modifying
    @Query(value = "DELETE FROM favourite_line_item u WHERE u.rec_id=?1",nativeQuery = true)
    void deleteByRecId(Long recId);

    List<FavouriteLineItem> findAllById(Long id);
}
