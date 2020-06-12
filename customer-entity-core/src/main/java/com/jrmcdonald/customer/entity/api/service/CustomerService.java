package com.jrmcdonald.customer.entity.api.service;

import com.jrmcdonald.customer.entity.api.mapper.CustomerRequestMapper;
import com.jrmcdonald.customer.entity.api.mapper.CustomerResponseMapper;
import com.jrmcdonald.customer.entity.api.model.CustomerRequest;
import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.db.service.CustomerPersistenceService;
import com.jrmcdonald.ext.spring.exception.ConflictException;
import com.jrmcdonald.ext.spring.exception.NotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerPersistenceService customerPersistenceService;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;

    public CustomerResponse getCustomer(String customerId) {
        return customerPersistenceService.findById(customerId)
                                         .map(customerResponseMapper)
                                         .orElseThrow(NotFoundException::new);
    }

    public CustomerResponse createCustomer(String customerId, CustomerRequest customerRequest) {
        if (customerPersistenceService.findById(customerId).isPresent()) {
            throw new ConflictException();
        }

        return customerResponseMapper.apply(customerPersistenceService.create(customerRequestMapper.apply(customerId, customerRequest)));
    }
}
