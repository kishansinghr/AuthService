package com.kishan.authservice.services;

import com.kishan.authservice.dtos.LoginDto;
import com.kishan.authservice.dtos.SessionDto;
import com.kishan.authservice.dtos.UserDto;
import com.kishan.authservice.dtos.UserRegisterDto;
import com.kishan.authservice.exceptions.IncorrectPasswordException;
import com.kishan.authservice.exceptions.InvalidRoleException;
import com.kishan.authservice.exceptions.SessionLimitException;
import com.kishan.authservice.exceptions.UserNotFoundException;
import com.kishan.authservice.mappers.UserMapper;
import com.kishan.authservice.models.Role;
import com.kishan.authservice.models.User;
import com.kishan.authservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, SessionService sessionService,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.roleService = roleService;
    }

    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        User userEntity = UserMapper.convertUserRegisterDtoToUserEntity(userRegisterDto);
        User saveUser = userRepository.save(userEntity);
        return UserMapper.convertUserEntityToUserDto(saveUser);
    }

    public SessionDto login(LoginDto loginDto) throws UserNotFoundException, IncorrectPasswordException, SessionLimitException {
        Optional<User> userEntityOptional = userRepository.findByEmail(loginDto.getEmail());

        if(userEntityOptional.isEmpty()) {
            throw new UserNotFoundException("User not registered with email "+loginDto.getEmail());
        }
        User userEntity = userEntityOptional.get();
        //Check active sessions
        List<SessionDto> activeSessions = sessionService.getActiveSessionsByUserId(userEntity.getId());
        if(activeSessions.size()>=2) {
            throw new SessionLimitException("You already have maximum number of active sessions");
        }

        //compare password
        if(!userEntity.getPassword().equals(loginDto.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password "+loginDto.getPassword());
        }

        return sessionService.createSession(userEntity);
    }

    public UserDto getUserById(long userId) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found for id "+userId+".");
        }
        return UserMapper.convertUserEntityToUserDto(optionalUser.get());
    }

    public List<UserDto> getAllUsers() {
        List<User> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(UserMapper::convertUserEntityToUserDto)
                .collect(Collectors.toList());
    }

    public void assignRolesToUser(long userId, String[] roleNames) throws UserNotFoundException, InvalidRoleException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found by id "+userId);
        }
        User user = optionalUser.get();

        for (String roleName : roleNames) {
            Role role = roleService.getRoleByName(roleName);
            user.getRoles().add(role);
        }

        userRepository.save(user);
    }
}
