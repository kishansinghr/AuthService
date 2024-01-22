package com.kishan.authservice.mappers;

import com.kishan.authservice.dtos.RoleDto;
import com.kishan.authservice.models.Role;

public class RoleMapper {

    public static RoleDto convertRoleEntityToDto(Role roleEntity) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(roleEntity.getName());
        roleDto.setId(roleEntity.getId());
        return roleDto;
    }
}
