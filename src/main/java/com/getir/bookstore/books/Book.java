package com.getir.bookstore.books;

import com.getir.bookstore.common.AbstractBaseCollection;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book extends AbstractBaseCollection implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String author;

    private String ISBN;

    @Field(targetType = DECIMAL128)
    private BigDecimal price;
}
