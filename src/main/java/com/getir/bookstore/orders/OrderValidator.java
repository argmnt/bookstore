package com.getir.bookstore.orders;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrderValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return OrderDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderDTO orderDTO = (OrderDTO) target;

        if (orderDTO.getLineItem() != null && orderDTO.getLineItem().getQuantity() < 1) {
            errors.rejectValue("lineItem.quantity", "lineItem.quantity.invalid",
                    "The quantity value should be greater than 1.");
        }

        if (orderDTO.getLineItem() != null &&
                (!StringUtils.hasLength(orderDTO.getLineItem().getBookId()))) {
            errors.rejectValue("lineItem.bookId", "lineItem.bookId.invalid",
                    "The bookId cannot be empty or null.");
        }
    }
}
