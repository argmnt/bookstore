package com.getir.bookstore.orders;

import com.getir.bookstore.common.AbstractBaseCollection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class Customer extends AbstractBaseCollection implements Serializable {

    @Builder
    public Customer(LocalDateTime createdAt, LocalDateTime updatedAt, String id, String name, String email, int age) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    private String id;

    private String name;

    private String email;

    private int age;
}
