package com.kishan.authservice.exceptions;

public class IncorrectPasswordException extends  Exception {
    public IncorrectPasswordException(String msg) {
        super(msg);
    }
}
