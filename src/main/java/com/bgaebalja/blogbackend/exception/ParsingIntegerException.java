package com.bgaebalja.blogbackend.exception;

public class ParsingIntegerException extends NumberFormatException {
    public ParsingIntegerException(String message) {
        super(message);
    }
}
