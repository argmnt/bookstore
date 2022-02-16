package com.getir.bookstore.statistics;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDTO> getCustomerStatistics(String customerId);
}
