package com.getir.bookstore.books;

import com.getir.bookstore.exception.ResourceNotFoundException;
import com.getir.bookstore.inventory.InventoryService;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookServiceUnitTest {

    @Test
    public void updateBookStock_IfInventoryNotFundExpectResourceNotFoundExc(){
        assertThrows(ResourceNotFoundException.class, () -> {
            //Given:
            InventoryService inventoryService = Mockito.mock(InventoryService.class);
            BookRepository bookRepository = Mockito.mock(BookRepository.class);
            UpdateResult updateResult = Mockito.mock(UpdateResult.class);
            //When:
            when(updateResult.getModifiedCount()).thenReturn(0l);
            when(inventoryService.updateInventoryQuantityByBookId(any(), any())).thenReturn(updateResult);
            BookService bookService = new BookServiceImpl(bookRepository, inventoryService);
            bookService.updateBookStock("12k34jdlfk", new BookStockUpdateDTO());
        });

    }
}
