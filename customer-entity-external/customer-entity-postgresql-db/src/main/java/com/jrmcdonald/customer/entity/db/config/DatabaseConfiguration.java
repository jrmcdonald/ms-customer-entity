package com.jrmcdonald.customer.entity.db.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.jrmcdonald.customer.entity.db.model")
@EnableJpaRepositories(basePackages = "com.jrmcdonald.customer.entity.db.repository")
public class DatabaseConfiguration {

}
