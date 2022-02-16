package com.getir.bookstore.orders;

import com.getir.bookstore.common.ErrorUtil;
import com.getir.bookstore.customers.CustomerService;
import com.getir.bookstore.exception.BadRequestException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final CustomerService customerService;

    private final OrderValidator orderValidator;

    @InitBinder("orderDTO")
    protected void initBinder(final WebDataBinder binder){
        binder.addValidators(orderValidator);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable("id") String id) {
        return orderService.findById(id);
    }

    @GetMapping
    public Page<Order> getOrders(@RequestBody OrderCriteriaFilterDTO orderCriteriaFilterDTO,
                                 @RequestParam int page, @RequestParam int size) {
        return orderService.findOrdersBetweenDates(orderCriteriaFilterDTO, page, size);
    }

    @ApiOperation(value = "createOrder", notes = "Creates order then returns id of created order.")
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult,
                                              Authentication authentication) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorUtil.toStringObjectErrorsDefaultMessages(bindingResult.getAllErrors()));
        }
        var customer = customerService.findByAuthentication(authentication);
        return ResponseEntity.ok(orderService.createOrder(orderDTO, customer));
    }

}
