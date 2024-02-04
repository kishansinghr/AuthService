package com.kishan.authservice.services;

import com.kishan.authservice.beans.SessionDataBean;
import com.kishan.authservice.dtos.SessionDto;
import com.kishan.authservice.exceptions.InvalidTokenException;
import com.kishan.authservice.mappers.SessionMapper;
import com.kishan.authservice.mappers.UserMapper;
import com.kishan.authservice.models.Role;
import com.kishan.authservice.models.Session;
import com.kishan.authservice.models.SessionStatus;
import com.kishan.authservice.models.User;
import com.kishan.authservice.repositories.SessionRepository;
import com.kishan.authservice.utils.JWTGenerator;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionDataBean verifyToken(String token) throws InvalidTokenException {

        SessionDataBean sessionDataBean = new SessionDataBean();
        try {
            Claims claims = JWTGenerator.parseJwtToken(token);
            if (claims.getExpiration().after(new Date())) {
                String userName = (String) claims.get("username");
                List<String> roles = (List<String>) claims.get("roles");

                sessionDataBean.setUsername(userName);
                sessionDataBean.setRoles(roles);

                Optional<Session> optionalSession = sessionRepository.findByToken(token);
                if (optionalSession.isEmpty()) {
                    sessionDataBean.setStatus(SessionStatus.NOT_FOUND);
                } else {
                    Session session = optionalSession.get();
                    sessionDataBean.setStatus(session.getStatus());
                    sessionDataBean.setUserDto(UserMapper.convertUserEntityToUserDto(session.getUser()));
                }
            } else {
                sessionDataBean.setStatus(SessionStatus.EXPIRED);
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            sessionDataBean.setStatus(SessionStatus.NOT_FOUND);
        }
        return sessionDataBean;
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
//        session.setToken(UUID.randomUUID().toString());
        List<String> roles = userEntity.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        String jwtToken = JWTGenerator.generateJwtForUsernameAndRole(userEntity.getEmail(), roles, expiry.getTime());
        session.setToken(jwtToken);

        Session savedSession = sessionRepository.save(session);

        return SessionMapper.convertSessionEntityToDto(savedSession);
    }
}
