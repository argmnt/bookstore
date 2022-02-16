package com.getir.bookstore.statistics;

import com.getir.bookstore.books.Book;
import com.getir.bookstore.books.BookService;
import com.getir.bookstore.customers.Customer;
import com.getir.bookstore.customers.CustomerService;
import com.getir.bookstore.orders.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsIntegrationTest {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void beforeEach() {
        orderRepository.deleteAll();
    }

    @Test
    public void createOrders_getStatisticsAndExpectGroupSizeEqualsGivenGroupCount() {
        final int MONTH_GROUP_COUNT = 2;
        var createdCustomer = createCustomer();
        createRandomOrders(createdCustomer, MONTH_GROUP_COUNT);
        List<StatisticsDTO> statisticsDTOS = statisticsService.getCustomerStatistics(createdCustomer.getId());
        Assertions.assertEquals(statisticsDTOS.size(), MONTH_GROUP_COUNT);
    }

    /**
     * Creates 9 orders for next month {totalCount : 27, totalPrice : 800}
     * And Creates 8 orders for the month after next month. {totalCount : 24, totalPrice: 800}
     * @param customer
     */
    private void createRandomOrders(Customer customer, int MONTH_GROUP_COUNT) {
        for (int i = 1; i <= MONTH_GROUP_COUNT; i++) {
            var now = LocalDateTime.now().plusMonths(i);
            for (int j = 0; j < 10 - i; j++) {
                var createdBook = createBook(String.format("Author%d%d", i, j) , String.format("BookName%d%d", i, j),
                        String.format("Description%d%d", i, j));
                var lineItem = createLineItem(createdBook, 3l);
                createOrder(customer, List.of(lineItem), now);
            }
        }
    }

    public Customer createCustomer() {
        LocalDateTime now = getCurrentTime();
        var customer = Customer.builder().age(25).email("email@hotmail.com").name("John Doe").createdAt(now).updatedAt(now).build();
        return customerService.saveCustomer(customer);
    }

    public Order createOrder(Customer customer, List<LineItem> lineItems, LocalDateTime now) {
        var order = Order.builder()
                .customer(orderService.convertCustomerToOrderCustomer(customer))
                .lineItems(lineItems)
                .createdAt(now).updatedAt(now).orderStatus(OrderStatus.CREATED).build();
        return orderService.saveOrder(order);
    }

    private Book createBook(String author, String name, String description) {
        var now = getCurrentTime();
        var book = Book.builder().author(author).name(name)
                .ISBN("1222300-34322").description(description)
                .price(BigDecimal.valueOf(100.0)).build();
        return bookService.saveBook(book);
    }

    private LineItem createLineItem(Book book, long quantity) {
        return LineItem.builder().book(book).quantity(quantity).build();
    }

    private static LocalDateTime getCurrentTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
    }
}
