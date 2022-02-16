package com.getir.bookstore.customers;

import com.getir.bookstore.orders.Order;
import org.springframework.data.domain.Page;

import org.springframework.security.core.Authentication;

public interface CustomerService {

    Customer findCustomerById(String id);

    String createCustomer(CustomerDTO customerDTO);

    Customer saveCustomer(Customer customer);

    Page<Order> findOrdersCustomerById(String id, int page, int size);

    boolean existsByEmail(String email);

    Customer findByAuthentication(Authentication authentication);

    Page<Customer> getCustomers(int page, int size);
}
