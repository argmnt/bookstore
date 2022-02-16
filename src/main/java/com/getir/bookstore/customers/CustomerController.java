package com.getir.bookstore.customers;

import com.getir.bookstore.common.ErrorUtil;
import com.getir.bookstore.exception.BadRequestException;
import com.getir.bookstore.orders.Order;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final CustomerValidator customerValidator;

    @InitBinder("customerDTO")
    protected void initBinder(final WebDataBinder binder){
        binder.addValidators(customerValidator);
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable String id) {
        return customerService.findCustomerById(id);
    }

    @ApiOperation(value = "createCustomer", notes = "Creates customer then returns id of created customer.")
    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerDTO customerDTO,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorUtil
                    .toStringObjectErrorsDefaultMessages(bindingResult.getAllErrors()));
        }
        var resourceId = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceId);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<Page<Order>> getOrdersByCustomerId(@PathVariable String id,
                                                             @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(customerService.findOrdersCustomerById(id, page, size));
    }

    @GetMapping
    public ResponseEntity<Page<Customer>> getCustomers(@RequestParam int page,
                                                       @RequestParam int size) {
        return ResponseEntity.ok(customerService.getCustomers(page, size));
    }
}
