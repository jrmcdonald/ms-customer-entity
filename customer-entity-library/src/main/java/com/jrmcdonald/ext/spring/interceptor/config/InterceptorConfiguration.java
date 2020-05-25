package com.jrmcdonald.ext.spring.interceptor.config;

import com.jrmcdonald.ext.spring.interceptor.ServiceEntryInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class InterceptorConfiguration {

    @Bean
    public ServiceEntryInterceptor serviceEntryInterceptor(Clock clock) { return new ServiceEntryInterceptor(clock); }

}