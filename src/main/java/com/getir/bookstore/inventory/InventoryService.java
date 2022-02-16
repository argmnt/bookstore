package com.getir.bookstore.inventory;

import com.getir.bookstore.books.Book;
import com.getir.bookstore.books.BookStockUpdateDTO;
import com.mongodb.client.result.UpdateResult;

import java.math.BigInteger;

public interface InventoryService {

    boolean hasEnoughStock(String bookId, BigInteger quantityDemand);

    Inventory saveInventory(Inventory inventory);

    Inventory findByBookId(String bookId);

    BigInteger subtractDemandQuantity(Inventory inventory, BigInteger quantityDemand);

    BigInteger calculateStockDifference(BigInteger currentStockQuantity, BigInteger demandQuantity);

    UpdateResult updateInventoryQuantityByBookId(String bookId, BookStockUpdateDTO bookStockUpdateDTO);

    Inventory createInventory(Book book, BigInteger quantity);
}
