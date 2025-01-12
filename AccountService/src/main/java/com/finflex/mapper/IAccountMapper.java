package com.finflex.mapper;

import com.finflex.dto.request.CreateAccountRequest;
import com.finflex.dto.request.UpdateAccountRequest;
import com.finflex.dto.response.AccountResponse;
import com.finflex.entitiy.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAccountMapper {
    IAccountMapper INSTANCE = Mappers.getMapper(IAccountMapper.class);

    @Mapping(source = "customer.TCKN", target = "customerTckn")
    @Mapping(source = "customer.VKN", target = "customerVkn")
    @Mapping(source = "customer.YKN", target = "customerYkn")
    AccountResponse mapAccountToAccountResponse(Account account);

    @Mapping(source = "customerNumber", target = "customer.customerNumber")
    @Mapping(source = "balance", target = "balance")
    Account mapCreateAccountRequestToAccount(CreateAccountRequest request);

    Account updateAccountFromUpdateAccountRequest(@MappingTarget Account account, UpdateAccountRequest request);
}
