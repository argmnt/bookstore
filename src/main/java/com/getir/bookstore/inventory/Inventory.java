package com.getir.bookstore.inventory;


import com.getir.bookstore.books.Book;
import com.getir.bookstore.common.AbstractBaseCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;
import org.springframework.data.annotation.Id;


import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory extends AbstractBaseCollection {

    @Id
    private String id;

    private Book book;

    private BigInteger quantity;

    @Version
    private long version;
}
