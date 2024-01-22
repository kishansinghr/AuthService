package com.kishan.authservice.exceptions;

public class SessionLimitException extends Exception {
    public SessionLimitException(String msg) {
        super(msg);
    }
}
