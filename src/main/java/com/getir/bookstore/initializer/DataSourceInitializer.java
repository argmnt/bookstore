package com.getir.bookstore.initializer;

import com.getir.bookstore.books.Book;
import com.getir.bookstore.books.BookRepository;
import com.getir.bookstore.books.BookService;
import com.getir.bookstore.customers.Customer;
import com.getir.bookstore.customers.CustomerRepository;
import com.getir.bookstore.customers.CustomerService;
import com.getir.bookstore.inventory.InventoryRepository;
import com.getir.bookstore.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSourceInitializer implements CommandLineRunner {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final BookService bookService;

    private final BookRepository bookRepository;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    private final InventoryRepository inventoryRepository;

    private final PasswordEncoder passwordEncoder;

    private void deleteEverything() {
        orderRepository.deleteAll();
        bookRepository.deleteAll();
        customerRepository.deleteAll();
        inventoryRepository.deleteAll();
    }

    @Override
    public void run(String... args) {
        deleteEverything();
        var customer = createCustomer();
        customer = customerService.saveCustomer(customer);

        final int MONTH_GROUP_COUNT = 2;

        createRandomOrders(customer, MONTH_GROUP_COUNT);

    }

    private Customer createCustomer() {
        return Customer.builder().age(20).email("admin@hotmail.com").name("Yagiz")
                .password(passwordEncoder.encode("123456")).build();
    }

    public Order createOrder(Customer customer, List<LineItem> lineItems, LocalDateTime now) {
        var order = Order.builder()
                .customer(orderService.convertCustomerToOrderCustomer(customer))
                .lineItems(lineItems)
                .createdAt(now).updatedAt(now).orderStatus(OrderStatus.CREATED).build();
        return orderService.saveOrder(order);
    }

    private void createRandomOrders(Customer customer, int MONTH_GROUP_COUNT) {
        for (int i = 1; i <= MONTH_GROUP_COUNT; i++) {
            var now = LocalDateTime.now().plusMonths(i);
            for (int j = 0; j < 10 - i; j++) {
                var createdBook = createBook(String.format("Author%d%d", i, j), String.format("BookName%d%d", i, j),
                        String.format("Description%d%d", i, j));
                var lineItem = createLineItem(createdBook, 3l);
                createOrder(customer, List.of(lineItem), now);
            }
        }
    }

    private LineItem createLineItem(Book book, long quantity) {
        return LineItem.builder().book(book).quantity(quantity).build();
    }

    private Book createBook(String author, String name, String description) {
        var book = Book.builder().author(author).name(name)
                .ISBN("1222300-34322").description(description)
                .price(BigDecimal.valueOf(100.0)).build();
        return bookService.saveBook(book);
    }

}
