package com.getir.bookstore.books;

import com.getir.bookstore.exception.ResourceNotFoundException;
import com.getir.bookstore.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final InventoryService inventoryService;

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public String createBook(BookDTO bookDTO) {
        var bookToBeSaved = Book.builder().author(bookDTO.getAuthor()).name(bookDTO.getName())
                .description(bookDTO.getDescription()).ISBN(bookDTO.getISBN())
                .price(bookDTO.getPrice()).build();
        var savedBook = saveBook(bookToBeSaved);
        inventoryService.createInventory(savedBook, BigInteger.ZERO);
        return savedBook.getId();

    }

    @Override
    public Book findById(String id) {
        return bookRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void updateBookStock(String bookId, BookStockUpdateDTO bookStockUpdateDTO) {
        var updateResult = inventoryService.updateInventoryQuantityByBookId(bookId, bookStockUpdateDTO);
        if (updateResult.getModifiedCount() != 1l) {
            throw new ResourceNotFoundException();
        }
    }
}
