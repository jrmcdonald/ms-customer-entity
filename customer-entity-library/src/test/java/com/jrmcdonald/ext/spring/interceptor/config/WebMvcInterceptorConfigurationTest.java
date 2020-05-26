package com.jrmcdonald.ext.spring.interceptor.config;

import com.jrmcdonald.ext.spring.interceptor.ServiceEntryInterceptor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebMvcInterceptorConfigurationTest {

    @Mock
    InterceptorRegistry interceptorRegistry;

    @Mock
    InterceptorRegistration interceptorRegistration;

    @Mock
    ServiceEntryInterceptor serviceEntryInterceptor;

    private WebMvcInterceptorConfiguration webMvcInterceptorConfiguration;

    @BeforeEach
    void beforeEach() {
        webMvcInterceptorConfiguration = new WebMvcInterceptorConfiguration(serviceEntryInterceptor);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(interceptorRegistry);
    }

    @Test
    @DisplayName("Should add ServiceEntryInterceptor to registry")
    void shouldAddServiceEntryInterceptorToRegistry() {
        when(interceptorRegistry.addInterceptor(eq(serviceEntryInterceptor))).thenReturn(interceptorRegistration);

        webMvcInterceptorConfiguration.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry).addInterceptor(eq(serviceEntryInterceptor));
    }

}