package com.kishan.authservice.services;

import com.kishan.authservice.dtos.SessionDto;
import com.kishan.authservice.mappers.SessionMapper;
import com.kishan.authservice.models.Session;
import com.kishan.authservice.models.SessionStatus;
import com.kishan.authservice.models.User;
import com.kishan.authservice.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionStatus verifyToken(String token) {
        Optional<Session> optionalSession = sessionRepository.findByToken(token);
        if(optionalSession.isEmpty()) {
            return SessionStatus.NOT_FOUND;
        }
        return optionalSession.get().getStatus();
    }

    public List<SessionDto> getActiveSessionsByUserId(long userId) {
        List<Session> activeSessions = sessionRepository.findByUser_IdAndStatusAndExpiryAfter(userId, SessionStatus.ACTIVE, new Date());
        return activeSessions.stream()
                .map(SessionMapper::convertSessionEntityToDto)
                .collect(Collectors.toList());
    }

    public SessionDto createSession(User userEntity) {
        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.MINUTE, 5);

        Session session = new Session();
        session.setUser(userEntity);
        session.setStartedAt(new Date());
        session.setExpiry(expiry.getTime());
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(UUID.randomUUID().toString());

        Session savedSession = sessionRepository.save(session);

        return SessionMapper.convertSessionEntityToDto(savedSession);
    }
}
