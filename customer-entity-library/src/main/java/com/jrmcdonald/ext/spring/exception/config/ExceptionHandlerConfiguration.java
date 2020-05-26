package com.jrmcdonald.ext.spring.exception.config;

import com.jrmcdonald.ext.spring.exception.handler.ServiceExceptionHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandlerConfiguration {

    @Bean
    public ServiceExceptionHandler customerExceptionHandler() { return new ServiceExceptionHandler(); }
}
