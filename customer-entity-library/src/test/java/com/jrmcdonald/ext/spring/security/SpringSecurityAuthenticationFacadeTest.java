package com.jrmcdonald.ext.spring.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class SpringSecurityAuthenticationFacadeTest {

    SpringSecurityAuthenticationFacade springSecurityAuthenticationFacade;

    @BeforeEach
    void beforeEach() {
        springSecurityAuthenticationFacade = new SpringSecurityAuthenticationFacade();
    }

    @Test
    @WithMockUser
    @DisplayName("Should return Authentication object")
    void shouldReturnAuthenticationObject() {
        Authentication authentication = springSecurityAuthenticationFacade.getAuthentication();
        assertThat(authentication).isNotNull();
    }

    @Test
    @WithMockUser
    @DisplayName("Should return customerId from Authentication")
    void shouldReturnCustomerIdFromAuthentication() {
        String customerId = springSecurityAuthenticationFacade.getCustomerId();
        assertThat(customerId).isEqualTo("user");
    }

}