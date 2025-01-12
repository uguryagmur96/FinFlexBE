package com.finflex.service;

import com.finflex.dto.request.CreateCustomerRequest;
import com.finflex.dto.response.CustomerResponse;
import com.finflex.entitiy.Account;
import com.finflex.entitiy.Address;
import com.finflex.entitiy.Customer;
import com.finflex.entitiy.enums.CurrencyType;
import com.finflex.entitiy.enums.CustomerType;
import com.finflex.exception.AccountException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.IAccountMapper;
import com.finflex.mapper.ICustomerMapper;
import com.finflex.repository.ICustomerRepository;
import com.finflex.utility.ServiceManager;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService extends ServiceManager<Customer, Long> {

    private final ICustomerRepository customerRepository;
    private final ICustomerMapper customerMapper;
    private final IAccountMapper accountMapper;
    private final AccountService accountService;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AccountService.class);

    public CustomerService(ICustomerRepository customerRepository, ICustomerRepository customerRepository1, ICustomerMapper customerMapper, IAccountMapper accountMapper, AccountService accountService) {
        super(customerRepository);
        this.customerRepository = customerRepository1;
        this.customerMapper = customerMapper;
        this.accountMapper = accountMapper;
        this.accountService = accountService;
    }

    public List<CustomerResponse> getAllCustomers() {
        return findAll().stream()
                .filter(customer -> customer.getState())
                .map(customerMapper::customerToCustomerResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findOptionalByCustomerIdAndStateTrue(id).orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));
        return customerMapper.customerToCustomerResponse(customer);
    }

    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findOptionalByEmailAndStateTrue(email).orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));
        return customerMapper.customerToCustomerResponse(customer);
    }

    public CustomerResponse getCustomerByCustomerNumber(Long customerNumber) {
        Customer customer = customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)
                .orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));
        return customerMapper.customerToCustomerResponse(customer);
    }

    public CustomerResponse getCustomerByTckn(String tckn) {
        Customer customer =  customerRepository.
                findOptionalByTCKNAndStateTrue(tckn).orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));
        return customerMapper.customerToCustomerResponse(customer);
    }
    public Optional<Customer> getOptionalCustomerByTckn(String tckn) {
        Optional<Customer> optCust  =  customerRepository.findOptionalByTCKNAndStateTrue(tckn);
        if (optCust.isPresent()){
            return optCust;
        }else {
            return Optional.empty();
        }
    }
    public Optional<Customer> getOptionalCustomerByYkn(String ykn) {
        Optional<Customer> optCust  =  customerRepository.findOptionalByYKNAndStateTrue(ykn);
        if (optCust.isPresent()){
            return optCust;
        }else {
            return Optional.empty();
        }
    }
    public Optional<Customer> getOptionalCustomerByVkn(String vkn) {
        Optional<Customer> optCust  =  customerRepository.findOptionalByVKNAndStateTrue(vkn);
        if (optCust.isPresent()){
            return optCust;
        }else {
            return Optional.empty();
        }
    }


    public CustomerResponse getCustomerByYkn(String ykn) {
        Customer customer =  customerRepository.
                findOptionalByYKNAndStateTrue(ykn).orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));
        return customerMapper.customerToCustomerResponse(customer);
    }

    public CustomerResponse getCustomerByVkn(String vkn) {
        Customer customer =  customerRepository.
                findOptionalByVKNAndStateTrue(vkn).orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));
        return customerMapper.customerToCustomerResponse(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        validateIdentityFields(request.getVKN(), request.getTCKN(), request.getYKN());
        checkCustomerTypeMismatch(request);
        isCustomerAlreadyExists(request);
        isEmailExists(request.getEmail());
        isPhoneNumberExists(request.getPhoneNumber());

        Customer customer = customerMapper.createCustomerRequestToCustomer(request);

        Long maxCustomerNumber = customerRepository.findMaxCustomerNumber();
        customer.setCustomerNumber((maxCustomerNumber != null ? maxCustomerNumber : 99999L) + 1);


        List<Account> accounts = new ArrayList<>();

        if(request.getAccounts() != null){
            accounts = setCustomerAccounts(request.getAccounts().stream().map(accountMapper::mapCreateAccountRequestToAccount).toList());
        }
        else {
            accounts = setCustomerAccounts(accounts);
        }

        for (Account account1 : accounts){
            account1.setCustomer(customer);
        }

        customer.setAccounts(accounts);
        Customer savedCustomer = save(customer);
        logger.info("Customer created successfully with customer number: {}", customer.getCustomerNumber());

        return customerMapper.customerToCustomerResponse(savedCustomer);
    }

    public CustomerResponse updateCustomer(Long customerNumber, CreateCustomerRequest request) {

        Customer customer = customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)
                .orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));

        if(!Objects.equals(request.getEmail(), customer.getEmail())){
            if(customerRepository.findOptionalByEmailAndStateTrue(request.getEmail()).isPresent()){
                throw new AccountException(ErrorType.EMAIL_ALREADY_EXISTS);
            }
        }

        if(!Objects.equals(request.getPhoneNumber(), customer.getPhoneNumber())){
            if(customerRepository.findOptionalByPhoneNumber(request.getPhoneNumber()).isPresent()){
                throw new AccountException(ErrorType.PHONE_NUMBER_ALREADY_EXISTS);
            }
        }

        customer.setAddress(new Address(request.getFullAddress(), request.getCity(), request.getDistrict(), request.getPostalCode()));
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        update(customer);
        logger.info("Customer updated successfully with customer number: {}", customer.getCustomerNumber());

        return customerMapper.customerToCustomerResponse(customer);
    }

    public void deleteCustomerByCustomerNumber(Long customerNumber) {

        Customer customer = customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)
                .orElseThrow(() -> new AccountException(ErrorType.CUSTOMER_NOT_FOUND));

        customer.setState(false);
        save(customer);
        logger.info("Customer deleted successfully with customer number: {}", customer.getCustomerNumber());
    }

    public void deleteCustomerById(Long id) {
        deleteById(id);
        logger.info("Customer deleted successfully with id: {}", id);
    }

    private static void checkCustomerTypeMismatch(CreateCustomerRequest request) {
        if(request.getCustomerType() == CustomerType.B
                && (request.getTCKN() == null && request.getYKN() == null)) {
            throw new AccountException(ErrorType.INDIVIDUAL_CUSTOMER_TYPE_MISMATCH);
        }

        if(request.getCustomerType() == CustomerType.K
                && (request.getVKN() == null)) {
            throw new AccountException(ErrorType.CORPORATE_CUSTOMER_TYPE_MISMATCH);
        }
    }

    private void isCustomerAlreadyExists(CreateCustomerRequest request) {
        if (request.getTCKN() != null && customerRepository.findOptionalByTCKNAndStateTrue(request.getTCKN()).isPresent()) {
            throw new AccountException(ErrorType.CUSTOMER_ALREADY_EXISTS);
        }
        if (request.getYKN() != null && customerRepository.findOptionalByYKNAndStateTrue(request.getYKN()).isPresent()) {
            throw new AccountException(ErrorType.CUSTOMER_ALREADY_EXISTS);
        }
        if (request.getVKN() != null && customerRepository.findOptionalByVKNAndStateTrue(request.getVKN()).isPresent()) {
            throw new AccountException(ErrorType.CUSTOMER_ALREADY_EXISTS);
        }
    }

    private List<Account> setCustomerAccounts(List<Account> accounts) {
        Account defaultAccount = new Account();
        defaultAccount.setCurrencyType(CurrencyType.TRY);
        defaultAccount.setAccountNo(accountService.generateAccountNumber());

        List<Account> accountsAdded = new ArrayList<>();
        accountsAdded.add(defaultAccount);

        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {

                int currentIndex = i;
                if(accountsAdded.stream().anyMatch(a -> a.getCurrencyType() == accounts.get(currentIndex).getCurrencyType()))
                    continue;

                accounts.get(i).setAccountNo(defaultAccount.getAccountNo() - i -1);
                accountsAdded.add(accounts.get(i));
            }
        }
        accountsAdded.forEach(a -> {
            a.setCreatedDate(LocalDateTime.now());
            a.setUpdatedDate(LocalDateTime.now());
        });

        return accountsAdded;
    }

    public void validateIdentityFields(String VKN, String TCKN, String YKN) {
        int nonEmptyCount = 0;

        if (VKN != null && !VKN.isEmpty()) {
            nonEmptyCount++;
        }
        if (TCKN != null && !TCKN.isEmpty()) {
            nonEmptyCount++;
        }
        if (YKN != null && !YKN.isEmpty()) {
            nonEmptyCount++;
        }

        if (nonEmptyCount != 1) {
            throw new AccountException(ErrorType.INVALID_NUMBER_OF_IDENTITY);
        }
    }

    private void isPhoneNumberExists(String phoneNumber) {
        Optional<Customer> customer = customerRepository.findOptionalByPhoneNumber(phoneNumber);
        if(customer.isPresent()){
            throw new AccountException(ErrorType.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    private void isEmailExists(String email) {
        Optional<Customer> customer = customerRepository.findOptionalByEmailAndStateTrue(email);
        if(customer.isPresent()){
            throw new AccountException(ErrorType.EMAIL_ALREADY_EXISTS);
        }
    }

}
