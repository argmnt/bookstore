package com.getir.bookstore.exception;

public class NoStockFoundException extends RuntimeException {

    public NoStockFoundException() {
        super("There is not enough stock.");
    }
}
