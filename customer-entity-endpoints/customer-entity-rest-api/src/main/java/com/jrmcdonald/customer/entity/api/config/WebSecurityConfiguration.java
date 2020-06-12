package com.jrmcdonald.customer.entity.api.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
            .antMatchers(HttpMethod.GET, "/v1/customer/**").hasAuthority("SCOPE_read:customer")
            .antMatchers(HttpMethod.POST, "/v1/customer/**").hasAuthority("SCOPE_create:customer")
            .antMatchers("/**").denyAll()
            .and()
            .csrf().disable()
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
