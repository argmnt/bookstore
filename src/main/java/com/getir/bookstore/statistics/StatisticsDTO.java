package com.getir.bookstore.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsDTO {

    private String month;

    private long orderCount;

    private long totalQuantity;

    private long totalPrice;
}
