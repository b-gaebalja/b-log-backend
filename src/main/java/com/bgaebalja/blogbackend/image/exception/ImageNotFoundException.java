package com.bgaebalja.blogbackend.image.exception;

import jakarta.persistence.EntityNotFoundException;

public class ImageNotFoundException extends EntityNotFoundException {
    public ImageNotFoundException(String message) {
        super(message);
    }
}
