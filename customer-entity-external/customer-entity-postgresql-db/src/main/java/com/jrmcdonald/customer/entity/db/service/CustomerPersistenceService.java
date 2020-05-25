package com.jrmcdonald.customer.entity.db.service;

import com.jrmcdonald.customer.entity.db.model.Customer;
import com.jrmcdonald.customer.entity.db.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerPersistenceService {

    private final CustomerRepository customerRepository;
    private final Clock clock;

    public Optional<Customer> findById(String customerId) {
        return customerRepository.findById(customerId);
    }

    public Customer create(Customer customer) {
        enrichCustomerPreCreation(customer);
        return customerRepository.saveAndFlush(customer);
    }

    private void enrichCustomerPreCreation(Customer customer) {
        customer.setCreatedAt(clock.instant());
    }
}
