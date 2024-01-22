package com.kishan.authservice.mappers;

import com.kishan.authservice.dtos.SessionDto;
import com.kishan.authservice.models.Session;

public class SessionMapper {

    public static SessionDto convertSessionEntityToDto(Session sessionEntiy) {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setUserId(String.valueOf(sessionEntiy.getUser().getId()));
        sessionDto.setToken(sessionEntiy.getToken());
        sessionDto.setStatus(sessionEntiy.getStatus().name());
        sessionDto.setExpireAt(sessionEntiy.getExpiry());
        return sessionDto;
    }
}
