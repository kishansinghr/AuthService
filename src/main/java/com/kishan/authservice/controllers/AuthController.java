package com.kishan.authservice.controllers;

import com.kishan.authservice.dtos.LoginDto;
import com.kishan.authservice.dtos.SessionDto;
import com.kishan.authservice.dtos.UserDto;
import com.kishan.authservice.dtos.UserRegisterDto;
import com.kishan.authservice.exceptions.IncorrectPasswordException;
import com.kishan.authservice.exceptions.SessionLimitException;
import com.kishan.authservice.exceptions.UserNotFoundException;
import com.kishan.authservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private

    @Autowired
    AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterDto userRegisterDto) {
        UserDto userDto = userService.registerUser(userRegisterDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<SessionDto> login(@RequestBody LoginDto loginDto) throws UserNotFoundException, IncorrectPasswordException, SessionLimitException {
        SessionDto sessionDto = userService.login(loginDto);
        return new ResponseEntity<>(sessionDto, HttpStatus.OK);
    }
}
