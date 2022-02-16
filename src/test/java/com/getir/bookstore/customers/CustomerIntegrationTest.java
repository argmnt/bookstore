package com.getir.bookstore.customers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.bookstore.orders.Order;
import com.getir.bookstore.orders.OrderService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CustomerIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);


    @Test
    public void postCreateCustomer_andExpectCustomerCreated() throws Exception {
        var customerDTO = CustomerDTO.builder().age(25).email("askdfj@hotmail.com")
                .name("Customer 1 Name").password("123456").build();
        var result = mockMvc
                .perform(post("/customers")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(customerDTO))
                          ).andExpect(status().is(HttpStatus.CREATED.value())).andReturn();
        var resourceId = result.getResponse().getContentAsString();
        Assertions.assertNotNull(customerService.findCustomerById(resourceId));
    }

    @Test
    public void createOrdersOfCustomer_andExpectAllOrdersOfCustomersReturnsHasSameSize() {
        List<Order> orderList = new ArrayList<Order>();
        var createdCustomer1 = createCustomer("Cust1", 25, "john@hotmail.com");
        var createdCustomer2 = createCustomer("Cust2", 25, "doe@hotmail.com");
        //Orders will be filtered by customerId.
        orderService.saveOrder(Order.builder().customer(orderService.convertCustomerToOrderCustomer(createdCustomer1)).build());
        orderService.saveOrder(Order.builder().customer(orderService.convertCustomerToOrderCustomer(createdCustomer1)).build());
        orderService.saveOrder(Order.builder().customer(orderService.convertCustomerToOrderCustomer(createdCustomer2)).build());
        var ordersCreatedByCustomer1Count = 2;

        Page<Order> orderPage = orderService.findByCustomerId(createdCustomer1.getId(), 0, 50);
        Assertions.assertEquals(ordersCreatedByCustomer1Count, orderPage.getContent().size());
    }

    @Test
    public void getOrdersOfCustomer_andExpectAllOrdersOfCustomersReturns() throws Exception {
        var currentTime = getCurrentTime();
        var createdCustomer1 = createCustomer("Cust1", 25, "john@hotmail.com");
        var createdCustomer2 = createCustomer("Cust2", 25, "doe@hotmail.com");
        //Orders will be filtered by customerId.
        var order1CreatedByCust1 = createOrder(Order.builder()
                .customer(orderService.convertCustomerToOrderCustomer(createdCustomer1))
                .createdAt(currentTime).updatedAt(currentTime).build());
        var order2CreatedByCust1 = createOrder(Order.builder()
                .customer(orderService.convertCustomerToOrderCustomer(createdCustomer1)).build());
        createOrder(Order.builder()
                .customer(orderService.convertCustomerToOrderCustomer(createdCustomer2))
                .createdAt(currentTime).updatedAt(currentTime).build());
        //Create ordersOfCustomer1Json
        var ordersOfCustomer1Json = (JSONArray) jsonParser.parse(objectMapper.writeValueAsString(List.of(order1CreatedByCust1, order2CreatedByCust1)));
        //Perform Request
        var URL = "/customers/" + createdCustomer1.getId() + "/orders";
        var result = mockMvc.perform(get(URL)
                            .param("page", String.valueOf(0))
                            .param("size", String.valueOf(50)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.content[0].id").value(order1CreatedByCust1.getId()))
                            .andExpect(jsonPath("$.content[1].id").value(order2CreatedByCust1.getId()));
    }

    private Customer createCustomer(String name, int age, String email) {
        return customerService.saveCustomer(Customer.builder().age(age)
                .email(email).name(name).build());
    }

    private Order createOrder(Order order) {
        return orderService.saveOrder(order);
    }

    private static LocalDateTime getCurrentTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
    }
}
