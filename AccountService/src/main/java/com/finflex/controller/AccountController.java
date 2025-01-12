package com.finflex.controller;

import com.finflex.dto.request.CreateAccountRequest;
import com.finflex.dto.request.UpdateAccountRequest;
import com.finflex.dto.response.AccountResponse;
import com.finflex.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.finflex.constants.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequestMapping(ACCOUNT)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(DEPOSIT_MONEY + "/{accountNo}/{amount}")
    public ResponseEntity<Boolean>depositMoney(@Valid @PathVariable Long accountNo, @PathVariable BigDecimal amount){
        return ResponseEntity.ok(accountService.depositMoney(accountNo,amount));
    }

    @PostMapping(WITHDRAW_MONEY + "/{accountNo}/{amount}")
    public ResponseEntity<Boolean>withdrawMoney(@Valid @PathVariable Long accountNo, @PathVariable BigDecimal amount){
        return ResponseEntity.ok(accountService.withdrawMoney(accountNo, amount));
    }

    @GetMapping(GET_ALL_ACCOUNTS)
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping( GET_ACCOUNT_BY_ID + "/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @GetMapping( GET_ACCOUNT_BY_ACCOUNT_NO + "/{accountNo}")
    public ResponseEntity<AccountResponse> getAccountByAccountNo(@PathVariable Long accountNo) {
        return ResponseEntity.ok(accountService.getAccountByAccountNo(accountNo));
    }

    @GetMapping(GET_CUSTOMER_ACCOUNTS_BY_TCKN + "/{customerTckn}")
    public ResponseEntity<List<AccountResponse>> getCustomerAccountsByTckn(@PathVariable String customerTckn){
        return ResponseEntity.ok(accountService.getCustomerAccountsByTckn(customerTckn));
    }

    @GetMapping(GET_CUSTOMER_ACCOUNTS_BY_CUSTOMER_NUMBER + "/{customerNumber}")
    public ResponseEntity<List<AccountResponse>> getCustomerAccountsByCustomerNumber(@PathVariable Long customerNumber){
        return ResponseEntity.ok(accountService.getCustomerAccountsByCustomerNumber(customerNumber));
    }

    @PostMapping(CREATE_ACCOUNT)
    public ResponseEntity<AccountResponse> createAccount(@RequestBody @Valid CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PutMapping(UPDATE_ACCOUNT + "/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long accountId, @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(accountId, request));
    }

    @DeleteMapping(DELETE_ACCOUNT_BY_ACCOUNT_NO + "/{accountNumber}")
    public ResponseEntity<String> deleteAccountByAccountNumber(@PathVariable Long accountNumber) {
        return ResponseEntity.ok(accountService.deleteAccountByAccountNumber(accountNumber));
    }

    @DeleteMapping(DELETE_ACCOUNT_BY_ID + "/{accountId}")
    public void deleteAccountById(@PathVariable Long accountId) {
        accountService.deleteAccountById(accountId);
    }

}
