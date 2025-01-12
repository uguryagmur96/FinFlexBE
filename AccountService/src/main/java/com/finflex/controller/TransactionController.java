package com.finflex.controller;

import com.finflex.dto.request.CreateTransactionRequest;
import com.finflex.dto.response.MessageResponse;
import com.finflex.dto.response.TransactionResponse;
import com.finflex.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.finflex.constants.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequestMapping(TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(GET_ALL_TRANSACTIONS)
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam(required = false) @DateTimeFormat LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat LocalDateTime endDate) {

        Page<TransactionResponse> transactions = transactionService.getAllTransactions(page, pageSize, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(GET_FILTERED_TRANSACTIONS)
    public ResponseEntity<Page<TransactionResponse>> getFilteredTransactions(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam(required = false) @DateTimeFormat LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat LocalDateTime endDate,
            @RequestParam(required = false) Long customerNo,
            @RequestParam(required = false) Long accountNo,
            @RequestParam(required = false) Long userNo) {

        Page<TransactionResponse> transactions = transactionService.getFilteredTransactions(
                page, pageSize, startDate, endDate, customerNo, accountNo, userNo);

        return ResponseEntity.ok(transactions);
    }


    @GetMapping( GET_TRANSACTION_BY_ID + "/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @GetMapping( GET_CUSTOMER_TRANSACTIONS_BY_TCKN + "/{customerTckn}")
    public ResponseEntity<List<TransactionResponse>> getCustomerTransactionsByTckn(@PathVariable String customerTckn) {
        return ResponseEntity.ok(transactionService.getCustomerTransactionsByTckn(customerTckn));
    }
    @GetMapping( GET_CUSTOMER_TRANSACTIONS_BY_USER_TCKN + "/{userTckn}")
    public ResponseEntity<Page<TransactionResponse>> getCustomerTransactionsByUserTckn(@PathVariable String userTckn,
                                                                                       @RequestParam int page,
                                                                                       @RequestParam int pageSize) {
        Page<TransactionResponse> transactions = transactionService.getCustomerTransactionsByUserTckn(userTckn, page, pageSize);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping( GET_CUSTOMER_TRANSACTIONS_BY_USER_NO + "/{userNo}")
    public ResponseEntity<Page<TransactionResponse>> getCustomerTransactionsByUserNo(@PathVariable Long userNo,
                                                                                       @RequestParam int page,
                                                                                       @RequestParam int pageSize) {
        Page<TransactionResponse> transactions = transactionService.getCustomerTransactionsByUserNo(userNo, page, pageSize);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(GET_CUSTOMER_TRANSACTIONS_BY_CUSTOMER_NO + "/{customerNo}")
    public ResponseEntity<Page<TransactionResponse>> getCustomerTransactionsByCustomerNo(@PathVariable Long customerNo,
                                                                                       @RequestParam int page,
                                                                                       @RequestParam int pageSize) {
        Page<TransactionResponse> transactions = transactionService.getCustomerTransactionsByCustomerNo(customerNo, page, pageSize);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(GET_CUSTOMER_TRANSACTIONS_BY_ACCOUNT_NO + "/{accountNo}")
    public ResponseEntity<Page<TransactionResponse>> getCustomerTransactionsByAccountNo(@PathVariable Long accountNo,
                                                                                       @RequestParam int page,
                                                                                       @RequestParam int pageSize) {
        Page<TransactionResponse> transactions = transactionService.getCustomerTransactionsByAccountNo(accountNo, page, pageSize);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping(CREATE_TRANSACTION)
    public ResponseEntity<MessageResponse> createTransaction(@RequestBody CreateTransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }
    @DeleteMapping(DELETE_TRANSACTION_BY_ID + "/{transactionId}")
    public void deleteTransactionById(@PathVariable Long transactionId) {
        transactionService.deleteTransactionById(transactionId);
    }

}
