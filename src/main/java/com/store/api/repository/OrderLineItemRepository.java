package com.store.api.repository;

import com.store.api.model.orders.OrderLineItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemRepository extends CrudRepository<OrderLineItem,Long> {
    Optional<OrderLineItem> findByProductId(Long productId);

    @Query(value = "SELECT * FROM order_line_item u WHERE u.order_id=?1",nativeQuery = true)
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
