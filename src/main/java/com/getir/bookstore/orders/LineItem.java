package com.getir.bookstore.orders;

import com.getir.bookstore.books.Book;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LineItem implements Serializable {

    private Book book;

    private long quantity;

}
