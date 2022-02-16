package com.getir.bookstore.statistics;

import com.getir.bookstore.orders.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final MongoOperations mongoOperations;

    @Override
    public List<StatisticsDTO> getCustomerStatistics(String customerId) {

        MatchOperation matchOperation = match(new Criteria()
                .andOperator(new Criteria("customer._id").is(customerId)));

        ProjectionOperation dateProjection = project()
                .and("lineItems.book.price").as("price")
                .and("lineItems.quantity").as("quantity")
                .and("createdAt").extractMonth().as("month");


        GroupOperation groupBy = group("month").first("month").as("month")

                .count().as("orderCount")
                .sum("quantity").as("totalQuantity")
                .sum("price").as("totalPrice");

        var aggregation = newAggregation(
                matchOperation,
                unwind("lineItems"),
                dateProjection,
                groupBy);
        AggregationResults<StatisticsDTO> groupResults = mongoOperations.aggregate(aggregation, Order.class, StatisticsDTO.class);
        return groupResults.getMappedResults();
    }
}
