package com.getir.bookstore.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.bookstore.books.Book;
import com.getir.bookstore.books.BookService;
import com.getir.bookstore.customers.Customer;
import com.getir.bookstore.customers.CustomerRepository;
import com.getir.bookstore.customers.CustomerService;
import com.getir.bookstore.inventory.Inventory;
import com.getir.bookstore.inventory.InventoryService;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired OrderRepository orderRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

    @BeforeEach
    public void beforeEach() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void createOrder_expectOrderIsSaved() {
        var order = createOrder();
        var fetchedOrder = orderService.findById(order.getId());
        assertEquals(order.getId(), fetchedOrder.getId());
    }

    @Test
    public void getOrderById_expectOrderShouldReturn() throws Exception {
        var order = createOrder();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + order.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    public void createOrder_andExpectStockDecreased() {
        var demandQuantity = 10l;
        var stockQuantity = BigInteger.valueOf(50l);
        var createdBook = createBook("Ataturk", "Nutuk");
        var orderDTO = createOrderDTO(createdBook, demandQuantity);
        var customer = createCustomer();
        createInventory(createdBook, stockQuantity);
        orderService.createOrder(orderDTO, customer);
        var updatedInventory = inventoryService.findByBookId(createdBook.getId());
        var difference = stockQuantity.subtract(BigInteger.valueOf(demandQuantity));
        assertEquals(updatedInventory.getQuantity(), difference);
    }

    private Customer createCustomer() {
        var customer = Customer.builder().name("Bob").email(LocalDateTime.now().toString() + "@hotmail.com").age(25).build();
        return customerService.saveCustomer(customer);
    }

    @Test
    public void getOrdersByCreatedTimeBetween_andExpectOrdersBetweenCratedTimeFilters() {
        //given
        var currentLocalDateTime = LocalDateTime.now();
        var currentCreatedOrder = Order.builder().createdAt(currentLocalDateTime).build();
        var oneMonthsLaterFutureOrder = Order.builder().createdAt(currentLocalDateTime.plusMonths(1)).build();
        var savedCurrentCreatedOrder = orderService.saveOrder(currentCreatedOrder);
        var savedOneMonthsLaterFutureOrder = orderService.saveOrder(oneMonthsLaterFutureOrder);

        var localDateTimeTwoMonthsLater = LocalDateTime.now().plusMonths(2);
        var twoMonthsLaterFutureOrder = Order.builder().createdAt(localDateTimeTwoMonthsLater).updatedAt(localDateTimeTwoMonthsLater).build();
        var savedTwoMonthsLaterFutureOrder = orderService.saveOrder(twoMonthsLaterFutureOrder);

        //when
        var orderCriteriaFilterDTO = OrderCriteriaFilterDTO.builder().startDateTime(currentLocalDateTime.minusDays(1))
                .endDateTime(currentLocalDateTime.plusMonths(1)).build();

        var createdOrdersListBetweenCriteria = List.of(savedCurrentCreatedOrder, savedOneMonthsLaterFutureOrder);
        var resultList = orderService.findOrdersBetweenDates(orderCriteriaFilterDTO, 0, 50).get().collect(Collectors.toList());
        //expect
        assertEquals(createdOrdersListBetweenCriteria, resultList);
    }

    public Order createOrder() {
        var customer = Customer.builder().age(20).email("fowler@hotmail.com").name("Yagiz").build();
        var book = createBook("Kafka", "Donusum");
        var lineItem = createLineItem(book);
        var now = LocalDateTime.now().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
        var order = Order.builder()
                .customer(orderService.convertCustomerToOrderCustomer(customer))
                .lineItems(List.of(lineItem))
                .createdAt(now).updatedAt(now).orderStatus(OrderStatus.CREATED).build();
        return orderService.saveOrder(order);
    }

    private OrderDTO createOrderDTO(Book book, long demandQuantity) {
        return OrderDTO.builder().lineItem(createLineItemDTO(book, demandQuantity)).build();
    }

    private Book createBook(String author, String name) {
        var book = Book.builder().author(author).name(name).ISBN("1222300-34322")
                .description("Franz Kafka Donusum").price(BigDecimal.valueOf(100.0)).build();
        return bookService.saveBook(book);
    }

    private LineItemDTO createLineItemDTO(Book book, long demandQuantity) {
        return LineItemDTO.builder().bookId(book.getId()).quantity(demandQuantity).build();
    }

    private LineItem createLineItem(Book book) {
        return LineItem.builder().book(book).quantity(30l).build();
    }

    private Inventory createInventory(Book book, BigInteger stockQuantity) {
        var inventory = Inventory.builder().book(book).quantity(stockQuantity).build();
        return inventoryService.saveInventory(inventory);
    }
}
