package com.jrmcdonald.customer.entity.db.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Customer {

    @Id
    private @NotNull String id;

    private @NotNull String firstName;
    private @NotNull String lastName;
    private @NotNull Instant createdAt;
}
