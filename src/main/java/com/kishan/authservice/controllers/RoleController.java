package com.kishan.authservice.controllers;

import com.kishan.authservice.dtos.RoleDto;
import com.kishan.authservice.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody String roleName) {
        RoleDto role = roleService.createRole(roleName);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.getAllRoles();
    }
}
