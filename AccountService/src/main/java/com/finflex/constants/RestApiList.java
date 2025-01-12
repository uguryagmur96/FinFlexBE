package com.finflex.constants;

public class RestApiList {

    public static final String API = "/api";
    public static final String VERSION = "/v1";

    public static final String ACCOUNT = API + VERSION + "/account";
    public static final String CUSTOMER = API + VERSION + "/customer";
    public static final String TRANSACTION = API + VERSION + "/transaction";
    public static final String EXCHANGE_RATES = API + VERSION + "/exchange-rates";

    // Account
    public static final String GET_ALL_ACCOUNTS = "/getAllAccounts";
    public static final String GET_CUSTOMER_ACCOUNTS_BY_TCKN = "/getCustomerAccountsByTckn";
    public static final String GET_CUSTOMER_ACCOUNTS_BY_CUSTOMER_NUMBER = "/getCustomerAccountsByCustomerNumber";
    public static final String GET_ACCOUNT_BY_ID = "/getAccountById";
    public static final String GET_ACCOUNT_BY_ACCOUNT_NO = "/getAccountByAccountNo";
    public static final String CREATE_ACCOUNT = "/createAccount";
    public static final String UPDATE_ACCOUNT = "/updateAccount";
    public static final String DELETE_ACCOUNT_BY_ID = "/deleteAccountById";
    public static final String DELETE_ACCOUNT_BY_ACCOUNT_NO = "/deleteAccountByAccountNo";

    // Customer
    public static final String GET_ALL_CUSTOMERS = "/getAllCustomers";
    public static final String GET_CUSTOMER_BY_ID = "/getCustomerById";
    public static final String GET_CUSTOMER_BY_EMAIL = "/getCustomerByEmail";
    public static final String GET_CUSTOMER_BY_CUSTOMER_NUMBER = "/getCustomerByCustomerNumber";
    public static final String GET_CUSTOMER_BY_TCKN = "/getCustomerByTckn";
    public static final String GET_CUSTOMER_BY_YKN = "/getCustomerByYkn";
    public static final String GET_CUSTOMER_BY_VKN = "/getCustomerByVkn";
    public static final String CREATE_CUSTOMER = "/createCustomer";
    public static final String UPDATE_CUSTOMER_BY_CUSTOMER_NUMBER = "/updateCustomerByCustomerNumber";
    public static final String DELETE_CUSTOMER_BY_ID = "/deleteCustomerById";
    public static final String DELETE_CUSTOMER_BY_CUSTOMER_NO = "/deleteCustomerByCustomerNo";

    // Transaction
    public static final String GET_ALL_TRANSACTIONS = "/getAllTransactions";
    public static final String GET_FILTERED_TRANSACTIONS = "/getFilteredTransactions";
    public static final String GET_CUSTOMER_TRANSACTIONS_BY_TCKN = "/getCustomerTransactionsByTckn";
    public static final String GET_CUSTOMER_TRANSACTIONS_BY_USER_TCKN = "/get-customer-transactions-user-tckn";
    public static final String GET_CUSTOMER_TRANSACTIONS_BY_USER_NO = "/get-customer-transactions-user-no";
    public static final String GET_CUSTOMER_TRANSACTIONS_BY_CUSTOMER_NO = "/getCustomerTransactionsByCustomerNo";
    public static final String GET_CUSTOMER_TRANSACTIONS_BY_ACCOUNT_NO = "/getCustomerTransactionsByAccountNo";
    public static final String GET_TRANSACTION_BY_ID = "/getTransactionById";
    public static final String CREATE_TRANSACTION = "/createTransaction";
    public static final String UPDATE_TRANSACTION = "/updateTransaction";
    public static final String DELETE_TRANSACTION_BY_ID = "/deleteTransactionById";
    public static final String DEPOSIT_MONEY = "/deposit-money";
    public static final String WITHDRAW_MONEY = "/withdraw-money";

    //Exchange Rates
   public static final String GET_EXCHANGE_RATES = "/get-exchange-rates";
    public static final String GET_ALL_EXCHANGE_RATES = "/get-all-exchange-rates";


}
