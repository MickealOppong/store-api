package com.store.api.repository;

import com.store.api.model.orders.OrderInvoiceAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderInvoiceAddressRepository extends CrudRepository<OrderInvoiceAddress,Long> {
    Optional<OrderInvoiceAddress> findByOrderId(Long  OrderId);
    Optional<OrderInvoiceAddress> findByGlobalAddressId(Long  globalAddressId);
}
