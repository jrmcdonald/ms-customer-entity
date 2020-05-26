package com.jrmcdonald.customer.entity.api.mapper;

import com.jrmcdonald.customer.entity.api.model.CustomerRequest;
import com.jrmcdonald.customer.entity.db.model.Customer;

import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class CustomerRequestMapper implements BiFunction<String, CustomerRequest, Customer> {

    @Override
    public Customer apply(String customerId, CustomerRequest customerRequest) {
        return Customer.builder()
                       .id(customerId)
                       .firstName(customerRequest.getFirstName())
                       .lastName(customerRequest.getLastName())
                       .build();
    }
}
