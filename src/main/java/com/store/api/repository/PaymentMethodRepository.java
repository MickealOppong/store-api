package com.store.api.repository;

import com.store.api.model.util.PaymentMethod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends CrudRepository<PaymentMethod,Long> {
    Optional<PaymentMethod> findByPaymentMethod(String paymentMethod);

    List<PaymentMethod>   findAll();

    @Query(value = "SELECT * FROM payment_method c ORDER BY c.payment_method ASC LIMIT 1",nativeQuery = true)
    Optional<PaymentMethod> findFirstItem();
}
