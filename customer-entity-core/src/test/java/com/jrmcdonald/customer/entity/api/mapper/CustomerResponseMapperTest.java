package com.jrmcdonald.customer.entity.api.mapper;

import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.db.model.Customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerResponseMapperTest {

    CustomerResponseMapper customerResponseMapper;

    @BeforeEach
    void beforeEach() {
        customerResponseMapper = new CustomerResponseMapper();
    }

    @Test
    @DisplayName("Should map Customer to CustomerResponse")
    void shouldMapCustomerToCustomerResponse() {
        Customer customer = Customer.builder().id("customer-id-123")
                                    .firstName("first")
                                    .lastName("last")
                                    .createdAt(Instant.now())
                                    .build();

        CustomerResponse customerResponse = customerResponseMapper.apply(customer);

        assertThat(customerResponse.getId()).isEqualTo(customer.getId());
        assertThat(customerResponse.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(customerResponse.getLastName()).isEqualTo(customer.getLastName());
        assertThat(customerResponse.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    }
}