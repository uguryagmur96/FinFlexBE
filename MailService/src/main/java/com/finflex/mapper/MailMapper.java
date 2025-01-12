package com.finflex.mapper;

import com.finflex.dto.MailRequest;
import com.finflex.entity.Mail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailMapper {
    Mail mapRegisterMailToMail(MailRequest request);
}
