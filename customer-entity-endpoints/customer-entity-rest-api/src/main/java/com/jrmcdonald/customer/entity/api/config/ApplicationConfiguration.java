package com.jrmcdonald.customer.entity.api.config;

import com.jrmcdonald.ext.spring.config.DateTimeConfiguration;
import com.jrmcdonald.ext.spring.exception.config.ExceptionHandlerConfiguration;
import com.jrmcdonald.ext.spring.interceptor.config.InterceptorConfiguration;
import com.jrmcdonald.ext.spring.interceptor.config.WebMvcInterceptorConfiguration;
import com.jrmcdonald.ext.spring.security.config.SecurityConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
@ComponentScan(basePackages = "com.jrmcdonald.customer.entity")
@Import({
        DateTimeConfiguration.class,
        ExceptionHandlerConfiguration.class,
        InterceptorConfiguration.class,
        SecurityConfiguration.class,
        WebMvcInterceptorConfiguration.class
})
public class ApplicationConfiguration {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
