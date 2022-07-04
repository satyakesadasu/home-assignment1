package com.homeassn.application.dto;

import java.util.List;

public class TransactionsRequest {

  private List<UserCreditLimitDetails> userCreditLimitDetails;
  private List<TransactionDetails> transactionDetails;

  public List<UserCreditLimitDetails> getUserCreditLimitDetails() {
    return userCreditLimitDetails;
  }

  public void setUserCreditLimitDetails(List<UserCreditLimitDetails> userCreditLimitDetails) {
    this.userCreditLimitDetails = userCreditLimitDetails;
  }

  public List<TransactionDetails> getTransactionDetails() {
    return transactionDetails;
  }

  public void setTransactionDetails(List<TransactionDetails> transactionDetails) {
    this.transactionDetails = transactionDetails;
  }

}
