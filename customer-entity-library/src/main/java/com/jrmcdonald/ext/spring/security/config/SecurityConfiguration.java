package com.jrmcdonald.ext.spring.security.config;

import com.jrmcdonald.ext.spring.security.AuthenticationFacade;
import com.jrmcdonald.ext.spring.security.SpringSecurityAuthenticationFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

    @Bean
    AuthenticationFacade authenticationFacade() {
        return new SpringSecurityAuthenticationFacade();
    }
}
