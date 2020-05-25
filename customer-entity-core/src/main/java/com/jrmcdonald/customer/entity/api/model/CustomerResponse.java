package com.jrmcdonald.customer.entity.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = CustomerResponse.CustomerResponseBuilder.class)
public class CustomerResponse {

    private final @NotNull String id;
    private final @NotNull String firstName;
    private final @NotNull String lastName;
    private final @NotNull Instant createdAt;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CustomerResponseBuilder {

    }
}

