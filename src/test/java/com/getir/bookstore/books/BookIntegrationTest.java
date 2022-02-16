package com.getir.bookstore.books;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.bookstore.inventory.Inventory;
import com.getir.bookstore.inventory.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postCreateBook_ExpectBookIsCreated() throws Exception {
        var bookDTO = BookDTO.builder().author("Uncle Bob")
                .description("Crafting clean code")
                .name("Clean Code").ISBN("210984203-12348")
                .price(BigDecimal.valueOf(20.50)).build();
        var result = mockMvc
                .perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO))
                ).andExpect(status().is(HttpStatus.CREATED.value())).andReturn();
        var resourceId = result.getResponse().getContentAsString();
        assertNotNull(bookService.findById(resourceId));
    }

    @Test
    public void patchRequestUpdateBookStock_ExpectStockQuantityUpdated() throws Exception {
        var book = createBook();
        createInventory(book);
        var bookStockUpdateDTO = BookStockUpdateDTO.builder().quantity(30).build();

        var resourceURL = "/books/" + book.getId() + "/inventory/stock";
        mockMvc.perform(patch(resourceURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookStockUpdateDTO))
                ).andExpect(status().isOk());
        var updatedInventory = inventoryService.findByBookId(book.getId());
        assertEquals(updatedInventory.getQuantity(), BigInteger.valueOf(bookStockUpdateDTO.getQuantity()));
    }

    @Test
    public void patchRequestUpdateBookStockWithInvalidBookId_ExpectNotFoundStatus() throws Exception {
        var bookStockUpdateDTO = BookStockUpdateDTO.builder().quantity(30).build();

        var resourceURL = "/books/" + -1 + "/inventory/stock";
        mockMvc.perform(patch(resourceURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookStockUpdateDTO))
        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    private Book createBook() {
        var book = Book.builder().name("Designing Data Intensive Applications").description("Data is at the center of " +
                "many challenges in system design today. Difficult issues need to be figured out, suc..")
                .author("Martin Kleppmann").ISBN("123434234-123434").price(BigDecimal.valueOf(50.0)).build();
        return bookService.saveBook(book);
    }

    private Inventory createInventory(Book book) {
        var inventory = Inventory.builder().quantity(BigInteger.valueOf(40l)).book(book).build();
        return inventoryService.saveInventory(inventory);

    }

}
