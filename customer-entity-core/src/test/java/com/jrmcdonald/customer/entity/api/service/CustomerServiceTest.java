package com.jrmcdonald.customer.entity.api.service;

import com.jrmcdonald.common.schema.definition.exception.ConflictException;
import com.jrmcdonald.common.schema.definition.exception.NotFoundException;
import com.jrmcdonald.customer.entity.api.mapper.CustomerRequestMapper;
import com.jrmcdonald.customer.entity.api.mapper.CustomerResponseMapper;
import com.jrmcdonald.customer.entity.api.model.CustomerRequest;
import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.db.model.Customer;
import com.jrmcdonald.customer.entity.db.service.CustomerPersistenceService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private static final String CUSTOMER_ID_VALUE = "customer-id-123";
    @Mock
    private CustomerResponseMapper customerResponseMapper;

    @Mock
    private CustomerRequestMapper customerRequestMapper;

    @Mock
    private CustomerPersistenceService customerPersistenceService;

    private CustomerService customerService;

    @BeforeEach
    void beforeEach() {
        customerService = new CustomerService(customerPersistenceService, customerRequestMapper, customerResponseMapper);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(customerResponseMapper, customerRequestMapper, customerPersistenceService);
    }

    @Test
    @DisplayName("Should find the customer")
    void shouldFindTheCustomer() {
        Customer customer = Customer.builder()
                                    .id(CUSTOMER_ID_VALUE)
                                    .firstName("first")
                                    .lastName("last")
                                    .createdAt(Instant.now())
                                    .build();

        CustomerResponse expectedCustomerResponse = CustomerResponse.builder()
                                                                    .id(customer.getId())
                                                                    .firstName(customer.getFirstName())
                                                                    .lastName(customer.getLastName())
                                                                    .createdAt(customer.getCreatedAt())
                                                                    .build();

        when(customerPersistenceService.findById(eq(CUSTOMER_ID_VALUE))).thenReturn(Optional.of(customer));
        when(customerResponseMapper.apply(eq(customer))).thenReturn(expectedCustomerResponse);

        CustomerResponse actualCustomerResponse = customerService.getCustomer(CUSTOMER_ID_VALUE);

        assertThat(actualCustomerResponse).isEqualTo(expectedCustomerResponse);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when the customer does not exist")
    void shouldThrowCustomerNotFoundExceptionWhenTheCustomerDoesNotExist() {
        when(customerPersistenceService.findById(eq(CUSTOMER_ID_VALUE))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getCustomer(CUSTOMER_ID_VALUE));
    }

    @Test
    @DisplayName("Should create a customer profile")
    void wshouldCreateACustomerProfile() {
        CustomerRequest customerRequest = CustomerRequest.builder()
                                                         .firstName("first")
                                                         .lastName("last")
                                                         .build();

        Customer customer = Customer.builder()
                                    .id(CUSTOMER_ID_VALUE)
                                    .firstName(customerRequest.getFirstName())
                                    .lastName(customerRequest.getLastName())
                                    .build();

        Customer savedCustomer = Customer.builder()
                                         .id(CUSTOMER_ID_VALUE)
                                         .firstName(customerRequest.getFirstName())
                                         .lastName(customerRequest.getLastName())
                                         .createdAt(Instant.now())
                                         .build();

        CustomerResponse expectedCustomerResponse = CustomerResponse.builder()
                                                                    .id(savedCustomer.getId())
                                                                    .firstName(savedCustomer.getFirstName())
                                                                    .lastName(savedCustomer.getLastName())
                                                                    .createdAt(savedCustomer.getCreatedAt())
                                                                    .build();

        when(customerPersistenceService.findById(CUSTOMER_ID_VALUE)).thenReturn(Optional.empty());
        when(customerRequestMapper.apply(eq(CUSTOMER_ID_VALUE), eq(customerRequest))).thenReturn(customer);
        when(customerPersistenceService.create(eq(customer))).thenReturn(savedCustomer);
        when(customerResponseMapper.apply(eq(savedCustomer))).thenReturn(expectedCustomerResponse);

        CustomerResponse actualCustomerResponse = customerService.createCustomer(CUSTOMER_ID_VALUE, customerRequest);

        assertThat(actualCustomerResponse).isEqualTo(expectedCustomerResponse);
    }

    @Test
    @DisplayName("Should throw CustomerAlreadyExistsException when the customer already exists")
    void shouldThrowCustomerAlreadyExistsExceptionWhenTheCustomerAlreadyExists() {
        CustomerRequest customerRequest = CustomerRequest.builder()
                                                         .firstName("first")
                                                         .lastName("last")
                                                         .build();

        Customer customer = Customer.builder()
                                    .id(CUSTOMER_ID_VALUE)
                                    .firstName(customerRequest.getFirstName())
                                    .lastName(customerRequest.getLastName())
                                    .build();

        when(customerPersistenceService.findById(CUSTOMER_ID_VALUE)).thenReturn(Optional.of(customer));

        assertThrows(ConflictException.class, () -> customerService.createCustomer(CUSTOMER_ID_VALUE, customerRequest));
    }
}