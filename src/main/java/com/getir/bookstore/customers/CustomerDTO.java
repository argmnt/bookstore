package com.getir.bookstore.customers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDTO implements Serializable {

    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @NotEmpty(message = "Email cannot be empty.")
    private String email;

    @Positive(message = "Age should be positive number")
    private int age;

    @NotEmpty(message = "password cannot be empty")
    private String password;
}
