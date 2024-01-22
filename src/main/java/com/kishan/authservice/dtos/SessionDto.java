package com.kishan.authservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SessionDto {
    private String userId;
    private String token;
    private Date expireAt;
    private String status;
}
