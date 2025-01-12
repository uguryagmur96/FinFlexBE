package com.finflex.mapper;

import com.finflex.dto.UserRequestDto;
import com.finflex.dto.UserResponseDto;
import com.finflex.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    User userRequestDtoToUser(UserRequestDto userRequestDto);

    UserResponseDto userToUserResponseDto(User user);

    void updateUserFromDto(UserRequestDto userRequestDto, @MappingTarget User user);
}

