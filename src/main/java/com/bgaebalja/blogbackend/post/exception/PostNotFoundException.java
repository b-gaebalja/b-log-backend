package com.bgaebalja.blogbackend.post.exception;

import jakarta.persistence.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
