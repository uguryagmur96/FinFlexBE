package com.finflex.service;

import com.finflex.dto.request.CreateAccountRequest;
import com.finflex.dto.request.UpdateAccountRequest;
import com.finflex.dto.response.AccountResponse;
import com.finflex.entitiy.Account;
import com.finflex.entitiy.BaseEntity;
import com.finflex.entitiy.Customer;
import com.finflex.entitiy.enums.CurrencyType;
import com.finflex.exception.AccountException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.IAccountMapper;
import com.finflex.repository.IAccountRepository;
import com.finflex.repository.ICustomerRepository;
import com.finflex.utility.ServiceManager;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService extends ServiceManager<Account, Long> {

    private final IAccountRepository accountRepository;
    private final IAccountMapper accountMapper;
    private final ICustomerRepository customerRepository;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AccountService.class);

    public AccountService(IAccountRepository accountRepository, IAccountRepository accountRepository1, IAccountMapper accountMapper, ICustomerRepository customerRepository) {
        super(accountRepository);
        this.accountRepository = accountRepository1;
        this.accountMapper = accountMapper;
        this.customerRepository = customerRepository;
    }

    public Boolean depositMoney(Long accountNo, BigDecimal amount){
        Account account = accountRepository.findOptionalByAccountNo(accountNo)
                .orElseThrow(() -> new AccountException(ErrorType.ACCOUNT_NOT_FOUND));
        account.setBalance(account.getBalance().add(amount));
        update(account);
        logger.info("Money deposited successfully with account number: {}", account.getAccountNo());
        return true;
    }

    public Boolean withdrawMoney(Long accountNo, BigDecimal amount){
        Account account = accountRepository.findOptionalByAccountNo(accountNo)
                .orElseThrow(() -> new AccountException(ErrorType.ACCOUNT_NOT_FOUND));
        if (amount.compareTo(account.getBalance())<=0){
            account.setBalance(account.getBalance().subtract(amount));
            update(account);
            logger.info("Money withdrawn successfully with account number: {}", account.getAccountNo());
            return true;
        }
        return false;
    }

    public List<AccountResponse> getAllAccounts() {
        return findAll().stream()
                .filter(account -> account.getState())
                .map(accountMapper::mapAccountToAccountResponse)
                .collect(Collectors.toList());
    }

    public AccountResponse getAccountById(Long id) {
        Account account = findById(id)
                .orElseThrow(() -> new AccountException(ErrorType.ACCOUNT_NOT_FOUND));
        return accountMapper.mapAccountToAccountResponse(account);
    }

    public AccountResponse getAccountByAccountNo(Long accountNo) {
        Account account = accountRepository.findOptionalByAccountNo(accountNo)
                .orElseThrow(() -> new AccountException(ErrorType.ACCOUNT_NOT_FOUND));
        return accountMapper.mapAccountToAccountResponse(account);
    }
    public Account getOptionalAccountByAccountNo(Long accountNo) {
       return  accountRepository.findOptionalByAccountNo(accountNo).
               orElseThrow(()->new AccountException(ErrorType.ACCOUNT_NOT_FOUND));
    }
    public List<AccountResponse> getCustomerAccountsByTckn(String customerTckn) {
        List<Account> accounts = accountRepository.findAllByCustomerTCKN(customerTckn);
        if(accounts.isEmpty()){
            throw new AccountException(ErrorType.CUSTOMER_NOT_FOUND);
        }

        return accounts.stream()
                .filter(account -> account.getState())
                .map(accountMapper::mapAccountToAccountResponse)
                .toList();
    }

    public List<AccountResponse> getCustomerAccountsByCustomerNumber(Long customerNumber) {
        List<Account> accounts = accountRepository.findAllByCustomerNumber(customerNumber);
        if(accounts.isEmpty()){
            throw new AccountException(ErrorType.CUSTOMER_NOT_FOUND);
        }

        return accounts.stream()
                .filter(account -> account.getState())
                .map(accountMapper::mapAccountToAccountResponse)
                .toList();
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        Customer customer = customerRepository.findOptionalByCustomerNumberAndStateTrue(request.getCustomerNumber())
                .orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));

        if(customer.getAccounts().stream().filter(BaseEntity::getState).anyMatch(a -> a.getCurrencyType() == request.getCurrencyType())){
            throw new AccountException(ErrorType.CUSTOMER_ALREADY_HAS_THIS_ACCOUNT);
        }

        Account account = accountMapper.mapCreateAccountRequestToAccount(request);
        account.setAccountNo(generateAccountNumber());
        account.setBalance(request.getBalance());
        account.setCreatedDate(LocalDateTime.now());
        account.setUpdatedDate(LocalDateTime.now());
        account.setCustomer(customer);

        customer.getAccounts().add(account);

        customerRepository.save(customer);

        logger.info("Account created successfully with account number: {}", account.getAccountNo());

        return accountMapper.mapAccountToAccountResponse(account);
    }

    public AccountResponse updateAccount(Long accountId, UpdateAccountRequest updateAccountRequest) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(ErrorType.ACCOUNT_NOT_FOUND));

        Account updatedAccount = accountMapper.updateAccountFromUpdateAccountRequest(existingAccount, updateAccountRequest);
        update(updatedAccount);
        logger.info("Account updated successfully with account number: {}", updatedAccount.getAccountNo());
        return accountMapper.mapAccountToAccountResponse(updatedAccount);
    }

    public String deleteAccountByAccountNumber(Long accountNumber) {
        Account account = accountRepository.findOptionalByAccountNo(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorType.ACCOUNT_NOT_FOUND));

        if(account.getCurrencyType().equals(CurrencyType.TRY)) {
            throw new AccountException(ErrorType.TRY_ACCOUNT_CANNOT_BE_DELETED);
        }

        if(account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new AccountException(ErrorType.ACCOUNT_CANNOT_BE_DELETED);
        }

        account.setState(false);
        save(account);
        logger.info("Account deleted successfully with account number: {}", account.getAccountNo());
        return "Account successfully deleted.";
    }

    public void deleteAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(ErrorType.ACCOUNT_NOT_FOUND));

        if(account.getCurrencyType().equals(CurrencyType.TRY)) {
            throw new AccountException(ErrorType.TRY_ACCOUNT_CANNOT_BE_DELETED);
        }

        if(account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new AccountException(ErrorType.ACCOUNT_CANNOT_BE_DELETED);
        }

        account.setState(false);
        save(account);
        logger.info("Account deleted successfully with id: {}", id);
    }

    public Long generateAccountNumber() {
        Long minAccountNumber = accountRepository.findMinAccountNumber();
        if (minAccountNumber == null) {
            return 999999L;
        }
        return minAccountNumber - 1;
    }

}
