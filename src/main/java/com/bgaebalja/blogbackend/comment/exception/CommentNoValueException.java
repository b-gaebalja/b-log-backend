package com.bgaebalja.blogbackend.comment.exception;

public class CommentNoValueException extends IllegalArgumentException {
    public CommentNoValueException(String message) {
        super(message);
    }
}
