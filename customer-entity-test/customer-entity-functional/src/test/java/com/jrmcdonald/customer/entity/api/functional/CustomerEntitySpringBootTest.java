package com.jrmcdonald.customer.entity.api.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jrmcdonald.customer.entity.api.Application;
import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.db.model.Customer;
import com.jrmcdonald.customer.entity.db.repository.CustomerRepository;
import com.jrmcdonald.ext.containers.PostgresqlContainerFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static com.jrmcdonald.customer.entity.db.model.Customer.builder;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = PostgresqlContainerFactory.Initializer.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("functional-test")
public class CustomerEntitySpringBootTest {

    private static final String CUSTOMER_URI_PATTERN = "/v1/customer/%s";
    private static final String CUSTOMER_ID_VALUE = "customer-id-123";

    private static final GrantedAuthority READ_CUSTOMER_AUTHORITY = new SimpleGrantedAuthority("SCOPE_read:customer");
    private static final GrantedAuthority CREATE_CUSTOMER_AUTHORITY = new SimpleGrantedAuthority("SCOPE_create:customer");

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = PostgresqlContainerFactory.getInstance();

    private static ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void afterEach() {
        customerRepository.deleteAll();
    }

    @DisplayName("GET /v1/customer Tests")
    @Nested
    class GetV1CustomerTests {

        @Test
        @DisplayName("Should get existing customer")
        void shouldGetExistingCustomer() throws Exception {
            Instant createdAt = Instant.now();

            Customer expectedCustomer = builder().id(CUSTOMER_ID_VALUE)
                                                 .firstName("first")
                                                 .lastName("last")
                                                 .createdAt(createdAt)
                                                 .build();

            customerRepository.saveAndFlush(expectedCustomer);

            CustomerResponse expectedResponse = CustomerResponse.builder()
                                                                .id(CUSTOMER_ID_VALUE)
                                                                .firstName("first")
                                                                .lastName("last")
                                                                .createdAt(createdAt)
                                                                .build();

            MvcResult mvcResult = mockMvc.perform(get(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE))
                                                          .contentType(APPLICATION_JSON)
                                                          .with(jwt().authorities(READ_CUSTOMER_AUTHORITY)))
                                         .andExpect(status().isOk())
                                         .andReturn();

            String responseString = mvcResult.getResponse().getContentAsString();
            CustomerResponse actualCustomerResponse = objectMapper.readValue(responseString, CustomerResponse.class);

            assertThat(actualCustomerResponse).isEqualTo(expectedResponse);
        }

        @Test
        @DisplayName("Should return 404 Not Found for non-existing customer")
        void shouldReturn404NotFoundForNonExistingCustomer() throws Exception {
            mockMvc.perform(get(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE))
                                    .contentType(APPLICATION_JSON)
                                    .with(jwt().authorities(READ_CUSTOMER_AUTHORITY)))
                   .andExpect(status().isNotFound());
        }
    }

    @DisplayName("POST /v1/customer Tests")
    @Nested
    class PostV1CustomerTests {

        @Test
        @DisplayName("Should register new customer")
        void shouldRegisterNewCustomer() throws Exception {
            CustomerResponse expectedResponse = CustomerResponse.builder()
                                                                .id(CUSTOMER_ID_VALUE)
                                                                .firstName("first")
                                                                .lastName("last")
                                                                .build();

            Customer expectedCustomer = builder().firstName("first")
                                                 .lastName("last")
                                                 .build();

            MvcResult mvcResult = mockMvc.perform(post(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE))
                                                          .contentType(APPLICATION_JSON)
                                                          .content(objectMapper.writeValueAsString(expectedCustomer))
                                                          .with(jwt().authorities(CREATE_CUSTOMER_AUTHORITY)))
                                         .andExpect(status().isCreated())
                                         .andReturn();

            String responseString = mvcResult.getResponse().getContentAsString();
            CustomerResponse actualCustomerResponse = objectMapper.readValue(responseString, CustomerResponse.class);

            assertThat(actualCustomerResponse).isEqualToIgnoringGivenFields(expectedResponse, "createdAt");
        }

        @Test
        @DisplayName("Should return 409 Conflict for existing customer")
        void shouldReturn409ConflictForExistingCustomer() throws Exception {
            Customer existingCustomer = builder().id(CUSTOMER_ID_VALUE)
                                                 .firstName("last")
                                                 .lastName("first")
                                                 .createdAt(Instant.now())
                                                 .build();

            customerRepository.saveAndFlush(existingCustomer);

            Customer newCustomer = builder().firstName("first")
                                            .lastName("last")
                                            .build();

            mockMvc.perform(post(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE))
                                    .contentType(APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(newCustomer))
                                    .with(jwt().authorities(CREATE_CUSTOMER_AUTHORITY)))
                   .andExpect(status().isConflict());
        }
    }
}

