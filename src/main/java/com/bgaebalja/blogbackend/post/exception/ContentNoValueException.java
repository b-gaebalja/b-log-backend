package com.bgaebalja.blogbackend.post.exception;

import com.bgaebalja.blogbackend.exception.NoValueException;

public class ContentNoValueException extends NoValueException {
    public ContentNoValueException(String message) {
        super(message);
    }
}
