package com.getir.bookstore.customers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    boolean existsByEmail(String email);


    Optional<Customer> findByEmail(String email);
}
