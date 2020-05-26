package com.jrmcdonald.customer.entity.api.mapper;

import com.jrmcdonald.customer.entity.db.model.Customer;
import com.jrmcdonald.customer.entity.api.model.CustomerRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRequestMapperTest {

    private CustomerRequestMapper customerRequestMapper;

    @BeforeEach
    void beforeEach() {
        customerRequestMapper = new CustomerRequestMapper();
    }

    @Test
    @DisplayName("Should map customer id and CustomerRequest to Customer")
    void shouldMapCustomerIdAndCustomerRequestToCustomer() {
        CustomerRequest customerRequest = CustomerRequest.builder()
                                                         .firstName("first")
                                                         .lastName("last")
                                                         .build();

        Customer actualCustomer = customerRequestMapper.apply("customer-id-123", customerRequest);

        assertThat(actualCustomer.getId()).isEqualTo("customer-id-123");
        assertThat(actualCustomer.getFirstName()).isEqualTo("first");
        assertThat(actualCustomer.getLastName()).isEqualTo("last");
        assertThat(actualCustomer.getCreatedAt()).isNull();
    }

}