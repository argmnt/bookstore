package com.getir.bookstore.inventory;

import com.getir.bookstore.books.Book;
import com.getir.bookstore.books.BookService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

@SpringBootTest
@AutoConfigureMockMvc
public class InventoryServiceIntegrationTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BookService bookService;

    @Test
    public void invokeHasEnoughStockForNoStock_ExpectFalse() {
        var book = bookService.saveBook(createBook());
        inventoryService.saveInventory(createInventory(book, "30"));
        var hasEnoughStock = inventoryService.hasEnoughStock(book.getId(), new BigInteger("40"));
        Assertions.assertEquals(false, hasEnoughStock);
    }

    @Test
    public void invokeHasEnoughStockForInStock_ExpectTrue() {
        var demandQuantity = "30";
        var inStockQuantity = "30";
        var book = bookService.saveBook(createBook());
        inventoryService.saveInventory(createInventory(book, inStockQuantity));
        var hasEnoughStock = inventoryService.hasEnoughStock(book.getId(), new BigInteger(demandQuantity));
        Assertions.assertEquals(true, hasEnoughStock);
    }

    private Inventory createInventory(Book book, String stockQuantity) {
        return Inventory.builder().book(book)
                .quantity(new BigInteger(stockQuantity)).build();
    }

    private Book createBook() {
        return Book.builder().author("Kafka").name("Donusum")
                .ISBN("1222300-34322").description("Franz Kafka Donusum").build();
    }
}
