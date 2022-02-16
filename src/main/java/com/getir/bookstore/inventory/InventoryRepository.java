package com.getir.bookstore.inventory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {

    @Query("{ 'book._id' : ?0}")
    Optional<Inventory> findByBookId(String bookId);
}
