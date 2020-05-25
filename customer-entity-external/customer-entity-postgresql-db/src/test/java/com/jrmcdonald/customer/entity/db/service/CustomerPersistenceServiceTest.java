package com.jrmcdonald.customer.entity.db.service;

import com.jrmcdonald.customer.entity.db.model.Customer;
import com.jrmcdonald.customer.entity.db.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerPersistenceServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    Clock clock;

    CustomerPersistenceService customerPersistenceService;

    @BeforeEach
    void beforeEach() {
        customerPersistenceService = new CustomerPersistenceService(customerRepository, clock);
    }

    @Test
    @DisplayName("Should find customer by id")
    void shouldFindCustomerById() {
        Customer expectedCustomer = Customer.builder()
                                            .id("customer-id-123")
                                            .firstName("first")
                                            .lastName("last")
                                            .createdAt(Instant.now())
                                            .build();

        when(customerRepository.findById(eq("customer-id-123"))).thenReturn(Optional.of(expectedCustomer));

        Optional<Customer> actualCustomer = customerPersistenceService.findById("customer-id-123");

        assertThat(actualCustomer).isPresent().contains(expectedCustomer);
    }

    @Test
    @DisplayName("Should save customer")
    void shouldSaveCustomer() {
        Instant createdAt = Instant.now();
        Customer expectedCustomer = Customer.builder()
                                            .id("customer-id-123")
                                            .firstName("first")
                                            .lastName("last")
                                            .createdAt(createdAt)
                                            .build();

        when(clock.instant()).thenReturn(createdAt);
        when(customerRepository.saveAndFlush(eq(expectedCustomer))).thenReturn(expectedCustomer);

        Customer actualCustomer = customerPersistenceService.create(expectedCustomer);

        assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }
}