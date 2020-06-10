package com.jrmcdonald.customer.entity.api.model;

import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerRequestTest {

    @Test
    @DisplayName("Should construct a valid bean")
    void shouldConstructAValidBean() {
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(CustomerRequest.class, new Object[]{"customer-id-123", "first", "last"});
    }

}