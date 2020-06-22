package com.jrmcdonald.customer.entity.api.config;

import com.jrmcdonald.common.ext.spring.core.oauth2.config.JwtValidatorConfiguration;
import com.jrmcdonald.common.ext.spring.datetime.config.DateTimeConfiguration;
import com.jrmcdonald.common.ext.spring.web.exception.handler.config.ExceptionHandlerConfiguration;
import com.jrmcdonald.common.ext.spring.web.interceptor.config.InterceptorConfiguration;
import com.jrmcdonald.common.ext.spring.web.interceptor.config.WebMvcInterceptorConfiguration;
import com.jrmcdonald.common.ext.spring.web.oauth2.jwt.config.JwtDecoderConfiguration;
import com.jrmcdonald.common.ext.spring.web.security.authentication.config.SecurityConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "com.jrmcdonald.customer.entity")
@Import({
        DateTimeConfiguration.class,
        ExceptionHandlerConfiguration.class,
        InterceptorConfiguration.class,
        JwtDecoderConfiguration.class,
        JwtValidatorConfiguration.class,
        SecurityConfiguration.class,
        WebMvcInterceptorConfiguration.class
})
public class ApplicationConfiguration {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
