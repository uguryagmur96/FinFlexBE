package com.finflex.controller;

import com.finflex.dto.request.CreateCustomerRequest;
import com.finflex.dto.response.CustomerResponse;
import com.finflex.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.finflex.constants.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequestMapping(CUSTOMER)
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(GET_ALL_CUSTOMERS)
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping( GET_CUSTOMER_BY_ID + "/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @GetMapping( GET_CUSTOMER_BY_EMAIL + "/{email}")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @GetMapping( GET_CUSTOMER_BY_CUSTOMER_NUMBER + "/{customerNumber}")
    public ResponseEntity<CustomerResponse> getCustomerByCustomerNumber(@PathVariable Long customerNumber) {
        return ResponseEntity.ok(customerService.getCustomerByCustomerNumber(customerNumber));
    }

    @GetMapping( GET_CUSTOMER_BY_TCKN + "/{customerTckn}")
    public ResponseEntity<CustomerResponse> getCustomerByTckn(@PathVariable String customerTckn) {
        return ResponseEntity.ok(customerService.getCustomerByTckn(customerTckn));
    }

    @GetMapping( GET_CUSTOMER_BY_YKN + "/{customerYkn}")
    public ResponseEntity<CustomerResponse> getCustomerByYkn(@PathVariable String customerYkn) {
        return ResponseEntity.ok(customerService.getCustomerByYkn(customerYkn));
    }

    @GetMapping( GET_CUSTOMER_BY_VKN + "/{customerVkn}")
    public ResponseEntity<CustomerResponse> getCustomerByVkn(@PathVariable String customerVkn) {
        return ResponseEntity.ok(customerService.getCustomerByVkn(customerVkn));
    }

    @PostMapping(CREATE_CUSTOMER)
    @CrossOrigin("*")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PutMapping(UPDATE_CUSTOMER_BY_CUSTOMER_NUMBER + "/{customerNumber}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long customerNumber ,@RequestBody @Valid CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(customerNumber, request));
    }

    @DeleteMapping(DELETE_CUSTOMER_BY_CUSTOMER_NO + "/{customerNumber}")
    public ResponseEntity<String> deleteCustomerByCustomerNumber(@PathVariable Long customerNumber) {
        customerService.deleteCustomerByCustomerNumber(customerNumber);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(DELETE_CUSTOMER_BY_ID + "/{customerId}")
    public void deleteCustomerById(@PathVariable Long customerId) {
        customerService.deleteCustomerById(customerId);
    }

}
