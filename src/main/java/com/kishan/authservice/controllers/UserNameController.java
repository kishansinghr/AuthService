package com.kishan.authservice.controllers;

import com.kishan.authservice.beans.SessionDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/username")
public class UserNameController {

    @Autowired
    private SessionDataBean sessionDataBean;

    @GetMapping
    public String getUsername() {
        return sessionDataBean.getUsername();
    }
}
