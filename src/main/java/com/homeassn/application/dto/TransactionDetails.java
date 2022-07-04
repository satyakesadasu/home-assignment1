package com.homeassn.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionDetails {

  @JsonProperty("First Name")
  private String firstName;
  @JsonProperty("Last Name")
  private String lastName;
  @JsonProperty("Email Id")
  private String emailId;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private int transactionCost;
  @JsonProperty("Transaction Number")
  private String transactionNumber;

  public TransactionDetails() {
    // default constructor
  }

  public TransactionDetails(String firstName, String lastName, String emailId, int transactionCost,
      String transactionNumber) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
    this.transactionCost = transactionCost;
    this.transactionNumber = transactionNumber;
  }

  public TransactionDetails(String firstName, String lastName, String emailId,
      String transactionNumber) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
    this.transactionNumber = transactionNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getTransactionNumber() {
    return transactionNumber;
  }

  public void setTransactionNumber(String transactionNumber) {
    this.transactionNumber = transactionNumber;
  }

  public int getTransactionCost() {
    return transactionCost;
  }

  public void setTransactionCost(int transactionCost) {
    this.transactionCost = transactionCost;
  }

  @Override
  public String toString() {
    return "Transaction Details [firstName=" + firstName + ", lastName=" + lastName + ", emailId="
        + emailId
        + ", transactionCost=" + transactionCost + ", transactionNumber=" + transactionNumber + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + transactionCost;
    result = prime * result + ((transactionNumber == null) ? 0 : transactionNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TransactionDetails other = (TransactionDetails) obj;
    if (emailId == null) {
      if (other.emailId != null) {
        return false;
      }
    } else if (!emailId.equals(other.emailId)) {
      return false;
    }
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!lastName.equals(other.lastName)) {
      return false;
    }
    if (transactionCost != other.transactionCost) {
      return false;
    }
    if (transactionNumber == null) {
      return other.transactionNumber == null;
    } else {
      return transactionNumber.equals(other.transactionNumber);
    }
  }

}
