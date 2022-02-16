package com.getir.bookstore.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {


    private final StatisticsService statisticsService;

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<StatisticsDTO>> getCustomersStatistics(@PathVariable String customerId) {
        return ResponseEntity.ok(statisticsService.getCustomerStatistics(customerId));
    }
}
