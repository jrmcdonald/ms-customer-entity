package com.jrmcdonald.ext.spring.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotFoundExceptionTest {

    @Test
    @DisplayName("Should create NotFoundException")
    void shouldCreateNotFoundException() {
        NotFoundException exception = new NotFoundException();
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

}