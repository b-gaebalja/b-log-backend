package com.bgaebalja.blogbackend.exception;

public class ParsingLongException extends NumberFormatException {
    public ParsingLongException(String message) {
        super(message);
    }
}
