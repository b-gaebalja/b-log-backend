package com.bgaebalja.blogbackend.exception;

import org.springframework.security.access.AccessDeniedException;

public class TokenNoValueException extends AccessDeniedException {
    public TokenNoValueException(String message) {
        super(message);
    }
}
