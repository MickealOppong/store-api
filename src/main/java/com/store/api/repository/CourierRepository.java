package com.store.api.repository;

import com.store.api.model.util.Courier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends CrudRepository<Courier,Long> {

    Optional<Courier> findByCourier(String courier);

    List<Courier> findAll();

    @Query(value = "SELECT * FROM courier c ORDER BY c.courier ASC LIMIT 1",nativeQuery = true)
    Optional<Courier> findFirstItem();
}
