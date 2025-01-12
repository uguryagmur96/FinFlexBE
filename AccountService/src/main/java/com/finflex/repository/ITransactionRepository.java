package com.finflex.repository;

import com.finflex.entitiy.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.customer.TCKN = :tckn")
    List<Transaction> findAllByCustomerTckn(@Param("tckn") String tckn);
    Page<Transaction> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<Transaction> findAllByUserTCKN(String tckn, Pageable pageable);
    Page<Transaction> findAllByUserTCKNOrderByCreatedDateDesc(String tckn, Pageable pageable);
    Page<Transaction> findAllByUserNo(Long userNo, Pageable pageable);
    Page<Transaction> findByCustomerCustomerNumber(Long customerNo, Pageable pageable);
    Page<Transaction> findByAccountAccountNo(Long accountNo, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.createdDate BETWEEN :startDate AND :endDate")
    Page<Transaction> findByTransactionDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:startDate IS NULL OR :endDate IS NULL OR t.createdDate BETWEEN :startDate AND :endDate) AND " +
            "(:customerNo IS NULL OR t.customer.customerNumber = :customerNo) AND " +
            "(:accountNo IS NULL OR t.account.accountNo = :accountNo) AND " +
            "(:userNo IS NULL OR t.userNo = :userNo) " +
            "ORDER BY t.createdDate DESC")
    Page<Transaction> findTransactionsByFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("customerNo") Long customerNo,
            @Param("accountNo") Long accountNo,
            @Param("userNo") Long userNo,
            Pageable pageable
    );
}
