package com.jrmcdonald.ext.containers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostgresqlContainerFactory {

    private static PostgreSQLContainer INSTANCE;

    public static PostgreSQLContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = buildPostgresqlContainer();
        }
        return INSTANCE;
    }

    private static PostgreSQLContainer buildPostgresqlContainer() {
        return new PostgreSQLContainer().withDatabaseName("customer-entity")
                                        .withUsername("customer-entity");
    }

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + INSTANCE.getJdbcUrl(),
                    "spring.datasource.username=" + INSTANCE.getUsername(),
                    "spring.datasource.password=" + INSTANCE.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
