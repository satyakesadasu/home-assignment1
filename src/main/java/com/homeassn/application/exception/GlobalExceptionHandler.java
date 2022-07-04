package com.homeassn.application.exception;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<?> handleInvalidDataException(InvalidDataException exception) {

    ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
        exception.getMessage());
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

  }
}
