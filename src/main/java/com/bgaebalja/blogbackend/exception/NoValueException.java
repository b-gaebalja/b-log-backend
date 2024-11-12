package com.bgaebalja.blogbackend.exception;

public abstract class NoValueException extends IllegalArgumentException {
    public NoValueException(String message) {
        super(message);
    }
}
