package com.finflex.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.finflex.dto.ForgotPasswordChangeRequest;
import com.finflex.entity.User;
import com.finflex.entity.token.Token;
import com.finflex.entity.token.TokenType;
import com.finflex.exception.AuthException;
import com.finflex.exception.ErrorType;
import com.finflex.repository.ITokenRepository;
import com.finflex.service.TokenService;
import com.finflex.service.UserActionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class TokenServiceTest {

    @Mock
    private ITokenRepository tokenRepository;

    @Mock
    private UserActionsService userActionsService;

    @InjectMocks
    private TokenService tokenService;

    private User testUser;
    private Token testToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = User.builder().id(1L).build();
        testToken = Token.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .user(testUser)
                .tokenType(TokenType.REGISTER)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .alreadyUsed(false)
                .build();
    }


    @Test
    public void testFindTokenAndVerifyCreatedUserSuccess() {
        when(tokenRepository.findByToken(testToken.getToken())).thenReturn(Optional.of(testToken));

        String result = tokenService.findTokenAndVerifyCreatedUser(testToken.getToken());

        assertEquals("Kullanıcı başarıyla doğrulandı.", result);
        assertTrue(testToken.isAlreadyUsed());
        verify(tokenRepository, times(1)).save(testToken);
        verify(userActionsService, times(1)).activateUser(testUser);
    }

    @Test
    public void testFindTokenAndVerifyCreatedUserTokenNotFound() {

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());


        AuthException exception = assertThrows(AuthException.class, () -> {
            tokenService.findTokenAndVerifyCreatedUser("invalidToken");
        });
        assertEquals(ErrorType.MAIL_TOKEN_NOT_FOUND, exception.getErrorType());
    }

    @Test
    public void testFindTokenAndVerifyCreatedUserTokenAlreadyUsed() {
        testToken.setAlreadyUsed(true);
        when(tokenRepository.findByToken(testToken.getToken())).thenReturn(Optional.of(testToken));

        AuthException exception = assertThrows(AuthException.class, () -> {
            tokenService.findTokenAndVerifyCreatedUser(testToken.getToken());
        });
        assertEquals(ErrorType.TOKEN_ALREADY_USED, exception.getErrorType());
    }

    @Test
    public void testFindTokenAndVerifyCreatedUserTokenExpired() {
        testToken.setExpiryDate(LocalDateTime.now().minusHours(1));
        when(tokenRepository.findByToken(testToken.getToken())).thenReturn(Optional.of(testToken));

        AuthException exception = assertThrows(AuthException.class, () -> {
            tokenService.findTokenAndVerifyCreatedUser(testToken.getToken());
        });
        assertEquals(ErrorType.TOKEN_EXPIRED, exception.getErrorType());
    }


    @Test
    public void testFindTokenAndChangePasswordTokenNotFound() {
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        AuthException exception = assertThrows(AuthException.class, () -> {
            tokenService.findTokenAndChangePassword(new ForgotPasswordChangeRequest(), "invalidToken");
        });
        assertEquals(ErrorType.MAIL_TOKEN_NOT_FOUND, exception.getErrorType());
    }

}