package com.jrmcdonald.ext.spring.interceptor.config;

import com.jrmcdonald.ext.spring.config.DateTimeConfiguration;
import com.jrmcdonald.ext.spring.interceptor.ServiceEntryInterceptor;
import com.jrmcdonald.ext.spring.interceptor.config.InterceptorConfiguration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class InterceptorConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DateTimeConfiguration.class, InterceptorConfiguration.class));

    @Test
    @DisplayName("Should create ServiceEntryInterceptor bean")
    void shouldCreateServiceEntryInterceptorBean() {
        runner.run(ctx -> assertThat(ctx.getBean("serviceEntryInterceptor")).isInstanceOf(ServiceEntryInterceptor.class));

    }
}