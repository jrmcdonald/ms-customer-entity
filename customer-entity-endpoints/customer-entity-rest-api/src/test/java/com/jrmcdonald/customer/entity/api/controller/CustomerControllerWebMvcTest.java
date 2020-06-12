package com.jrmcdonald.customer.entity.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jrmcdonald.customer.entity.api.model.CustomerRequest;
import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.api.service.CustomerService;
import com.jrmcdonald.ext.spring.config.DateTimeConfiguration;
import com.jrmcdonald.ext.spring.interceptor.config.InterceptorConfiguration;
import com.jrmcdonald.ext.spring.security.oauth2.config.JwtDecoderConfiguration;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static java.lang.String.format;
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
@Import({DateTimeConfiguration.class, InterceptorConfiguration.class, JwtDecoderConfiguration.class})
@ActiveProfiles("test")
class CustomerControllerWebMvcTest {

    private static final String CUSTOMER_URI_PATTERN = "/v1/customer/%s";
    private static final String CUSTOMER_ID_VALUE = "customer-id-123";

    private static final GrantedAuthority READ_CUSTOMER_AUTHORITY = new SimpleGrantedAuthority("SCOPE_read:customer");
    private static final GrantedAuthority CREATE_CUSTOMER_AUTHORITY = new SimpleGrantedAuthority("SCOPE_create:customer");

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
            mockMvc.perform(get(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE)))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should reject user without appropriate permission")
        void shouldRejectUserWithoutAppropriatePermission() throws Exception {
            mockMvc.perform(get(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE)).with(jwt()))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return customer profile")
        void shouldReturnCustomerProfile() throws Exception {
            CustomerResponse expectedResponse = CustomerResponse.builder()
                                                                .id(CUSTOMER_ID_VALUE)
                                                                .firstName("first")
                                                                .lastName("last")
                                                                .createdAt(Instant.now())
                                                                .build();

            when(customerService.getCustomer(eq(CUSTOMER_ID_VALUE))).thenReturn(expectedResponse);

            MvcResult mvcResult = mockMvc.perform(get(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE))
                                                          .with(jwt().authorities(READ_CUSTOMER_AUTHORITY)))
                                         .andExpect(status().isOk())
                                         .andReturn();

            String responseString = mvcResult.getResponse().getContentAsString();
            CustomerResponse actualCustomerResponse = objectMapper.readValue(responseString, CustomerResponse.class);

            assertThat(actualCustomerResponse).isEqualTo(expectedResponse);

            verify(customerService).getCustomer(eq(CUSTOMER_ID_VALUE));
        }
    }

    @Nested
    class CreateCustomerTests {

        @Test
        @WithAnonymousUser
        @DisplayName("Should reject anonymous user")
        void shouldRejectAnonymousUser() throws Exception {
            mockMvc.perform(post(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE)))
                   .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should reject user without appropriate permission")
        void shouldRejectUserWithoutAppropriatePermission() throws Exception {
            mockMvc.perform(post(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE)).with(jwt()))
                   .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return created customer profile")
        void shouldReturnCreatedCustomerProfile() throws Exception {
            CustomerResponse expectedResponse = CustomerResponse.builder()
                                                                .id(CUSTOMER_ID_VALUE)
                                                                .firstName("first")
                                                                .lastName("last")
                                                                .createdAt(Instant.now())
                                                                .build();

            CustomerRequest customerRequest = CustomerRequest.builder()
                                                             .firstName("first")
                                                             .lastName("last")
                                                             .build();

            when(customerService.createCustomer(eq(CUSTOMER_ID_VALUE), refEq(customerRequest))).thenReturn(expectedResponse);

            String customerRequestAsJson = objectMapper.writeValueAsString(customerRequest);

            MvcResult mvcResult = mockMvc.perform(post(format(CUSTOMER_URI_PATTERN, CUSTOMER_ID_VALUE))
                                                          .content(customerRequestAsJson)
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .with(jwt().authorities(CREATE_CUSTOMER_AUTHORITY)))
                                         .andExpect(status().isCreated())
                                         .andReturn();

            String responseString = mvcResult.getResponse().getContentAsString();
            CustomerResponse actualCustomerResponse = objectMapper.readValue(responseString, CustomerResponse.class);

            assertThat(actualCustomerResponse).isEqualTo(expectedResponse);

            verify(customerService).createCustomer(eq(CUSTOMER_ID_VALUE), refEq(customerRequest));
        }
    }

}