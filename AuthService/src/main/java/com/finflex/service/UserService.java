package com.finflex.service;

import com.finflex.dto.UserRequestDto;
import com.finflex.dto.UserResponseDto;
import com.finflex.dto.RegisterMailRequest;
import com.finflex.entity.EUserType;
import com.finflex.entity.User;
import com.finflex.entity.UserStatus;
import com.finflex.entity.token.TokenType;
import com.finflex.exception.AuthException;
import com.finflex.exception.ErrorType;
import com.finflex.mapper.IUserMapper;
import com.finflex.repository.IUserRepository;
import com.finflex.utility.GenerateUsernamePassword;
import com.finflex.utility.JwtTokenManager;
import com.finflex.utility.ServiceManager;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends ServiceManager<User, Long> {


    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final JwtTokenManager tokenManager;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(IUserRepository userRepository, IUserMapper userMapper, JwtTokenManager tokenManager, KafkaTemplate<String, Object> kafkaTemplate, TokenService tokenService) {
        super(userRepository);
        this.userMapper = userMapper;
        this.userRepository=userRepository;
        this.tokenManager = tokenManager;
        this.kafkaTemplate = kafkaTemplate;
        this.tokenService = tokenService;
    }

    @Transactional
    public User createUser(UserRequestDto userRequestDto) {
        validateUserDoesNotExist(userRequestDto);
        User user = mapToUser(userRequestDto);
        String username = generateUniqueUsername(userRequestDto.getFirstName(), userRequestDto.getLastName());
        user.setUserName(username);
        String personelNumber = generateUniquePersonelNumber();
        user.setPersonelNumber(personelNumber);
        String rawPassword = GenerateUsernamePassword.generatePassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        setDefaultUserProperties(user);
        User savedUser = save(user);
        sendRegistrationMail(user,rawPassword);

        return savedUser;
    }

    private void validateUserDoesNotExist(UserRequestDto userRequestDto) {
        if (userRepository.findOptionalByTckn(userRequestDto.getTckn()).isPresent() ||
                existingMailAddress(userRequestDto.getMailAddress())) {
            throw new AuthException(ErrorType.USER_ALREADY_EXISTS, "Kullanıcı Zaten Kayıtlı");
        }
    }

    private User mapToUser(UserRequestDto userRequestDto) {
        return userMapper.userRequestDtoToUser(userRequestDto);
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String username = GenerateUsernamePassword.generateUsername(firstName, lastName);
        StringBuilder usernameBuilder = new StringBuilder(username);
        int count = 1;

        while (existingUsername(usernameBuilder.toString())) {
            usernameBuilder.setLength(username.length());
            usernameBuilder.append(count);
            count++;
        }

        return usernameBuilder.toString();
    }

    private String generateUniquePersonelNumber() {
        String personelNumber = GenerateUsernamePassword.generatePersonelNumber();

        while (existingPersonelNumber(personelNumber)) {
            personelNumber = GenerateUsernamePassword.generatePersonelNumber();
        }

        return personelNumber;
    }

    private void setDefaultUserProperties(User user) {
        user.setUserType(EUserType.USER);
        user.setStatus(UserStatus.PASSIVE);
    }

    private void sendRegistrationMail(User user,String rawPassword) {
        RegisterMailRequest mailRequest = new RegisterMailRequest();
        mailRequest.setSentToMailAddress(user.getMailAddress());
        mailRequest.setToken(tokenService.createToken(user, TokenType.REGISTER, 24));
        mailRequest.setUsername(user.getUserName());
        mailRequest.setPassword(rawPassword);

        kafkaTemplate.send("send-register-mail", mailRequest);
    }

    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        Optional<User> existingUserOpt = userRepository.findOptionalByTckn(userRequestDto.getTckn());
        Optional<User> mailOptUser = userRepository.findOptionalByMailAddress(userRequestDto.getMailAddress());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (mailOptUser.isEmpty() || mailOptUser.get().getId().equals(existingUser.getId())) {
                userMapper.updateUserFromDto(userRequestDto, existingUser);
                update(existingUser);
                System.out.println("Personel güncellendi");
                return userMapper.userToUserResponseDto(existingUser);
            } else {
                throw new AuthException(ErrorType.MAIL_ADDRESS_ALREADY_EXISTS, "Bu Mail Adresi Kullanılamaz");
            }
        } else {
            throw new AuthException(ErrorType.USER_NOT_FOUND, "Kullanıcı Bulunamadı");
        }
    }


    @Transactional
    public void deactivateUser(String tckn) {
        Optional<User> optionalUser = userRepository.findOptionalByTckn(tckn);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(UserStatus.PASSIVE);
            System.out.println("kullanıcı pasife alındı :"+ user.getUserName());
            delete(user);
        } else {
            throw new AuthException(ErrorType.USER_NOT_FOUND);
        }
    }
    public UserResponseDto getUserByTckn(String tckn) {
        Optional<User> optionalUser = userRepository.findOptionalByTckn(tckn);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return userMapper.userToUserResponseDto(user);
        } else {
            throw new AuthException(ErrorType.USER_NOT_FOUND);
        }
    }
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> user.getUserType().equals(EUserType.USER)&&user.getStatus().equals(UserStatus.ACTIVE))
                .map(userMapper::userToUserResponseDto)
                .toList();
    }

    public Optional<User> authenticateUser(String userName){
        Optional<User> optUser = userRepository.findOptionalByUserName(userName);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if ( user.getStatus()==UserStatus.ACTIVE) {
                return optUser;
            }
        }
        return Optional.empty();
    }

    public boolean existingUsername(String userName){
        return userRepository.findOptionalByUserName(userName).isPresent();
    }
    public boolean existingPersonelNumber(String personelNumber){
        return userRepository.findOptionalByPersonelNumber(personelNumber).isPresent();
    }

    public Optional<User> findUserById(Long id){
        return userRepository.findOptionalById(id);
    }
    public boolean existingMailAddress(String email){
        Optional<User> user = userRepository.findOptionalByMailAddress(email);
        return user.isPresent();
    }
    public UserResponseDto findByPersonelNumber(String personelNumber) {
        User user = userRepository.findOptionalByPersonelNumber(personelNumber)
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));
        return userMapper.userToUserResponseDto(user);

    }

    public Optional<User> findByUserName(String username){
        return userRepository.findOptionalByUserName(username);
    }



}
