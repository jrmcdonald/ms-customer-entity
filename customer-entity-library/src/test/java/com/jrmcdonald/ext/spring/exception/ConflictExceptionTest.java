package com.jrmcdonald.ext.spring.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConflictExceptionTest {

    @Test
    @DisplayName("Should create ConflictException")
    void shouldCreateConflictException() {
        ConflictException exception = new ConflictException();
        assertThat(exception).isInstanceOf(ConflictException.class);
    }
}