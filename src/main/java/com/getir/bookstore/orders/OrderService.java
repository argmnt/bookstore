package com.getir.bookstore.orders;

import com.getir.bookstore.books.Book;
import com.getir.bookstore.customers.Customer;
import org.springframework.data.domain.Page;

public interface OrderService {

    Order findById(String id);

    String createOrder(OrderDTO orderDTO, Customer customer);

    LineItem createLineItem(Book book, long demandQuantity);

    Order saveOrderAndUpdateInventory(String bookId, Order order);

    Page<Order> findByCustomerId(String customerId, int page, int size);

    Order saveOrder(Order order);

    Page<Order> findOrdersBetweenDates(OrderCriteriaFilterDTO orderCriteriaFilterDTO, int page, int size);

    com.getir.bookstore.orders.Customer convertCustomerToOrderCustomer(Customer customer);
}
