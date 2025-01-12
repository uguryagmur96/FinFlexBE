package com.finflex.service;

import com.finflex.dto.ForgotPasswordChangeRequest;
import com.finflex.entity.token.Token;
import com.finflex.entity.User;
import com.finflex.entity.token.TokenType;
import com.finflex.exception.AuthException;
import com.finflex.exception.ErrorType;
import com.finflex.repository.ITokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final ITokenRepository tokenRepository;
    private final UserActionsService userActionsService;

    public String createToken(User user, TokenType tokenType, int duration) {
        String generatedToken = UUID.randomUUID().toString();
        Token token = Token.builder()
                .token(generatedToken)
                .user(user)
                .tokenType(tokenType)
                .expiryDate(LocalDateTime.now().plusHours(duration))
                .build();

        tokenRepository.save(token);

        return generatedToken;
    }

    public String findTokenAndVerifyCreatedUser(String token) {
        Token foundToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(ErrorType.MAIL_TOKEN_NOT_FOUND));

        if (isTokenCorrectForProcess(foundToken.getTokenType(), TokenType.REGISTER)) {
            throw new AuthException(ErrorType.TOKEN_TYPE_AND_PROCESS_DOESNT_MATCH);
        }

        if (foundToken.isAlreadyUsed()) {
            throw new AuthException(ErrorType.TOKEN_ALREADY_USED);
        }

        if (isTokenExpired(foundToken.getExpiryDate())) {
            throw new AuthException(ErrorType.TOKEN_EXPIRED);
        }

        foundToken.setAlreadyUsed(true);
        tokenRepository.save(foundToken);

        userActionsService.activateUser(foundToken.getUser());

        return "Kullanıcı başarıyla doğrulandı.";
    }

    public String findTokenAndChangePassword(ForgotPasswordChangeRequest passwordDto, String token) {
        Token foundToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(ErrorType.MAIL_TOKEN_NOT_FOUND));

        if (isTokenCorrectForProcess(foundToken.getTokenType(), TokenType.FORGOT_PASSWORD)) {
            throw new AuthException(ErrorType.TOKEN_TYPE_AND_PROCESS_DOESNT_MATCH);
        }

        if (foundToken.isAlreadyUsed()) {
            throw new AuthException(ErrorType.TOKEN_ALREADY_USED);
        }

        if (isTokenExpired(foundToken.getExpiryDate())) {
            throw new AuthException(ErrorType.TOKEN_EXPIRED);
        }

        foundToken.setAlreadyUsed(true);
        tokenRepository.save(foundToken);

        userActionsService.changePassword(passwordDto.getNewPassword(), foundToken.getUser());

        return "Şifre başarıyla değiştirildi.";
    }

    private boolean isTokenExpired(LocalDateTime expireDate) {
        return expireDate.isBefore(LocalDateTime.now());
    }

    private boolean isTokenCorrectForProcess(TokenType source, TokenType target) {
        return source != target;
    }
}
