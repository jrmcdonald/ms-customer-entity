package com.jrmcdonald.customer.entity.db.model;

import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    @DisplayName("Should construct a valid bean")
    void shouldConstructAValidBean() {
        BeanTester beanTester = new BeanTester();
        beanTester.addTestValues(Instant.class, new Object[]{Instant.now()});
        beanTester.testBean(Customer.class, new Object[]{"customer-id-123", "first", "last", Instant.now()});
    }

    @Test
    @DisplayName("Should validate required fields")
    void shouldValidateRequiredFields() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Customer customer = new Customer();

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations).extracting("propertyPath")
                  .extractingResultOf("toString")
                  .containsExactlyInAnyOrder("id", "firstName", "lastName", "createdAt");

        assertThat(violations).extracting("interpolatedMessage", String.class)
                  .allMatch("must not be null"::equalsIgnoreCase);
    }
}