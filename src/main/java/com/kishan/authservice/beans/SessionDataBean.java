package com.kishan.authservice.beans;

import com.kishan.authservice.dtos.UserDto;
import com.kishan.authservice.models.SessionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SessionDataBean {
    private String username;
    private List<String> roles;
    private SessionStatus status;
    private UserDto userDto;
}
