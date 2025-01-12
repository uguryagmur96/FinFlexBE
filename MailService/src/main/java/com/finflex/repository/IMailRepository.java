package com.finflex.repository;

import com.finflex.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMailRepository extends JpaRepository<Mail, Long> {
}
