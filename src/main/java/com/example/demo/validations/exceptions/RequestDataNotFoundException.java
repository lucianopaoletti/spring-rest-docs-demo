package com.example.demo.validations.exceptions;

public class RequestDataNotFoundException extends RuntimeException {
  public RequestDataNotFoundException(String message) {
    super(message);
  }
}
