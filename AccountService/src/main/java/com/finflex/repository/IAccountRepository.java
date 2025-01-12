package com.finflex.repository;

import com.finflex.entitiy.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.customer.TCKN = :tckn")
    List<Account> findAllByCustomerTCKN(@Param("tckn") String tckn);

    @Query("SELECT a FROM Account a WHERE a.customer.customerNumber = :customerNumber")
    List<Account> findAllByCustomerNumber(@Param("customerNumber") Long customerNumber);

    Optional<Account> findOptionalByAccountNo(Long accountNo);

    @Query("SELECT min(a.accountNo) FROM Account a")
    Long findMinAccountNumber();
}
