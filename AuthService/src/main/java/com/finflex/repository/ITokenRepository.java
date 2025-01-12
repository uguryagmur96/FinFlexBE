package com.finflex.repository;

import com.finflex.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
}
