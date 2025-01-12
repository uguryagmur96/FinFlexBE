package com.finflex.service;

import com.finflex.dto.request.CreateTransactionRequest;
import com.finflex.dto.response.MessageResponse;
import com.finflex.dto.response.TransactionResponse;
import com.finflex.entitiy.Account;
import com.finflex.entitiy.Customer;
import com.finflex.entitiy.ExchangeRates;
import com.finflex.entitiy.Transaction;
import com.finflex.exception.AccountException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.ITransactionMapper;
import com.finflex.repository.ITransactionRepository;
import com.finflex.utility.ServiceManager;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService extends ServiceManager<Transaction, Long> {

    private final ITransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final ExchangeRatesService exchangeRatesService;
    private final ITransactionMapper transactionMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(ITransactionRepository transactionRepository,AccountService accountService,
                              CustomerService customerService,ExchangeRatesService exchangeRatesService,
                              ITransactionMapper transactionMapper) {
        super(transactionRepository);
        this.transactionRepository=transactionRepository;
        this.customerService=customerService;
        this.exchangeRatesService=exchangeRatesService;
        this.transactionMapper=transactionMapper;
        this.accountService=accountService;
    }

    public MessageResponse createTransaction(CreateTransactionRequest request){
        Optional<Customer> optCustomer;

         if(!request.getCustomerTckn().isEmpty()){
             optCustomer=customerService.getOptionalCustomerByTckn(request.getCustomerTckn());
         } else if (!request.getCustomerVkn().isEmpty()) {
             optCustomer=customerService.getOptionalCustomerByVkn(request.getCustomerVkn());
         }else if (!request.getCustomerYkn().isEmpty()){
             optCustomer=customerService.getOptionalCustomerByYkn(request.getCustomerYkn());
         } else{
             throw new AccountException(ErrorType.CUSTOMER_NOT_FOUND);
         }
        Account source=accountService.getOptionalAccountByAccountNo(request.getSourceAccountNo());
        Account target=accountService.getOptionalAccountByAccountNo(request.getTargetAccountNo());
        String targetCurrencyPair=target.getCurrencyType().toString()+source.getCurrencyType().toString();
        ExchangeRates targetRates=exchangeRatesService.getExchangeRates(targetCurrencyPair);
        BigDecimal sourceFinalAmount=request.getAmount().multiply(targetRates.getRate());
        BigDecimal fee=sourceFinalAmount.multiply(BigDecimal.valueOf(0.03));
        if(sourceFinalAmount.compareTo(source.getBalance())>=0){
            throw new AccountException(ErrorType.INSUFFICIENT_BALANCE);
        }
        source.setBalance((source.getBalance().subtract(sourceFinalAmount)).subtract(fee));
        target.setBalance(target.getBalance().add(request.getAmount()));
        accountService.save(source);
        accountService.save(target);
        Transaction transaction=Transaction.builder()
                .customer(optCustomer.get())
                .sourceCurrType(source.getCurrencyType())
                .targetCurrType(target.getCurrencyType())
                .transactionCurrRate(targetRates.getRate())
                .sourceAmount(sourceFinalAmount)
                .targetAmount(request.getAmount())
                .transactionFee(fee)
                .userTCKN(request.getUserTCKN())
                .userNo(request.getUserNo())
                .customer(optCustomer.get())
                .account(target)
                .build();
                save(transaction);
                logger.info("Transaction created successfully",+transaction.getTransactionId());
                return new MessageResponse("Transaction is done!");
    }

    public Page<TransactionResponse> getAllTransactions(int page, int pageSize, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Transaction> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate, pageable);
        } else {
            transactions = transactionRepository.findAllByOrderByCreatedDateDesc(pageable);
        }
        return transactions.map(transactionMapper::transactionToTransactionResponse);
    }

    public Page<TransactionResponse> getFilteredTransactions(
            int page,
            int pageSize,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long customerNo,
            Long accountNo,
            Long userNo) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Transaction> transactions = transactionRepository.findTransactionsByFilters(
                startDate, endDate, customerNo, accountNo, userNo, pageable);

        if (transactions.isEmpty()) {
            if (customerNo != null && customerService.getCustomerByCustomerNumber(customerNo) == null) {
                throw new AccountException(ErrorType.CUSTOMER_NOT_FOUND);
            } else if (accountNo != null && accountService.getAccountByAccountNo(accountNo) == null) {
                throw new AccountException(ErrorType.ACCOUNT_NOT_FOUND);
            } else if (userNo != null) {
                try {
                    String userNumber = String.valueOf(userNo);
                    String userServiceUrl = "http://localhost:8080/api/v1/users/getUserByUserNo/" + userNumber;
                    restTemplate.headForHeaders(userServiceUrl);
                } catch (Exception e) {
                    throw new AccountException(ErrorType.USER_NOT_FOUND);
                }
            }
        }

        return transactions.map(transactionMapper::transactionToTransactionResponse);
    }

    public List<TransactionResponse> getCustomerTransactionsByTckn(String tckn) {
        List<Transaction> transactions = transactionRepository.findAllByCustomerTckn(tckn);

        if (transactions.isEmpty()){
            throw new AccountException(ErrorType.CUSTOMER_NOT_FOUND);
        }

        return transactions.stream()
                .map(transactionMapper::transactionToTransactionResponse)
                .toList();
    }
    public Page<TransactionResponse> getCustomerTransactionsByUserTckn(String tckn, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findAllByUserTCKNOrderByCreatedDateDesc(tckn, pageRequest);

        if (transactions.isEmpty()){
            throw new AccountException(ErrorType.CUSTOMER_NOT_FOUND);
        }

        return transactions.map(transactionMapper::transactionToTransactionResponse);
    }

    public Page<TransactionResponse> getCustomerTransactionsByUserNo(Long userNo, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findAllByUserNo(userNo, pageRequest);

        return transactions.map(transactionMapper::transactionToTransactionResponse);
    }

    public Page<TransactionResponse> getCustomerTransactionsByCustomerNo(Long customerNo, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        if(customerService.getCustomerByCustomerNumber(customerNo) == null) {
            throw new AccountException(ErrorType.CUSTOMER_NOT_FOUND);
        }
        Page<Transaction> transactions = transactionRepository.findByCustomerCustomerNumber(customerNo, pageRequest);
        return transactions.map(transactionMapper::transactionToTransactionResponse);
    }

    public Page<TransactionResponse> getCustomerTransactionsByAccountNo(Long accountNo, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        if(accountService.getAccountByAccountNo(accountNo) == null) {
            throw new AccountException(ErrorType.ACCOUNT_NOT_FOUND);
        }
        Page<Transaction> transactions = transactionRepository.findByAccountAccountNo(accountNo, pageRequest);
        return transactions.map(transactionMapper::transactionToTransactionResponse);
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = findById(id).orElseThrow(() -> new AccountException(ErrorType.TRANSACTION_NOT_FOUND));
        return transactionMapper.transactionToTransactionResponse(transaction);
    }

    public void deleteTransactionById(Long id) {
        deleteById(id);
    }

}
