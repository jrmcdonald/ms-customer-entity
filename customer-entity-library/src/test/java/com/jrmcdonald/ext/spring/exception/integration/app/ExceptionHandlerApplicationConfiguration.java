package com.jrmcdonald.ext.spring.exception.integration.app;

import com.jrmcdonald.ext.spring.exception.config.ExceptionHandlerConfiguration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import(ExceptionHandlerConfiguration.class)
public class ExceptionHandlerApplicationConfiguration {

}
