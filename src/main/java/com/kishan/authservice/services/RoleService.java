package com.kishan.authservice.services;

import com.kishan.authservice.dtos.RoleDto;
import com.kishan.authservice.exceptions.InvalidRoleException;
import com.kishan.authservice.mappers.RoleMapper;
import com.kishan.authservice.models.Role;
import com.kishan.authservice.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public RoleDto createRole(String roleName) {
        Role roleEntity = new Role();
        roleEntity.setName(roleName);

        Role savedRole = roleRepository.save(roleEntity);
        return RoleMapper.convertRoleEntityToDto(savedRole);
    }

    public List<RoleDto> getAllRoles() {
        List<Role> allRoles = roleRepository.findAll();
        return allRoles.stream()
                .map(RoleMapper::convertRoleEntityToDto)
                .collect(Collectors.toList());
    }

    public Role getRoleByName(String roleName) throws InvalidRoleException {
        Optional<Role> role = roleRepository.findByName(roleName);
        if(role.isEmpty()) {
            throw new InvalidRoleException("Invalid role "+roleName);
        }
        return role.get();
    }
}
