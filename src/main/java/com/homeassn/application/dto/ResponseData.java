package com.homeassn.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ResponseData {

  @JsonProperty("Rejected Transactions")
  private List<TransactionDetails> rejectedTransactions;

  public ResponseData(List<TransactionDetails> rejectedTransactions) {
    this.rejectedTransactions = rejectedTransactions;
  }

  public List<TransactionDetails> getRejectedTransactions() {
    return rejectedTransactions;
  }

  public void setRejectedTransactions(List<TransactionDetails> rejectedTransactions) {
    this.rejectedTransactions = rejectedTransactions;
  }

}
