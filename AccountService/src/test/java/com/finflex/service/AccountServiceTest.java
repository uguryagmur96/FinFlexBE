package com.finflex.service;

import com.finflex.dto.request.CreateAccountRequest;
import com.finflex.dto.request.UpdateAccountRequest;
import com.finflex.dto.response.AccountResponse;
import com.finflex.entitiy.Account;
import com.finflex.entitiy.Customer;
import com.finflex.entitiy.enums.CurrencyType;
import com.finflex.exception.AccountException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.IAccountMapper;
import com.finflex.repository.IAccountRepository;
import com.finflex.repository.ICustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private IAccountMapper accountMapper;

    @Mock
    private ICustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void depositMoney_accountExists_shouldAddAmount() {

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(500));

        Long accountNo = 999999L;
        BigDecimal depositAmount = BigDecimal.valueOf(200);

        when(accountRepository.findOptionalByAccountNo(accountNo)).thenReturn(Optional.of(account));

        Boolean result = accountService.depositMoney(accountNo, depositAmount);

        assertTrue(result);
        assertEquals(BigDecimal.valueOf(700), account.getBalance());

        verify(accountRepository).save(account);
    }

    @Test
    void depositMoney_accountNotFound_shouldThrowException() {

        Long accountNo = 999998L;
        BigDecimal depositAmount = BigDecimal.valueOf(100);

        when(accountRepository.findOptionalByAccountNo(accountNo)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.depositMoney(accountNo, depositAmount);
        });
        assertEquals(ErrorType.ACCOUNT_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void withdrawMoney_accountExists_shouldSubtractAmount() {

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(500));

        Long accountNo = 999997L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(250);

        when(accountRepository.findOptionalByAccountNo(accountNo)).thenReturn(Optional.of(account));

        Boolean result = accountService.withdrawMoney(accountNo, withdrawAmount);

        assertTrue(result);
        assertEquals(BigDecimal.valueOf(250), account.getBalance());

        verify(accountRepository).save(account);
    }

    @Test
    void withdrawMoney_insufficientFunds_shouldReturnFalse() {

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(1000));

        Long accountNo = 999996L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(1500);

        when(accountRepository.findOptionalByAccountNo(accountNo)).thenReturn(Optional.of(account));

        Boolean result = accountService.withdrawMoney(accountNo, withdrawAmount);

        assertFalse(result);
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void withdrawMoney_accountNotFound_shouldThrowException() {

        Long accountNo = 999995L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(500);

        when(accountRepository.findOptionalByAccountNo(accountNo)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.withdrawMoney(accountNo, withdrawAmount);
        });

        assertEquals(ErrorType.ACCOUNT_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void getAllAccounts_shouldReturnListOfActiveAccounts() {

        Account account1 = new Account();
        account1.setState(true);

        Account account2 = new Account();
        account2.setState(false);

        Account account3 = new Account();
        account3.setState(true);

        when(accountRepository.findAll()).thenReturn(List.of(account1, account2, account3));
        when(accountMapper.mapAccountToAccountResponse(any(Account.class))).thenReturn(new AccountResponse());

        List<AccountResponse> result = accountService.getAllAccounts();

        assertEquals(2, result.size());

        verify(accountRepository).findAll();
    }

    @Test
    void getAccountById_accountExists_shouldReturnAccountResponse() {

        Long accountId = 999994L;
        Account account = new Account();
        account.setAccountId(accountId);

        AccountResponse response = new AccountResponse();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.mapAccountToAccountResponse(account)).thenReturn(response);

        AccountResponse result = accountService.getAccountById(accountId);

        assertEquals(response, result);

        verify(accountRepository).findById(accountId);
    }

    @Test
    void getAccountById_accountNotFound_shouldThrowException() {

        Long accountId = 999993L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.getAccountById(accountId);
        });

        assertEquals(ErrorType.ACCOUNT_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void getAccountByAccountNo_accountExists_shouldReturnAccountResponse() {

        Long accountNo = 999992L;
        Account account = new Account();
        account.setAccountNo(accountNo);

        AccountResponse response = new AccountResponse();

        when(accountRepository.findOptionalByAccountNo(accountNo)).thenReturn(Optional.of(account));
        when(accountMapper.mapAccountToAccountResponse(account)).thenReturn(response);

        AccountResponse result = accountService.getAccountByAccountNo(accountNo);

        assertEquals(response, result);

        verify(accountRepository).findOptionalByAccountNo(accountNo);
    }

    @Test
    void getAccountByAccountNo_accountNotFound_shouldThrowException() {

        Long accountNo = 999991L;

        when(accountRepository.findOptionalByAccountNo(accountNo)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.getAccountByAccountNo(accountNo);
        });

        assertEquals(ErrorType.ACCOUNT_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void getCustomerAccountsByTckn_customerExists_shouldReturnListOfAccountResponses() {

        String tckn = "12345678901";

        Account account1 = new Account();
        account1.setState(true);

        Account account2 = new Account();
        account2.setState(false);

        when(accountRepository.findAllByCustomerTCKN(tckn)).thenReturn(List.of(account1, account2));
        when(accountMapper.mapAccountToAccountResponse(any(Account.class))).thenReturn(new AccountResponse());

        List<AccountResponse> result = accountService.getCustomerAccountsByTckn(tckn);

        assertEquals(1, result.size());

        verify(accountRepository).findAllByCustomerTCKN(tckn);
    }

    @Test
    void getCustomerAccountsByTckn_customerNotFound_shouldThrowException() {

        String tckn = "12345678901";

        when(accountRepository.findAllByCustomerTCKN(tckn)).thenReturn(List.of());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.getCustomerAccountsByTckn(tckn);
        });

        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void getCustomerAccountsByCustomerNumber_customerExists_shouldReturnListOfAccountResponses() {

        Long customerNumber = 100000L;
        Account account1 = new Account();
        account1.setState(true);
        Account account2 = new Account();
        account2.setState(false);

        when(accountRepository.findAllByCustomerNumber(customerNumber)).thenReturn(List.of(account1, account2));
        when(accountMapper.mapAccountToAccountResponse(any(Account.class))).thenReturn(new AccountResponse());

        List<AccountResponse> result = accountService.getCustomerAccountsByCustomerNumber(customerNumber);

        assertEquals(1, result.size());

        verify(accountRepository).findAllByCustomerNumber(customerNumber);
    }

    @Test
    void getCustomerAccountsByCustomerNumber_customerNotFound_shouldThrowException() {

        Long customerNumber = 100001L;

        when(accountRepository.findAllByCustomerNumber(customerNumber)).thenReturn(List.of());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.getCustomerAccountsByCustomerNumber(customerNumber);
        });

        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void createAccount_shouldReturnCreatedAccountResponse() {

        CreateAccountRequest request = new CreateAccountRequest();
        request.setCurrencyType(CurrencyType.USD);
        request.setBalance(BigDecimal.valueOf(1000));
        request.setCustomerNumber(100002L);

        Account account = new Account();
        account.setAccountId(123L);
        account.setAccountNo(999990L);
        account.setCurrencyType(CurrencyType.TRY);

        Customer customer = new Customer();
        customer.setCustomerNumber(123L);
        customer.setAccounts(new ArrayList<>());

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(100002L)).thenReturn(Optional.of(customer));
        when(accountMapper.mapCreateAccountRequestToAccount(request)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.mapAccountToAccountResponse(account)).thenReturn(new AccountResponse());

        AccountResponse result = accountService.createAccount(request);

        assertNotNull(result);
    }

    @Test
    void createAccount_customerNotFound_shouldThrowException() {

        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerNumber(100003L);

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(100003L)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.createAccount(request);
        });

        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void updateAccount_accountExists_shouldReturnUpdatedAccountResponse() {

        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setBalance(BigDecimal.valueOf(2000));

        Account existingAccount = new Account();
        existingAccount.setAccountId(123L);
        existingAccount.setBalance(BigDecimal.valueOf(1000));

        Account updatedAccount = new Account();
        updatedAccount.setAccountId(123L);
        updatedAccount.setBalance(BigDecimal.valueOf(2000));

        when(accountRepository.findById(123L)).thenReturn(Optional.of(existingAccount));
        when(accountMapper.updateAccountFromUpdateAccountRequest(existingAccount, request)).thenReturn(updatedAccount);
        when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);
        when(accountMapper.mapAccountToAccountResponse(updatedAccount)).thenReturn(new AccountResponse());

        AccountResponse result = accountService.updateAccount(updatedAccount.getAccountId(),request);

        assertNotNull(result);
        verify(accountRepository).save(updatedAccount);
    }

    @Test
    void updateAccount_accountNotFound_shouldThrowException() {

        UpdateAccountRequest request = new UpdateAccountRequest();

        when(accountRepository.findById(123L)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.updateAccount(123L, request);
        });

        assertEquals(ErrorType.ACCOUNT_NOT_FOUND, exception.getErrorType());
    }
}
