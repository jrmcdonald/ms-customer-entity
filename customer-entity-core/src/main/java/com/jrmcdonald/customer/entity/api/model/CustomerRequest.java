package com.jrmcdonald.customer.entity.api.model;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class CustomerRequest {

    private final @NotNull String id;
    private final @NotNull String firstName;
    private final @NotNull String lastName;
}
