package com.getir.bookstore.orders;


import com.getir.bookstore.common.AbstractBaseCollection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
public class Order extends AbstractBaseCollection implements Serializable {

    @Builder
    public Order(LocalDateTime createdAt, LocalDateTime updatedAt, String id, List<LineItem> lineItems, Customer customer, OrderStatus orderStatus) {
        super(createdAt, updatedAt);
        this.id = id;
        this.lineItems = lineItems;
        this.customer = customer;
        this.orderStatus = orderStatus;
    }

    @Id
    private String id;

    private List<LineItem> lineItems;

    private Customer customer;

    private OrderStatus orderStatus;

}
