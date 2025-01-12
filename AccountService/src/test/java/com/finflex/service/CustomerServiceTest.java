package com.finflex.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.finflex.dto.request.CreateCustomerRequest;
import com.finflex.dto.response.CustomerResponse;
import com.finflex.entitiy.Customer;
import com.finflex.entitiy.enums.CustomerType;
import com.finflex.exception.AccountException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.IAccountMapper;
import com.finflex.mapper.ICustomerMapper;
import com.finflex.repository.ICustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.when;

public class CustomerServiceTest {
    @Mock
    private ICustomerRepository customerRepository;

    @Mock
    private ICustomerMapper customerMapper;

    @Mock
    private CustomerService customerServiceInstance;
    @Mock
    private IAccountMapper accountMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomers() {

        Customer customer = new Customer();
        customer.setState(true);

        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        List<CustomerResponse> responses = customerService.getAllCustomers();

        verify(customerRepository).findAll();
        verify(customerMapper).customerToCustomerResponse(customer);


        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(customerResponse, responses.get(0));
    }
    @Test
    public void testGetAllCustomers_NoActiveCustomers() {
        Customer inactiveCustomer = new Customer();
        inactiveCustomer.setState(false);

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(inactiveCustomer));
        when(customerMapper.customerToCustomerResponse(inactiveCustomer)).thenReturn(null);

        List<CustomerResponse> responses = customerService.getAllCustomers();

        verify(customerRepository).findAll();
        verify(customerMapper, never()).customerToCustomerResponse(any());

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testGetCustomerById_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findOptionalByCustomerIdAndStateTrue(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.getCustomerById(customerId);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository).findOptionalByCustomerIdAndStateTrue(customerId);
        verify(customerMapper).customerToCustomerResponse(customer);
    }

    @Test
    void testGetCustomerById_NotFound() {
        Long customerId = 1L;

        when(customerRepository.findOptionalByCustomerIdAndStateTrue(customerId)).thenReturn(Optional.empty());

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.getCustomerById(customerId));
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, thrown.getErrorType());
        verify(customerRepository).findOptionalByCustomerIdAndStateTrue(customerId);
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    void testGetCustomerByEmail_Success() {
        String email = "test@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);

        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findOptionalByEmailAndStateTrue(email)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.getCustomerByEmail(email);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository).findOptionalByEmailAndStateTrue(email);
        verify(customerMapper).customerToCustomerResponse(customer);
    }

    @Test
    void testGetCustomerByEmail_NotFound() {
        String email = "test@example.com";

        when(customerRepository.findOptionalByEmailAndStateTrue(email)).thenReturn(Optional.empty());

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.getCustomerByEmail(email));
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, thrown.getErrorType());
        verify(customerRepository).findOptionalByEmailAndStateTrue(email);
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    void testGetCustomerByCustomerNumber_Success() {
        Long customerNumber = 12345L;
        Customer customer = new Customer();
        customer.setCustomerNumber(customerNumber);

        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.getCustomerByCustomerNumber(customerNumber);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository).findOptionalByCustomerNumberAndStateTrue(customerNumber);
        verify(customerMapper).customerToCustomerResponse(customer);
    }

    @Test
    void testGetCustomerByCustomerNumber_NotFound() {
        Long customerNumber = 12345L;

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)).thenReturn(Optional.empty());

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.getCustomerByCustomerNumber(customerNumber));
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, thrown.getErrorType());
        verify(customerRepository).findOptionalByCustomerNumberAndStateTrue(customerNumber);
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    void testGetCustomerByTckn_Success() {
        String tckn = "12345678901";
        Customer customer = new Customer();
        customer.setTCKN(tckn);

        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findOptionalByTCKNAndStateTrue(tckn)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.getCustomerByTckn(tckn);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository).findOptionalByTCKNAndStateTrue(tckn);
        verify(customerMapper).customerToCustomerResponse(customer);
    }

    @Test
    void testGetCustomerByTckn_NotFound() {
        String tckn = "12345678901";

        when(customerRepository.findOptionalByTCKNAndStateTrue(tckn)).thenReturn(Optional.empty());

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.getCustomerByTckn(tckn));
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, thrown.getErrorType());
        verify(customerRepository).findOptionalByTCKNAndStateTrue(tckn);
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    void testGetOptionalCustomerByYkn_Found() {
        String ykn = "YKN123456";
        Customer customer = new Customer();
        customer.setYKN(ykn);

        when(customerRepository.findOptionalByYKNAndStateTrue(ykn)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getOptionalCustomerByYkn(ykn);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        verify(customerRepository).findOptionalByYKNAndStateTrue(ykn);
    }

    @Test
    void testGetOptionalCustomerByYkn_NotFound() {
        String ykn = "YKN123456";

        when(customerRepository.findOptionalByYKNAndStateTrue(ykn)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getOptionalCustomerByYkn(ykn);

        assertFalse(result.isPresent());
        verify(customerRepository).findOptionalByYKNAndStateTrue(ykn);
    }

    @Test
    void testGetCustomerByVkn_Success() {
        String vkn = "VKN123456";
        Customer customer = new Customer();
        customer.setVKN(vkn);

        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findOptionalByVKNAndStateTrue(vkn)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.getCustomerByVkn(vkn);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository).findOptionalByVKNAndStateTrue(vkn);
        verify(customerMapper).customerToCustomerResponse(customer);
    }

    @Test
    void testGetCustomerByVkn_NotFound() {
        String vkn = "VKN123456";

        when(customerRepository.findOptionalByVKNAndStateTrue(vkn)).thenReturn(Optional.empty());

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.getCustomerByVkn(vkn));
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, thrown.getErrorType());
        verify(customerRepository).findOptionalByVKNAndStateTrue(vkn);
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }



    @Test
    void testCreateCustomer_EmailExists() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("existing@example.com");
        request.setTCKN("12345678941");

        when(customerMapper.createCustomerRequestToCustomer(request)).thenReturn(new Customer());
        when(customerRepository.findMaxCustomerNumber()).thenReturn(100L);
        when(customerRepository.findOptionalByEmailAndStateTrue(request.getEmail())).thenReturn(Optional.of(new Customer()));


        AccountException thrown = assertThrows(AccountException.class, () -> customerService.createCustomer(request));
        assertEquals(ErrorType.EMAIL_ALREADY_EXISTS, thrown.getErrorType());
    }

    @Test
    void testCreateCustomer_PhoneNumberExists() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setPhoneNumber("1234567890");
        request.setTCKN("12354869751");


        when(customerMapper.createCustomerRequestToCustomer(request)).thenReturn(new Customer());
        when(customerRepository.findMaxCustomerNumber()).thenReturn(100L);
        when(customerRepository.findOptionalByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(new Customer()));


        AccountException thrown = assertThrows(AccountException.class, () -> customerService.createCustomer(request));
        assertEquals(ErrorType.PHONE_NUMBER_ALREADY_EXISTS, thrown.getErrorType());
    }

    @Test
    void testUpdateCustomer_EmailAlreadyExists() {
        Long customerNumber = 123L;
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("existingemail@example.com");

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerNumber(customerNumber);
        existingCustomer.setEmail("oldemail@example.com");

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findOptionalByEmailAndStateTrue(request.getEmail())).thenReturn(Optional.of(new Customer()));

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.updateCustomer(customerNumber, request));
        assertEquals(ErrorType.EMAIL_ALREADY_EXISTS, thrown.getErrorType());

        verify(customerRepository).findOptionalByCustomerNumberAndStateTrue(customerNumber);
        verify(customerRepository).findOptionalByEmailAndStateTrue(request.getEmail());
        verify(customerRepository, never()).findOptionalByPhoneNumber(anyString());
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_PhoneNumberAlreadyExists() {
        Long customerNumber = 123L;
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setPhoneNumber("existingphonenumber");

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerNumber(customerNumber);
        existingCustomer.setPhoneNumber("1234567890");

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findOptionalByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(new Customer()));

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.updateCustomer(customerNumber, request));
        assertEquals(ErrorType.PHONE_NUMBER_ALREADY_EXISTS, thrown.getErrorType());

        verify(customerRepository).findOptionalByCustomerNumberAndStateTrue(customerNumber);
        verify(customerRepository).findOptionalByPhoneNumber(request.getPhoneNumber());
        verify(customerRepository, never()).findOptionalByEmailAndStateTrue(anyString());
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_CustomerNotFound() {
        Long customerNumber = 123L;
        CreateCustomerRequest request = new CreateCustomerRequest();

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)).thenReturn(Optional.empty());

        AccountException thrown = assertThrows(AccountException.class, () -> customerService.updateCustomer(customerNumber, request));
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, thrown.getErrorType());

        verify(customerRepository).findOptionalByCustomerNumberAndStateTrue(customerNumber);
        verify(customerRepository, never()).findOptionalByEmailAndStateTrue(anyString());
        verify(customerRepository, never()).findOptionalByPhoneNumber(anyString());
        verify(customerMapper, never()).customerToCustomerResponse(any(Customer.class));
    }

    @Test
    void testDeleteCustomerByCustomerNumber_Success() {
        Long customerNumber = 123L;

        Customer customer = new Customer();
        customer.setCustomerNumber(customerNumber);
        customer.setState(true);

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)).thenReturn(Optional.of(customer));

        customerService.deleteCustomerByCustomerNumber(customerNumber);

        verify(customerRepository).save(customer);

        assertFalse(customer.getState(), "Customer state should be set to false");
    }


    @Test
    void testDeleteCustomerByCustomerNumber_CustomerNotFound() {
        Long customerNumber = 123L;

        when(customerRepository.findOptionalByCustomerNumberAndStateTrue(customerNumber)).thenReturn(Optional.empty());

        AccountException thrown = assertThrows(AccountException.class, () ->
                customerService.deleteCustomerByCustomerNumber(customerNumber)
        );
        assertEquals(ErrorType.CUSTOMER_NOT_FOUND, thrown.getErrorType());

        verify(customerRepository).findOptionalByCustomerNumberAndStateTrue(customerNumber);

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testCheckCustomerTypeMismatch_IndividualCustomerTypeMismatch() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setCustomerType(CustomerType.B);
        request.setTCKN(null);
        request.setYKN(null);

        AccountException thrown = assertThrows(AccountException.class, () -> {
            checkCustomerTypeMismatch(request);
        });
        assertEquals(ErrorType.INDIVIDUAL_CUSTOMER_TYPE_MISMATCH, thrown.getErrorType());
    }

    @Test
    void testCheckCustomerTypeMismatch_CorporateCustomerTypeMismatch() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setCustomerType(CustomerType.K);
        request.setVKN(null);

        AccountException thrown = assertThrows(AccountException.class, () -> {
            checkCustomerTypeMismatch(request);
        });
        assertEquals(ErrorType.CORPORATE_CUSTOMER_TYPE_MISMATCH, thrown.getErrorType());
    }

    @Test
    void testCheckCustomerTypeMismatch_NoMismatch() {
        CreateCustomerRequest individualRequest = new CreateCustomerRequest();
        individualRequest.setCustomerType(CustomerType.B);
        individualRequest.setTCKN("someTCKN");

        CreateCustomerRequest corporateRequest = new CreateCustomerRequest();
        corporateRequest.setCustomerType(CustomerType.K);
        corporateRequest.setVKN("someVKN");

        assertDoesNotThrow(() -> checkCustomerTypeMismatch(individualRequest));
        assertDoesNotThrow(() -> checkCustomerTypeMismatch(corporateRequest));
    }

    private static void checkCustomerTypeMismatch(CreateCustomerRequest request) {
        if (request.getCustomerType() == CustomerType.B
                && (request.getTCKN() == null && request.getYKN() == null)) {
            throw new AccountException(ErrorType.INDIVIDUAL_CUSTOMER_TYPE_MISMATCH);
        }

        if (request.getCustomerType() == CustomerType.K
                && (request.getVKN() == null)) {
            throw new AccountException(ErrorType.CORPORATE_CUSTOMER_TYPE_MISMATCH);
        }
    }

    @Test
    void testValidateIdentityFields_OneFieldNotEmpty() {
        assertDoesNotThrow(() -> validateIdentityFields("someVKN", null, null));
        assertDoesNotThrow(() -> validateIdentityFields(null, "someTCKN", null));
        assertDoesNotThrow(() -> validateIdentityFields(null, null, "someYKN"));
    }

    @Test
    void testValidateIdentityFields_NoFieldsNotEmpty() {
        assertThrows(AccountException.class, () -> validateIdentityFields(null, null, null));
    }

    @Test
    void testValidateIdentityFields_TwoFieldsNotEmpty() {
        assertThrows(AccountException.class, () -> validateIdentityFields("someVKN", "someTCKN", null));
        assertThrows(AccountException.class, () -> validateIdentityFields("someVKN", null, "someYKN"));
        assertThrows(AccountException.class, () -> validateIdentityFields(null, "someTCKN", "someYKN"));
    }

    @Test
    void testValidateIdentityFields_AllFieldsNotEmpty() {
        assertThrows(AccountException.class, () -> validateIdentityFields("someVKN", "someTCKN", "someYKN"));
    }

    private void validateIdentityFields(String VKN, String TCKN, String YKN) {
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
}
