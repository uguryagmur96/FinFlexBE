package com.finflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.finflex.entity.User;
import com.finflex.entity.UserStatus;
import com.finflex.repository.IUserRepository;
import com.finflex.service.UserActionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserActionsServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserActionsService userActionsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test cases for activateUser
    @Test
    void testActivateUserSuccess() {
        User user = new User();
        user.setStatus(UserStatus.PASSIVE);

        when(userRepository.save(any(User.class))).thenReturn(user);

        userActionsService.activateUser(user);

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void testChangePasswordSuccess() {
        String newPassword = "newPassword";
        User user = new User();
        String encodedPassword = "encodedNewPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userActionsService.changePassword(newPassword, user);

        assertEquals(encodedPassword, user.getPassword());
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(user);
    }
}
