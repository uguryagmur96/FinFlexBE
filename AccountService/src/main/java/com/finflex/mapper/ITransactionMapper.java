package com.finflex.mapper;

import com.finflex.dto.request.CreateTransactionRequest;
import com.finflex.dto.response.TransactionResponse;
import com.finflex.entitiy.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITransactionMapper {

    ITransactionMapper INSTANCE = Mappers.getMapper(ITransactionMapper.class);

    @Mapping(source = "customer.TCKN", target = "customerTckn")
    @Mapping(source = "sourceCurrType", target = "sourceCurrType")
    @Mapping(source = "targetCurrType", target = "targetCurrType")
    @Mapping(source = "customer.customerNumber", target = "customerNumber")
    @Mapping(source = "userTCKN", target = "userTCKN")
    @Mapping(source = "account.accountNo", target = "accountNumber")
    @Mapping(source = "createdDate", target = "transactionDate")
    @Mapping(source = "userNo", target = "userNo")
    @Mapping(source = "customer.firstName", target = "customerFirstName")
    @Mapping(source = "customer.lastName", target = "customerLastName")
    TransactionResponse transactionToTransactionResponse(Transaction transaction);
    Transaction createTransactionRequestToTransaction(CreateTransactionRequest request);
}
