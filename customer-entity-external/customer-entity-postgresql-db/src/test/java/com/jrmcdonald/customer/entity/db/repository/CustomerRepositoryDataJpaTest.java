package com.jrmcdonald.customer.entity.db.repository;

import com.jrmcdonald.common.test.container.factory.PostgresqlContainerFactory;
import com.jrmcdonald.customer.entity.db.config.DatabaseConfiguration;
import com.jrmcdonald.customer.entity.db.model.Customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = DatabaseConfiguration.class, initializers = PostgresqlContainerFactory.Initializer.class)
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class CustomerRepositoryDataJpaTest {

    @Container
    @SuppressWarnings("rawtypes")
    private static final PostgreSQLContainer postgreSQLContainer = PostgresqlContainerFactory.getInstance();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Should find customer by id")
    void shouldFindCustomerById() {
        Customer expectedCustomer = Customer.builder()
                                            .id("customer-id-123")
                                            .firstName("first")
                                            .lastName("last")
                                            .createdAt(Instant.now())
                                            .build();

        testEntityManager.persistAndFlush(expectedCustomer);

        Optional<Customer> actualCustomer = customerRepository.findById("customer-id-123");

        assertThat(actualCustomer).isPresent().contains(expectedCustomer);
    }

    @Test
    @DisplayName("Should save customer")
    void shouldSaveCustomer() {
        Customer expectedCustomer = Customer.builder()
                                            .id("customer-id-123")
                                            .firstName("first")
                                            .lastName("last")
                                            .createdAt(Instant.now())
                                            .build();

        customerRepository.saveAndFlush(expectedCustomer);

        Customer actualCustomer = testEntityManager.find(Customer.class, "customer-id-123");

        assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }
}