package com.finflex.service;

import com.finflex.dto.request.CreateTransactionRequest;
import com.finflex.dto.response.MessageResponse;
import com.finflex.dto.response.TransactionResponse;
import com.finflex.entitiy.Account;
import com.finflex.entitiy.Customer;
import com.finflex.entitiy.ExchangeRates;
import com.finflex.entitiy.Transaction;
import com.finflex.entitiy.enums.CurrencyType;
import com.finflex.exception.AccountException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.ITransactionMapper;
import com.finflex.repository.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private AccountService accountService;

    @Mock
    private ExchangeRatesService exchangeRatesService;

    @Mock
    private ITransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    private Customer customer;
    private Account sourceAccount;
    private Account targetAccount;
    private CreateTransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        sourceAccount = new Account();
        targetAccount = new Account();

        sourceAccount.setCurrencyType(CurrencyType.USD);
        sourceAccount.setBalance(BigDecimal.valueOf(10000));

        targetAccount.setCurrencyType(CurrencyType.TRY);
        targetAccount.setBalance(BigDecimal.valueOf(10000));

        transactionRequest = new CreateTransactionRequest();
        transactionRequest.setCustomerTckn("12345678901");
        transactionRequest.setSourceAccountNo(123456l);
        transactionRequest.setTargetAccountNo(654321l);
        transactionRequest.setAmount(BigDecimal.valueOf(100));
    }

    @Test
    void createTransaction_Successful() {
        when(customerService.getOptionalCustomerByTckn(any())).thenReturn(Optional.of(customer));
        when(accountService.getOptionalAccountByAccountNo(any())).thenReturn(sourceAccount).thenReturn(targetAccount);
        when(exchangeRatesService.getExchangeRates(any())).thenReturn(new ExchangeRates("USDTRY", BigDecimal.valueOf(10)));

        MessageResponse response = transactionService.createTransaction(transactionRequest);

        assertNotNull(response);
        assertEquals("Transaction is done!", response.getMessage());

        verify(accountService, times(2)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_InsufficientBalance() {
        sourceAccount.setBalance(BigDecimal.valueOf(10));
        when(customerService.getOptionalCustomerByTckn(any())).thenReturn(Optional.of(customer));
        when(accountService.getOptionalAccountByAccountNo(any())).thenReturn(sourceAccount).thenReturn(targetAccount);
        when(exchangeRatesService.getExchangeRates(any())).thenReturn(new ExchangeRates("USDTRY", BigDecimal.valueOf(10)));

        AccountException exception = assertThrows(AccountException.class, () ->
                transactionService.createTransaction(transactionRequest));

        assertEquals(ErrorType.INSUFFICIENT_BALANCE, exception.getErrorType());
    }

    @Test
    void getAllTransactions_WithDateRange() {
        Transaction transaction = new Transaction();
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction));

        when(transactionRepository.findByTransactionDateBetween(any(), any(), any(Pageable.class)))
                .thenReturn(transactionPage);
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(new TransactionResponse());

        Page<TransactionResponse> response = transactionService.getAllTransactions(0, 10, LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void getAllTransactions_WithoutDateRange() {
        Transaction transaction = new Transaction();
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction));

        when(transactionRepository.findAllByOrderByCreatedDateDesc(any(Pageable.class)))
                .thenReturn(transactionPage);
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(new TransactionResponse());

        Page<TransactionResponse> response = transactionService.getAllTransactions(0, 10, null, null);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void getFilteredTransactions_WithAllFilters() {
        Transaction transaction = new Transaction();
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction));

        when(transactionRepository.findTransactionsByFilters(any(), any(), anyLong(), anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(transactionPage);
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(new TransactionResponse());

        Page<TransactionResponse> response = transactionService.getFilteredTransactions(0, 10, LocalDateTime.now().minusDays(1), LocalDateTime.now(), 1L, 1L, 1L);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void getFilteredTransactions_NoFiltersApplied() {
        Page<Transaction> transactionPage = new PageImpl<>(List.of());

        when(transactionRepository.findTransactionsByFilters(any(), any(), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(transactionPage);

        Page<TransactionResponse> response = transactionService.getFilteredTransactions(0, 10, null, null, null, null, null);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void getCustomerTransactionsByTckn_ReturnsTransactions() {
        Transaction transaction = new Transaction();

        when(transactionRepository.findAllByCustomerTckn(anyString())).thenReturn(List.of(transaction));
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(new TransactionResponse());

        List<TransactionResponse> response = transactionService.getCustomerTransactionsByTckn("12345678901");

        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    void getCustomerTransactionsByTckn_CustomerNotFound() {
        when(transactionRepository.findAllByCustomerTckn(anyString())).thenReturn(List.of());

        AccountException exception = assertThrows(AccountException.class, () ->
                transactionService.getCustomerTransactionsByTckn("12345678901"));

        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void getTransactionById_TransactionFound() {
        Transaction transaction = new Transaction();

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(new TransactionResponse());

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertNotNull(response);
    }
}