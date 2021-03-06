package com.jrmcdonald.customer.entity.api.controller;

import com.jrmcdonald.customer.entity.api.doc.CustomerApi;
import com.jrmcdonald.customer.entity.api.model.CustomerRequest;
import com.jrmcdonald.customer.entity.api.model.CustomerResponse;
import com.jrmcdonald.customer.entity.api.service.CustomerService;

import io.micrometer.core.annotation.Timed;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer/{id}")
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;

    @Override
    @Timed
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable("id") String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @Override
    @Timed
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> createCustomer(@PathVariable("id") String customerId, @RequestBody CustomerRequest customerRequest) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri())
                             .body(customerService.createCustomer(customerId, customerRequest));
    }
}
