package com.getir.bookstore.customers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CustomerValidator implements Validator {

    private final CustomerService customerService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerDTO customerDTO = (CustomerDTO) target;
        if (customerDTO.getEmail() != null && customerService.existsByEmail(customerDTO.getEmail())) {
            errors.rejectValue("email", "email.already_exists", "Email already exists.");
        }

    }
}
