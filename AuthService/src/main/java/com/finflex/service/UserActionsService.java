package com.finflex.service;

import com.finflex.entity.User;
import com.finflex.entity.UserStatus;
import com.finflex.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActionsService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void activateUser(User user) {
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    public void changePassword(String newPassword, User user) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
