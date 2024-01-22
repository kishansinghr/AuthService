package com.kishan.authservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private long id;
    private String email;
    private String name;
    private List<String> roles;
}
