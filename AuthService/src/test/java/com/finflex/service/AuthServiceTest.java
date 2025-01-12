package com.finflex.service;

import com.finflex.dto.*;
import com.finflex.entity.EUserType;
import com.finflex.entity.User;
import com.finflex.entity.UserStatus;
import com.finflex.entity.token.TokenType;
import com.finflex.exception.AuthException;
import com.finflex.exception.ErrorType;
import com.finflex.repository.IUserRepository;
import com.finflex.service.AuthService;
import com.finflex.service.TokenService;
import com.finflex.utility.JwtTokenManager;
import com.finflex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setPassword(new BCryptPasswordEncoder().encode("password"));
        mockUser.setUserType(EUserType.USER);
        mockUser.setTckn("21342342412");
        mockUser.setPersonelNumber("43950");

        when(userService.authenticateUser(authRequest.getUsername())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(authRequest.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtTokenManager.createToken(mockUser.getId(), mockUser.getUserType(), mockUser.getTckn(), mockUser.getPersonelNumber())).thenReturn(Optional.of("mockToken"));
        when(jwtTokenManager.getRoleFromToken("mockToken")).thenReturn(Optional.of("USER"));

        AuthResponse authResponse = authService.login(authRequest);

        assertNotNull(authResponse);
        assertEquals("mockToken", authResponse.getToken());
        assertEquals(EUserType.USER, authResponse.getUserType());
    }
    @Test
    void testLogin_InvalidPassword() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("wrongpassword");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setPassword("$2a$10$7q6h65ZtM9R.LsFjJgJ7FOE5yjO5GQz8PlpDWW1Tht0d7xUVH8DOm"); // Encoded "password"

        when(userService.authenticateUser(authRequest.getUsername())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(authRequest.getPassword(), mockUser.getPassword())).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(authRequest));
        assertEquals(ErrorType.INVALID_USERNAME_OR_PASSWORD, exception.getErrorType());
        assertEquals("Şifre Hatalı", exception.getMessage());
    }




    @Test
    void testForgotPassword_Success() {
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
        forgetPasswordRequest.setMailAddress("aybukeenurcorapci0@gmail.com");

        User mockUser = new User();
        mockUser.setMailAddress("aybukeenurcorapci0@gmail.com");

        when(userRepository.findOptionalByMailAddress("aybukeenurcorapci0@gmail.com")).thenReturn(Optional.of(mockUser));
        when(tokenService.createToken(any(User.class), eq(TokenType.FORGOT_PASSWORD), anyInt())).thenReturn("mockToken");

        String result = authService.forgotPassword(forgetPasswordRequest);

        assertEquals("Forget mail sent.", result);

        ArgumentCaptor<ForgotMailRequest> captor = ArgumentCaptor.forClass(ForgotMailRequest.class);
        verify(kafkaTemplate).send(eq("send-forgot-mail"), captor.capture());

        ForgotMailRequest mailRequest = captor.getValue();
        assertEquals("aybukeenurcorapci0@gmail.com", mailRequest.getSentToMailAddress());
        assertEquals("mockToken", mailRequest.getToken());
    }
}