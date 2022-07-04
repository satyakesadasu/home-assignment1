package com.homeassn.application.service;

import com.homeassn.application.dto.ResponseData;
import com.homeassn.application.dto.TransactionDetails;
import com.homeassn.application.dto.TransactionDetailsRequest;
import com.homeassn.application.dto.TransactionsRequest;
import com.homeassn.application.dto.UserCreditLimitDetails;
import com.homeassn.application.exception.InvalidDataException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingsServiceImpl implements BookingsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookingsServiceImpl.class);

  private static final String EMPTY_TRANSACTION_DETAILS_ERROR_MESSAGE = "User transaction details should not be empty or null";
  private static final String EMPTY_REQUEST_ERROR_MESSAGE = "Input request should not be null";

  @Override
  public Flux<TransactionDetails> rejectedTransactionDetails(
      TransactionDetailsRequest transactionDetailsRequest) {
    LOGGER.info("Request reached service rejectedTransactionDetails");

    if (null == transactionDetailsRequest) {
      throw new InvalidDataException(EMPTY_REQUEST_ERROR_MESSAGE);
    }

    if (CollectionUtils.isEmpty(transactionDetailsRequest.getTransactionDetails())) {
      throw new InvalidDataException(EMPTY_TRANSACTION_DETAILS_ERROR_MESSAGE);
    }

    Mono<List<TransactionDetails>> just = Mono
        .just(getTransactionDetails(transactionDetailsRequest));
    Flux<TransactionDetails> flatMapMany = just.flatMapMany(Flux::fromIterable);
    return flatMapMany.log();

  }

  private List<TransactionDetails> getTransactionDetails(
      TransactionDetailsRequest transactionDetailsRequest) {
    List<TransactionDetails> rejectedTransactions = new LinkedList<>();
    // assuming email id of the users are unique for each user
    Map<String, Integer> userCreditLimitMap = new HashMap<>();
    try {
      // this will create a map of user email id and credit limit
      if (CollectionUtils.isEmpty(transactionDetailsRequest.getUserCreditLimitDetails())) {
        transactionDetailsRequest.setUserCreditLimitDetails(new ArrayList<>());
      }
      transactionDetailsRequest.getUserCreditLimitDetails().stream().forEach(p -> {
        String[] userCreditLimit = p.split(",");
        userCreditLimitMap.put(userCreditLimit[0], Integer.parseInt(userCreditLimit[1]));
      });

      // iterates each transaction and checks if the credit limit is exceeded or not
      transactionDetailsRequest.getTransactionDetails().stream().forEach(transaction -> {
        String[] transactionInfo = transaction.split(",");
        String firstName = transactionInfo[0];
        String lastName = transactionInfo[1];
        String emailId = transactionInfo[2];
        int transactionCost = Integer.parseInt(transactionInfo[3]);
        String transactionId = transactionInfo[4];
        if (userCreditLimitMap.keySet().contains(emailId)) {
          int creditLimit = userCreditLimitMap.get(emailId);
          if (creditLimit >= transactionCost) {
            userCreditLimitMap.put(emailId, creditLimit - transactionCost);
          } else {
            rejectedTransactions
                .add(new TransactionDetails(firstName, lastName, emailId, transactionId));
          }
        } else {
          rejectedTransactions
              .add(new TransactionDetails(firstName, lastName, emailId, transactionId));
        }
      });

      return rejectedTransactions;

    } catch (ArrayIndexOutOfBoundsException e) {
      throw new InvalidDataException("Invalid Input request ");
    } catch (Exception e) {
      throw new InvalidDataException("Error while processing the request " + e.getMessage());
    }
  }

  @Override
  public ResponseData rejectedTransactionDetailsAsList(
      TransactionDetailsRequest transactionDetailsRequest) {

    LOGGER.info("Request reached service rejectedTransactionDetails");
    if (null == transactionDetailsRequest) {
      throw new InvalidDataException(EMPTY_REQUEST_ERROR_MESSAGE);
    }
    if (CollectionUtils.isEmpty(transactionDetailsRequest.getUserCreditLimitDetails())) {
      transactionDetailsRequest.setUserCreditLimitDetails(new ArrayList<>());

    }

    if (CollectionUtils.isEmpty(transactionDetailsRequest.getTransactionDetails())) {
      throw new InvalidDataException(EMPTY_TRANSACTION_DETAILS_ERROR_MESSAGE);
    }

    return new ResponseData(getTransactionDetails(transactionDetailsRequest));

  }

  private TransactionDetailsRequest transactionDetailsRequest() {
    TransactionDetailsRequest transactionDetailsRequest = new TransactionDetailsRequest();
    List<String> userCreditLimitDetails = new ArrayList<>(3);
    userCreditLimitDetails.add("john@doe.com,0");
    userCreditLimitDetails.add("john@doe1.com,0");
    userCreditLimitDetails.add("john@doe2.com,0");

    List<String> transactionDetails = new ArrayList<>(5);

    transactionDetails.add("John,Doe,john@doe.com,190,TR0001");
    transactionDetails.add("John,Doe1,john@doe1.com,200,TR0002");
    transactionDetails.add("John,Doe2,john@doe2.com,201,TR0003");
    transactionDetails.add("John,Doe,john@doe.com,9,TR0004");
    transactionDetails.add("John,Doe,john@doe.com,2,TR0005");

    transactionDetailsRequest.setUserCreditLimitDetails(userCreditLimitDetails);
    transactionDetailsRequest.setTransactionDetails(transactionDetails);
    return transactionDetailsRequest;
  }

  @Override
  public Flux<TransactionDetails> rejectedTransactionDetailsObjectFormat(
      TransactionsRequest transactionsRequest) {
    LOGGER.info("Request reached service rejectedTransactionDetailsObjectFormat");
    if (null == transactionsRequest) {
      throw new InvalidDataException(EMPTY_REQUEST_ERROR_MESSAGE);
    }
    if (CollectionUtils.isEmpty(transactionsRequest.getUserCreditLimitDetails())) {
      transactionsRequest.setUserCreditLimitDetails(new ArrayList<>());
    }

    if (CollectionUtils.isEmpty(transactionsRequest.getTransactionDetails())) {
      throw new InvalidDataException(EMPTY_TRANSACTION_DETAILS_ERROR_MESSAGE);
    }
    List<TransactionDetails> rejectedTransactions = new LinkedList<>();
    try {
      Map<String, Integer> userCreditLimitMap = transactionsRequest.getUserCreditLimitDetails()
          .stream().collect(
              Collectors.toMap(UserCreditLimitDetails::getEmailId,
                  UserCreditLimitDetails::getCreditLimit));

      transactionsRequest.getTransactionDetails().stream().forEach(transaction -> {
        if (userCreditLimitMap.keySet().contains(transaction.getEmailId())) {
          int creditLimit = userCreditLimitMap.get(transaction.getEmailId());
          if (creditLimit >= transaction.getTransactionCost()) {
            userCreditLimitMap.put(transaction.getEmailId(),
                creditLimit - transaction.getTransactionCost());
          } else {
            rejectedTransactions.add(transaction);
          }
        } else {
          rejectedTransactions.add(transaction);
        }
      });
      Mono<List<TransactionDetails>> just = Mono.just(rejectedTransactions);
      Flux<TransactionDetails> flatMapMany = just.flatMapMany(Flux::fromIterable);
      return flatMapMany.log();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new InvalidDataException("Invalid Input request ");
    } catch (Exception e) {
      throw new InvalidDataException("Error while processing the request " + e.getMessage());
    }
  }

  @Override
  public Flux<TransactionDetails> rejectedTransactionDetailsWithDataHardCoding() {
    TransactionDetailsRequest transactionDetailsRequest = transactionDetailsRequest();
    LOGGER.info("Request reached service");

    Mono<List<TransactionDetails>> just = Mono
        .just(getTransactionDetails(transactionDetailsRequest));
    Flux<TransactionDetails> flatMapMany = just.flatMapMany(Flux::fromIterable);
    return flatMapMany.delayElements(Duration.ofSeconds(1)).log();

  }
}
