package com.kishan.authservice.repositories;

import com.kishan.authservice.models.Session;
import com.kishan.authservice.models.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByToken(String token);

    List<Session> findByUser_IdAndStatusAndExpiryAfter(long userId, SessionStatus sessionStatus, Date expiryAfter);
}