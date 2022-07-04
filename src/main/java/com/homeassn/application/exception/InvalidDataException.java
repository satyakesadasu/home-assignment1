package com.homeassn.application.exception;

public class InvalidDataException extends RuntimeException {
  
  private static final long serialVersionUID = -3867341917133461251L;

  public InvalidDataException() {
  }

  public InvalidDataException(String s) {
    super(s);
  }
}
