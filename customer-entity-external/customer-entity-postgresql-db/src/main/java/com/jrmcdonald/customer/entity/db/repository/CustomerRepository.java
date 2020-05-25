package com.jrmcdonald.customer.entity.db.repository;

import com.jrmcdonald.customer.entity.db.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

}
