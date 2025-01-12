package com.finflex.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.finflex.dto.UserRequestDto;
import com.finflex.dto.UserResponseDto;
import com.finflex.entity.EUserType;
import com.finflex.entity.User;
import com.finflex.entity.UserStatus;
import com.finflex.exception.AuthException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.IUserMapper;
import com.finflex.repository.IUserRepository;
import com.finflex.service.TokenService;
import com.finflex.service.UserService;
import com.finflex.utility.IService;
import com.finflex.utility.JwtTokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserMapper userMapper;

    @Mock
    private JwtTokenManager tokenManager;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private TokenService tokenService;

    @Mock
    private IService<User, Long> service;

    @Spy
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testCreateUserUserExists() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setTckn("12345678901");
        userRequestDto.setMailAddress("johndoe@gmail.com");

        when(userRepository.findOptionalByTckn(anyString())).thenReturn(Optional.of(new User()));
        when(userRepository.findOptionalByMailAddress(anyString())).thenReturn(Optional.empty());

        AuthException thrown = assertThrows(AuthException.class, () -> {
            userService.createUser(userRequestDto);
        });
        assertEquals(ErrorType.USER_ALREADY_EXISTS, thrown.getErrorType());
    }


    @Test
    void testUpdateUserSuccess() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setTckn("12345678901");
        userRequestDto.setMailAddress("johndoe@gmail.com");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setTckn("12345678901");

        when(userRepository.findOptionalByTckn(anyString())).thenReturn(Optional.of(existingUser));
        when(userRepository.findOptionalByMailAddress(anyString())).thenReturn(Optional.empty());
        doNothing().when(userMapper).updateUserFromDto(any(UserRequestDto.class), any(User.class));
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(new UserResponseDto());

        UserResponseDto response = userService.updateUser(userRequestDto);

        assertNotNull(response);
        verify(userRepository).findOptionalByTckn(userRequestDto.getTckn());
        verify(userMapper).updateUserFromDto(userRequestDto, existingUser);
    }

    @Test
    void testUpdateUserEmailExists() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setTckn("12345678901");
        userRequestDto.setMailAddress("duplicateemail@gmail.com");

        User existingUser = new User();
        existingUser.setId(1L);

        User duplicateUser = new User();
        duplicateUser.setId(2L);
        duplicateUser.setMailAddress("duplicateemail@gmail.com");

        when(userRepository.findOptionalByTckn(anyString())).thenReturn(Optional.of(existingUser));
        when(userRepository.findOptionalByMailAddress(anyString())).thenReturn(Optional.of(duplicateUser));

        AuthException thrown = assertThrows(AuthException.class, () -> {
            userService.updateUser(userRequestDto);
        });
        assertEquals(ErrorType.MAIL_ADDRESS_ALREADY_EXISTS, thrown.getErrorType());
    }

    @Test
    void testUpdateUserNotFound() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setTckn("nonexistentTckn");

        when(userRepository.findOptionalByTckn(anyString())).thenReturn(Optional.empty());

        AuthException thrown = assertThrows(AuthException.class, () -> {
            userService.updateUser(userRequestDto);
        });
        assertEquals(ErrorType.USER_NOT_FOUND, thrown.getErrorType());
    }

    @Test
    void testDeactivateUserSuccess() {
        String tckn = "12345678901";
        User user = new User();
        user.setUserName("john_doe");
        user.setStatus(UserStatus.ACTIVE);

        when(userRepository.findOptionalByTckn(tckn)).thenReturn(Optional.of(user));
        doNothing().when(userService).delete(user);

        userService.deactivateUser(tckn);

        assertEquals(UserStatus.PASSIVE, user.getStatus());
        verify(userRepository).findOptionalByTckn(tckn);
        verify(userService).delete(user);
    }

    @Test
    void testDeactivateUserNotFound() {
        String tckn = "nonexistentTckn";

        when(userRepository.findOptionalByTckn(anyString())).thenReturn(Optional.empty());

        AuthException thrown = assertThrows(AuthException.class, () -> {
            userService.deactivateUser(tckn);
        });
        assertEquals(ErrorType.USER_NOT_FOUND, thrown.getErrorType());
    }


    @Test
    void testGetUserByTcknSuccess() {
        String tckn = "12345678901";
        User user = new User();
        user.setTckn(tckn);

        when(userRepository.findOptionalByTckn(anyString())).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(new UserResponseDto());

        UserResponseDto response = userService.getUserByTckn(tckn);

        assertNotNull(response);
        verify(userRepository).findOptionalByTckn(tckn);
    }

    @Test
    void testGetUserByTcknNotFound() {
        String tckn = "nonexistentTckn";

        when(userRepository.findOptionalByTckn(anyString())).thenReturn(Optional.empty());

        AuthException thrown = assertThrows(AuthException.class, () -> {
            userService.getUserByTckn(tckn);
        });
        assertEquals(ErrorType.USER_NOT_FOUND, thrown.getErrorType());
    }


    @Test
    void testGetAllUsers() {
        User activeUser = new User();
        activeUser.setUserType(EUserType.USER);
        activeUser.setStatus(UserStatus.ACTIVE);

        User inactiveUser = new User();
        inactiveUser.setUserType(EUserType.USER);
        inactiveUser.setStatus(UserStatus.PASSIVE);

        when(userRepository.findAll()).thenReturn(List.of(activeUser, inactiveUser));
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(new UserResponseDto());

        List<UserResponseDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size()); // Only active users should be returned
    }


    @Test
    void testAuthenticateUserSuccess() {
        String userName = "john_doe";
        User user = new User();
        user.setUserName(userName);
        user.setStatus(UserStatus.ACTIVE);

        when(userRepository.findOptionalByUserName(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.authenticateUser(userName);

        assertTrue(result.isPresent());
        assertEquals(userName, result.get().getUserName());
    }

    @Test
    void testAuthenticateUserInactive() {
        String userName = "john_doe";
        User user = new User();
        user.setUserName(userName);
        user.setStatus(UserStatus.PASSIVE);

        when(userRepository.findOptionalByUserName(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.authenticateUser(userName);

        assertFalse(result.isPresent());
    }

    @Test
    void testAuthenticateUserNotFound() {
        String userName = "nonexistentUser";

        when(userRepository.findOptionalByUserName(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.authenticateUser(userName);

        assertFalse(result.isPresent());
    }


    @Test
    void testExistingUsername() {
        String userName = "john_doe";

        when(userRepository.findOptionalByUserName(anyString())).thenReturn(Optional.of(new User()));

        boolean exists = userService.existingUsername(userName);

        assertTrue(exists);
    }

    @Test
    void testNonExistingUsername() {
        String userName = "nonexistentUser";

        when(userRepository.findOptionalByUserName(anyString())).thenReturn(Optional.empty());

        boolean exists = userService.existingUsername(userName);

        assertFalse(exists);
    }


    @Test
    void testExistingPersonelNumber() {
        String personelNumber = "123456";

        when(userRepository.findOptionalByPersonelNumber(anyString())).thenReturn(Optional.of(new User()));

        boolean exists = userService.existingPersonelNumber(personelNumber);

        assertTrue(exists);
    }

    @Test
    void testNonExistingPersonelNumber() {
        String personelNumber = "nonexistentNumber";

        when(userRepository.findOptionalByPersonelNumber(anyString())).thenReturn(Optional.empty());

        boolean exists = userService.existingPersonelNumber(personelNumber);

        assertFalse(exists);
    }


    @Test
    void testFindUserByIdSuccess() {
        Long id = 1L;
        User user = new User();

        when(userRepository.findOptionalById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindUserByIdNotFound() {
        Long id = 1L;

        when(userRepository.findOptionalById(anyLong())).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(id);

        assertFalse(result.isPresent());
    }


    @Test
    void testFindByPersonelNumberSuccess() {
        String personelNumber = "123456";
        User user = new User();

        when(userRepository.findOptionalByPersonelNumber(anyString())).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(new UserResponseDto());

        UserResponseDto response = userService.findByPersonelNumber(personelNumber);

        assertNotNull(response);
    }

    @Test
    void testFindByPersonelNumberNotFound() {
        String personelNumber = "nonexistentNumber";

        when(userRepository.findOptionalByPersonelNumber(anyString())).thenReturn(Optional.empty());

        AuthException thrown = assertThrows(AuthException.class, () -> {
            userService.findByPersonelNumber(personelNumber);
        });
        assertEquals(ErrorType.USER_NOT_FOUND, thrown.getErrorType());
    }


    @Test
    void testFindByUserNameSuccess() {
        String userName = "john_doe";
        User user = new User();

        when(userRepository.findOptionalByUserName(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUserName(userName);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindByUserNameNotFound() {
        String userName = "nonexistentUser";

        when(userRepository.findOptionalByUserName(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUserName(userName);

        assertFalse(result.isPresent());
    }
}
