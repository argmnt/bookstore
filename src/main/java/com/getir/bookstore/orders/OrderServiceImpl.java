package com.getir.bookstore.orders;

import com.getir.bookstore.books.Book;
import com.getir.bookstore.books.BookService;
import com.getir.bookstore.customers.Customer;
import com.getir.bookstore.exception.NoStockFoundException;
import com.getir.bookstore.exception.ResourceNotFoundException;
import com.getir.bookstore.inventory.Inventory;
import com.getir.bookstore.inventory.InventoryService;
import com.mongodb.MongoCommandException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final InventoryService inventoryService;

    private final BookService bookService;

    private final MongoOperations mongoOperations;

    @Override
    public Order findById(String id) {
        return orderRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public String createOrder(OrderDTO orderDTO, Customer customer) {
        var book = bookService.findById(orderDTO.getLineItem().getBookId());
        var lineItemDTO = orderDTO.getLineItem();
        var order = Order.builder().orderStatus(OrderStatus.CREATED)
                .lineItems(List.of(createLineItem(book, lineItemDTO.getQuantity())))
                .customer(convertCustomerToOrderCustomer(customer))
                .build();
        return saveOrderAndUpdateInventory(book.getId(), order).getId();
    }

    @Override
    public com.getir.bookstore.orders.Customer convertCustomerToOrderCustomer(Customer customer) {
        return com.getir.bookstore.orders.Customer.builder()
                .age(customer.getAge()).createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .email(customer.getEmail()).id(customer.getId())
                .name(customer.getName()).build();
    }

    @Override
    public LineItem createLineItem(Book book, long demandQuantity) {
        return LineItem.builder().book(book).quantity(demandQuantity).build();
    }

    @Transactional
    @Retryable( value = {UncategorizedMongoDbException.class, MongoCommandException.class},
            maxAttempts = 2, backoff = @Backoff(delay = 100))
    @Override
    public Order saveOrderAndUpdateInventory(String bookId, Order order) {
        var inventory = inventoryService.findByBookId(bookId);
        var remainingStockQuantity = inventoryService.calculateStockDifference(inventory.getQuantity(),
                                                    BigInteger.valueOf(order.getLineItems().get(0).getQuantity()));
        if (hasEnoughStock(remainingStockQuantity)) {
            throw new NoStockFoundException();
        }
            mongoOperations.updateFirst(
                    Query.query(Criteria.
                            where("version").is(inventory.getVersion())
                            .and("book.id").is(inventory.getBook().getId())
                    ),
                    Update.update("quantity", remainingStockQuantity),
                    Inventory.class);
            return mongoOperations.insert(order);
    }

    private boolean hasEnoughStock(BigInteger remainingStockQuantity) {
        return remainingStockQuantity.signum() < 0;
    }

    @Override
    public Page<Order> findByCustomerId(String customerId, int page, int size) {
        return orderRepository.findByCustomerId(customerId, PageRequest.of(page, size));
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> findOrdersBetweenDates(OrderCriteriaFilterDTO orderCriteriaFilterDTO, int page, int size) {
        return orderRepository.findByCreatedAtBetween(orderCriteriaFilterDTO.getStartDateTime(),
                orderCriteriaFilterDTO.getEndDateTime(), PageRequest.of(page, size));
    }
}
