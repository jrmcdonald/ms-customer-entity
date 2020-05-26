package com.jrmcdonald.ext.spring.exception.integration.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.jrmcdonald.ext.spring.exception.integration.app", exclude = SecurityAutoConfiguration.class)
public class ExceptionHandlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExceptionHandlerApplication.class, args);
    }

}
