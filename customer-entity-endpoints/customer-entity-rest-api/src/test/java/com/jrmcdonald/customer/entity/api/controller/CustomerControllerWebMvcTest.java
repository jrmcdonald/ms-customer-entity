package com.jrmcdonald.customer.entity.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jrmcdonald.customer.entity.api.model.CustomerRequest;
import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.api.service.CustomerService;
import com.jrmcdonald.ext.spring.config.DateTimeConfiguration;
import com.jrmcdonald.ext.spring.interceptor.config.InterceptorConfiguration;
import com.jrmcdonald.schema.definition.ServiceHeaders;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@Import({DateTimeConfiguration.class, InterceptorConfiguration.class})
@ActiveProfiles("test")
class CustomerControllerWebMvcTest {

    private static final String CUSTOMER_ID = "customer-id-123";

    private static ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(customerService);
    }

    @Nested
    class GetCustomerTests {

        @Test
        @WithAnonymousUser
        @DisplayName("Should reject anonymous user")
        void shouldRejectAnonymousUser() throws Exception {
            mockMvc.perform(get("/v1/customer"))
                   .andExpect(status().isUnauthorized());

        }

        @Test
        @DisplayName("Should return authenticated customer profile")
        void shouldReturnAuthenticatedCustomerProfile() throws Exception {
            CustomerResponse expectedResponse = CustomerResponse.builder()
                                                                .id(CUSTOMER_ID)
                                                                .firstName("first")
                                                                .lastName("last")
                                                                .createdAt(Instant.now())
                                                                .build();

            when(customerService.getCustomer(eq(CUSTOMER_ID))).thenReturn(expectedResponse);

            MvcResult mvcResult = mockMvc.perform(get("/v1/customer")
                                                          .header(ServiceHeaders.CUSTOMER_ID, CUSTOMER_ID)
                                                          .with(jwt()))
                                         .andExpect(status().isOk())
                                         .andReturn();

            String responseString = mvcResult.getResponse().getContentAsString();
            CustomerResponse actualCustomerResponse = objectMapper.readValue(responseString, CustomerResponse.class);

            assertThat(actualCustomerResponse).isEqualTo(expectedResponse);

            verify(customerService).getCustomer(eq(CUSTOMER_ID));
        }
    }

    @Nested
    class CreateCustomerTests {

        @Test
        @WithAnonymousUser
        @DisplayName("Should reject anonymous user")
        void shouldRejectAnonymousUser() throws Exception {
            mockMvc.perform(get("/v1/customer"))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return created customer profile")
        void shouldReturnCreatedCustomerProfile() throws Exception {
            CustomerResponse expectedResponse = CustomerResponse.builder()
                                                                .id(CUSTOMER_ID)
                                                                .firstName("first")
                                                                .lastName("last")
                                                                .createdAt(Instant.now())
                                                                .build();

            CustomerRequest customerRequest = CustomerRequest.builder()
                                                             .id(CUSTOMER_ID)
                                                             .firstName("first")
                                                             .lastName("last")
                                                             .build();

            when(customerService.createCustomer(refEq(customerRequest))).thenReturn(expectedResponse);

            String customerRequestAsJson = objectMapper.writeValueAsString(customerRequest);

            MvcResult mvcResult = mockMvc.perform(post("/v1/customer")
                                                          .content(customerRequestAsJson)
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .with(jwt()))
                                         .andExpect(status().isCreated())
                                         .andReturn();

            String responseString = mvcResult.getResponse().getContentAsString();
            CustomerResponse actualCustomerResponse = objectMapper.readValue(responseString, CustomerResponse.class);

            assertThat(actualCustomerResponse).isEqualTo(expectedResponse);

            verify(customerService).createCustomer(refEq(customerRequest));
        }
    }

}