package com.getir.bookstore.inventory;

import com.getir.bookstore.books.Book;
import com.getir.bookstore.books.BookStockUpdateDTO;
import com.getir.bookstore.exception.ResourceNotFoundException;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final MongoOperations mongoOperations;

    @Override
    public boolean hasEnoughStock(String bookId, BigInteger quantityDemand) {
        Inventory inventory = findByBookId(bookId);
        var remainingQuantity = subtractDemandQuantity(inventory, quantityDemand);
        return remainingQuantity.signum() > -1;
    }

    @Override
    public Inventory createInventory(Book book, BigInteger quantity) {
        var inventory = Inventory.builder().book(book).quantity(quantity).build();
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory findByBookId(String bookId) {
        return inventoryRepository.findByBookId(bookId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public BigInteger subtractDemandQuantity(Inventory inventory, BigInteger quantityDemand) {
        return inventory.getQuantity().subtract(quantityDemand);
    }

    @Override
    public BigInteger calculateStockDifference(BigInteger currentStockQuantity, BigInteger demandQuantity) {
        return currentStockQuantity.subtract(demandQuantity);
    }

    @Override
    public UpdateResult updateInventoryQuantityByBookId(String bookId, BookStockUpdateDTO bookStockUpdateDTO) {
        var quantity = BigInteger.valueOf(bookStockUpdateDTO.getQuantity());
        return mongoOperations.updateFirst(
                Query.query(Criteria.
                        where("book.id").is(bookId)
                ),
                Update.update("quantity", quantity),
                Inventory.class);
    }
}
