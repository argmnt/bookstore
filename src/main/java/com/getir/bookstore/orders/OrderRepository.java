package com.getir.bookstore.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Page<Order> findByCustomerId(String customerId, Pageable pageable);

    @Query("{'createdAt': {$gte: ?0, $lte:?1 }}")
    Page<Order> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
