package com.getir.bookstore.books;

public interface BookService {

    Book saveBook(Book book);

    String createBook(BookDTO bookDTO);

    Book findById(String id);

    void updateBookStock(String bookId, BookStockUpdateDTO bookStockUpdateDTO);
}
