package com.getir.bookstore.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO implements Serializable {

    @NotNull(message = "LineItem cannot be null.")
    private LineItemDTO lineItem;

}
