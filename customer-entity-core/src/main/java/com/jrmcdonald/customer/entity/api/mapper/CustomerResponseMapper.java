package com.jrmcdonald.customer.entity.api.mapper;

import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.db.model.Customer;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CustomerResponseMapper implements Function<Customer, CustomerResponse> {

    @Override
    public CustomerResponse apply(Customer customer) {
        return CustomerResponse.builder()
                               .id(customer.getId())
                               .firstName(customer.getFirstName())
                               .lastName(customer.getLastName())
                               .createdAt(customer.getCreatedAt())
                               .build();
    }
}
