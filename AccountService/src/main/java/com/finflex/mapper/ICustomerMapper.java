package com.finflex.mapper;

import com.finflex.dto.request.CreateCustomerRequest;
import com.finflex.dto.response.CustomerResponse;
import com.finflex.entitiy.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICustomerMapper {
    ICustomerMapper INSTANCE = Mappers.getMapper(ICustomerMapper.class);

    @Mapping(source = "address.fullAddress", target = "fullAddress")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.district", target = "district")
    @Mapping(source = "address.postalCode", target = "postalCode")
    CustomerResponse customerToCustomerResponse(final Customer customer);

    @Mapping(source = "fullAddress", target = "address.fullAddress")
    @Mapping(source = "city", target = "address.city")
    @Mapping(source = "district", target = "address.district")
    @Mapping(source = "postalCode", target = "address.postalCode")
    Customer createCustomerRequestToCustomer(final CreateCustomerRequest createCustomerRequest);
}
