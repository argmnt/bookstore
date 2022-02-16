package com.getir.bookstore.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.bookstore.books.BookService;
import com.getir.bookstore.customers.CustomerRepository;
import com.getir.bookstore.customers.CustomerService;
import com.getir.bookstore.exception.ResourceNotFoundException;
import com.getir.bookstore.inventory.InventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderValidator orderValidator;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void findOrderById_IfOrderNotExistsThrowResourcenotFoundExc() {
        //Given:
        assertThrows(ResourceNotFoundException.class, () -> {
            Optional<Order> order = Optional.empty();
            OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
            InventoryService inventoryService = Mockito.mock(InventoryService.class);
            BookService bookService = Mockito.mock(BookService.class);
            MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
            //When:
            when(orderRepository.findById(anyString())).thenReturn(order);
            OrderService orderService = new OrderServiceImpl(orderRepository, inventoryService,
                    bookService, mongoOperations);
            orderService.findById("Id");
        });
    }

    @Test
    public void invokeCreateOrderWithMinusQuantity_ExpectBadRequestWillBeThrown() throws Exception {
        var lineItemDTO = LineItemDTO.builder().quantity(-1l).build();
        OrderDTO orderDTO = OrderDTO.builder().lineItem(lineItemDTO).build();
        when(orderValidator.supports(any())).thenReturn(true);
        doCallRealMethod().when(orderValidator).validate(any(), any());
        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO))
                     )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void invokeCreateOrderWithNotValidCustomerDTO_ExpectBadRequestWillBeThrown() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder().lineItem(null).build();
        when(orderValidator.supports(any())).thenReturn(true);
        this.mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO))
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}
