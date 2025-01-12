package com.store.api.repository;

import com.store.api.model.cart.CartTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartTableRepository extends CrudRepository<CartTable,Long> {

    Optional<CartTable> findByUsername(String username);
    Optional<CartTable> findByUserId(Long userId);
    Optional<CartTable> findBySessionId(String sessionId);

}
