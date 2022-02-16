package com.getir.bookstore.common;

import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorUtil {

    private ErrorUtil() {}

    public static String toStringObjectErrorsDefaultMessages(List<ObjectError> objectErrors) {
        return objectErrors.stream()
                .map(error -> error.getDefaultMessage() + " ")
                .collect(Collectors.toList()).toString();
    }
}
