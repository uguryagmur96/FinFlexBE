package com.finflex.service;

import com.finflex.dto.*;
import com.finflex.dto.ForgetPasswordRequest;
import com.finflex.dto.ForgotMailRequest;
import com.finflex.entity.EUserType;
import com.finflex.entity.User;
import com.finflex.entity.token.TokenType;
import com.finflex.exception.AuthException;
import com.finflex.exception.ErrorType;
import com.finflex.repository.IUserRepository;
import com.finflex.utility.JwtTokenManager;
import com.finflex.utility.ServiceManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<User, Long>{

    private final IUserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(JpaRepository<User, Long> repository, IUserRepository userRepository, JwtTokenManager jwtTokenManager, UserService userService, TokenService tokenService, KafkaTemplate<String, Object> kafkaTemplate) {
        super(repository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public AuthResponse login(AuthRequest authRequest) {
        Optional<User> optionalUser = userService.authenticateUser(authRequest.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                throw new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Şifre Hatalı");
            }
            String token = jwtTokenManager.createToken(user.getId(), user.getUserType(), user.getTckn(), user.getPersonelNumber())
                    .orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN, "Geçersiz Oturum"));

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUserType(EUserType.valueOf(jwtTokenManager.getRoleFromToken(token)
                    .orElseThrow(() -> new AuthException(ErrorType.BAD_REQUEST_ERROR, "Geçersiz rol"))));
            authResponse.setStatus(user.getStatus());
            return authResponse;
        } else {
            throw new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Kullanıcı Adı veya Şifre Hatalı");
        }
    }

    @Transactional
    public String changePassword(ChangePasswordDto changePasswordDto) {
        Long userId = verifyToken(changePasswordDto.getToken())
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN, "Geçersiz Oturum"));

        User user = userService.findUserById(userId)
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND, "Kullanıcı Bulunamadı"));

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new AuthException(ErrorType.BAD_REQUEST_ERROR, "Şifre Hatalı");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        save(user);
        return "Password changed successfully";
    }

    public Optional<Long> verifyToken(String token) {
        return jwtTokenManager.getByIdFromToken(token);
    }

    public Optional<String> getRoleFromToken(String token) {
        return jwtTokenManager.getRoleFromToken(token);
    }

    public String forgotPassword(ForgetPasswordRequest forgetPasswordRequest) {
        User user = userRepository.findOptionalByMailAddress(forgetPasswordRequest.getMailAddress())
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));

        sendForgotPasswordMail(user);

        return "Şifre sıfırlama maili gönderildi.";
    }

    private void sendForgotPasswordMail(User user) {
        ForgotMailRequest mailRequest = new ForgotMailRequest();
        mailRequest.setSentToMailAddress(user.getMailAddress());
        mailRequest.setToken(tokenService.createToken(user, TokenType.FORGOT_PASSWORD, 1));

        kafkaTemplate.send("send-forgot-mail", mailRequest);
    }
}
