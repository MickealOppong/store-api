package com.store.api.repository;

import com.store.api.model.orders.OrderDeliveryAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDeliveryAddressRepository extends CrudRepository<OrderDeliveryAddress,Long> {

    Optional<OrderDeliveryAddress> findByOrderId(Long  OrderId);
    Optional<OrderDeliveryAddress> findByGlobalAddressId(Long  globalAddressId);
}
