package com.store.api.repository;

import com.store.api.model.favourite.FavouriteTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteTableRepository extends CrudRepository<FavouriteTable,Long> {
    Optional<FavouriteTable> findByUsername(String username);
    Optional<FavouriteTable> findByUserId(Long userId);
}
