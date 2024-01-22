package com.kishan.authservice.mappers;

import com.kishan.authservice.dtos.UserDto;
import com.kishan.authservice.dtos.UserRegisterDto;
import com.kishan.authservice.models.Role;
import com.kishan.authservice.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User convertUserRegisterDtoToUserEntity(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setEmail(userRegisterDto.getEmail());
        user.setName(userRegisterDto.getName());
        user.setPassword(userRegisterDto.getPassword());
        return user;
    }

    public static UserDto convertUserEntityToUserDto(User userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setName(userEntity.getName());
        if(userEntity.getRoles()!=null) {
            List<String> roleList = userEntity.getRoles()
                    .stream().map(Role::getName)
                    .collect(Collectors.toList());
            userDto.setRoles(roleList);
        }
        return userDto;
    }
}
