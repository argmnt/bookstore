package com.getir.bookstore.customers;

import com.getir.bookstore.exception.ResourceNotFoundException;
import com.getir.bookstore.jwt.JWTService;
import com.getir.bookstore.orders.Order;
import com.getir.bookstore.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private final CustomerRepository customerRepository;

    private final OrderService orderService;

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer findCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public String createCustomer(CustomerDTO customerDTO) {
        var customer = Customer.builder().name(customerDTO.getName()).email(customerDTO.getEmail())
                .age(customerDTO.getAge())
                .password(passwordEncoder.encode(customerDTO.getPassword())).build();
        var savedCustomer = saveCustomer(customer);
        return savedCustomer.getId();
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Order> findOrdersCustomerById(String id, int page, int size) {
        return orderService.findByCustomerId(id, page, size);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Customer findByAuthentication(Authentication authentication) {
        //Warning! email is held as username in token!
        String email = jwtService.getClaim(authentication, "username");
        return customerRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Page<Customer> getCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size));
    }
}
