package com.homeassn.application.exception;

import java.util.Date;

public class ErrorDetails {

  public ErrorDetails() {
  }

  public ErrorDetails(Date timestamp, int statusCode, String message) {
    this.timestamp = timestamp;
    this.statusCode = statusCode;
    this.message = message;
  }

  private Date timestamp;
  private int statusCode;
  private String message;

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
