package com.kishan.authservice.controllers;

import com.kishan.authservice.dtos.UserDto;
import com.kishan.authservice.exceptions.InvalidRoleException;
import com.kishan.authservice.exceptions.UserNotFoundException;
import com.kishan.authservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") long userId) throws UserNotFoundException {
        return userService.getUserById(userId);
    }

    @PostMapping("/assign-roles/{userId}")
    public void assignRolesToUser(@PathVariable("userId") long userId, @RequestBody String[] roles) throws UserNotFoundException, InvalidRoleException {
        userService.assignRolesToUser(userId, roles);
    }
}
