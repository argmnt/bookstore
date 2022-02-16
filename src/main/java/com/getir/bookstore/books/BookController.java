package com.getir.bookstore.books;

import com.getir.bookstore.inventory.Inventory;
import com.getir.bookstore.inventory.InventoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final InventoryService inventoryService;

    @ApiOperation(value = "createBook", notes = "Creates book then returns id of created book.")
    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody BookDTO bookDTO) {
        String resourceId = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceId);
    }

    @PatchMapping("/{bookId}/inventory/stock")
    public ResponseEntity<Void> updateBookStock(@PathVariable String bookId,
                                                     @RequestBody @Valid BookStockUpdateDTO bookStockUpdateDTO) {
        bookService.updateBookStock(bookId, bookStockUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}/inventory")
    public Inventory getBookInventoryByBookId(@PathVariable String bookId) {
        return inventoryService.findByBookId(bookId);
    }

}
