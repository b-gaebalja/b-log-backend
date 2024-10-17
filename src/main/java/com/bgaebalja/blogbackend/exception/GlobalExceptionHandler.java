package com.bgaebalja.blogbackend.exception;

import static org.springframework.http.HttpStatus.*;

import com.bgaebalja.blogbackend.user.exception.DeleteUserFailException;
import com.bgaebalja.blogbackend.user.exception.JwtCustomException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String LOG_ERROR_MESSAGE = "Error exception occurred: {}";
    private static final String LOG_WARN_MESSAGE = "Warning exception occurred: {}";
    private static final String LOG_INFO_MESSAGE = "Exception occurred: {}";

    @ExceptionHandler(IOException.class)
    private ResponseEntity<ErrorResponse> handleIOException
            (IOException e) {
        log.error(LOG_ERROR_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<ErrorResponse> handleNullPointerException
            (NullPointerException e) {
        log.error(LOG_ERROR_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleEntityNotFoundException
            (EntityNotFoundException e) {
        log.warn(LOG_WARN_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(NOT_FOUND.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException
            (HttpRequestMethodNotSupportedException e) {
        log.warn(LOG_WARN_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(METHOD_NOT_ALLOWED.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ErrorResponse> handleAccessDeniedException
            (AccessDeniedException e) {
        log.warn(LOG_WARN_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(FORBIDDEN.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleIllegalArgumentException
            (IllegalArgumentException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException
            (HttpMessageNotReadableException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException
            (HttpMediaTypeNotSupportedException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(UNSUPPORTED_MEDIA_TYPE.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(MalformedJwtException.class)
    private ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    private ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(DeleteUserFailException.class)
    private ResponseEntity<ErrorResponse> handleDeleteUserFailException(DeleteUserFailException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .statusCode(NOT_IMPLEMENTED.value())
            .message(e.getMessage())
            .build();
        return new ResponseEntity<>(errorResponse, resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(JwtCustomException.class)
    private ResponseEntity<?> handleJwtCustomException(JwtCustomException e) {
        log.info(LOG_INFO_MESSAGE, e.getMessage(), e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR.value()).body(Map.of("ERROR",
            Objects.requireNonNull(e.getMessage())));
    }
}
