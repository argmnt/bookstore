package com.getir.bookstore.books;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO implements Serializable {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String author;

    @NotEmpty
    private String ISBN;

    @Positive
    private BigDecimal price;
}
