package com.jrmcdonald.ext.spring.interceptor.config;

import com.jrmcdonald.ext.spring.interceptor.ServiceEntryInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcInterceptorConfiguration implements WebMvcConfigurer {

    private final ServiceEntryInterceptor serviceEntryInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceEntryInterceptor);
    }
}
