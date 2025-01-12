package com.finflex.repository;

import com.finflex.entitiy.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findOptionalByTCKNAndStateTrue(String tckn);
    Optional<Customer> findOptionalByYKNAndStateTrue(String yckn);
    Optional<Customer> findOptionalByVKNAndStateTrue(String vkn);
    Optional<Customer> findOptionalByCustomerNumberAndStateTrue(Long customerNumber);
    @Query("SELECT MAX(c.customerNumber) FROM Customer c")
    Long findMaxCustomerNumber();
    Optional<Customer> findOptionalByEmailAndStateTrue(String email);
    Optional<Customer> findOptionalByPhoneNumber(String phoneNumber);
   Optional<Customer>  findOptionalByCustomerIdAndStateTrue(Long id);
}
