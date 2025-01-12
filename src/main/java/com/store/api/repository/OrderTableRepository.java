package com.store.api.repository;

import com.store.api.model.orders.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable,Long> {

    List<OrderTable> findByUsername(String username);
   List<OrderTable> findByUserId(Long userId);
    Optional<OrderTable> findByUsernameAndCompleted(String username, boolean  completed);

    //@Query(value = "SELECT * FROM  order_table o where o.user_id=?1 AND o.completed=?2",nativeQuery = true)
    Optional<OrderTable> findByUserIdAndCompleted(Long userId, boolean  completed);

}
