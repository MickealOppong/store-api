package com.store.api.repository;

import com.store.api.model.util.AppImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppImageRepository extends CrudRepository<AppImage,Long> {

    List<AppImage> findAll();
    Optional<AppImage> findByPath(String path);

    @Query(value= "SELECT * FROM photos p where p.uid=?1",nativeQuery = true)
    Optional<AppImage> findByUserId(Long uid);

    @Query(value= "SELECT * FROM photos p where p.product_id=?1",nativeQuery = true)
    List<AppImage> findByProductId(Long pid);

    @Query(value= "SELECT * FROM photos p where p.payment_id=?1",nativeQuery = true)
    Optional<AppImage> findByPaymentMethodId(Long paymentId);

    @Transactional
    @Modifying
    @Query(value= "DELETE FROM photos p where p.path=?1",nativeQuery = true)
    void deleteByPath(String path);
}
