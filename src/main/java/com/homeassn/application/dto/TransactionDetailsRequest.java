package com.homeassn.application.dto;

import java.util.List;
import java.util.StringJoiner;
import javax.validation.constraints.NotNull;


public class TransactionDetailsRequest {
  
  private List<String> userCreditLimitDetails;
  @NotNull(message = "Transaction details is required")
  private List<String> transactionDetails;

  public List<String> getUserCreditLimitDetails() {
    return userCreditLimitDetails;
  }

  public void setUserCreditLimitDetails(List<String> userCreditLimitDetails) {
    this.userCreditLimitDetails = userCreditLimitDetails;
  }

  public List<String> getTransactionDetails() {
    return transactionDetails;
  }

  public void setTransactionDetails(List<String> transactionDetails) {
    this.transactionDetails = transactionDetails;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TransactionDetailsRequest.class.getSimpleName() + "[",
        "]")
        .add("userCreditLimitDetails=" + userCreditLimitDetails)
        .add("transactionDetails=" + transactionDetails)
        .toString();
  }
}
