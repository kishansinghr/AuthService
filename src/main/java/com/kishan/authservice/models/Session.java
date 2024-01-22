package com.kishan.authservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Session  extends  BaseModel{
    private String token;
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = User.class)
    private User user;
    @Enumerated(EnumType.STRING)
    private SessionStatus status;
    private Date startedAt;
    private Date expiry;
}
