package com.bgaebalja.blogbackend.exception;

public class JwtCustomException extends RuntimeException {

  public JwtCustomException() {
    super();
  }

  public JwtCustomException(String message) {
    super(message);
  }

  public JwtCustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public JwtCustomException(Throwable cause) {
    super(cause);
  }
}
