package com.eberry.application.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.eberry.application.dto.TransactionDetails;
import com.eberry.application.dto.TransactionDetailsRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class BookingsControllerTest {

  @Autowired
  WebTestClient webTestClient;
  private static final String REJECTED_TRANSACTIONS_URI = "/booking/rejectedTransactionsAsFlux";

  //3 users and 5 transactions where credit limit is 0 for 3 users.
  @Test
  public void postServiceTestWithZeroCreditLimit() {
    EntityExchangeResult<List<TransactionDetails>> entityExchangeResult = webTestClient.post()
        .uri(REJECTED_TRANSACTIONS_URI).bodyValue(transactionDetailsRequest(0, 0, 0))
        .accept(MediaType.APPLICATION_STREAM_JSON).exchange()
        .expectStatus().isOk().expectHeader()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .expectBodyList(TransactionDetails.class)
        .returnResult();

    assertEquals(expectedResponse(), entityExchangeResult.getResponseBody());
  }

  // 3 users and 5 transactions where credit limit is 5000 for 3 users
  @Test
  public void postServiceTestWithHighCreditLimit() {
    EntityExchangeResult<List<TransactionDetails>> entityExchangeResult = webTestClient.post()
        .uri(REJECTED_TRANSACTIONS_URI).bodyValue(transactionDetailsRequest(5000, 5000, 5000))
        .accept(MediaType.APPLICATION_STREAM_JSON).exchange()
        .expectStatus().isOk().expectHeader()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .expectBodyList(TransactionDetails.class)
        .returnResult();

    assertEquals(Collections.EMPTY_LIST, entityExchangeResult.getResponseBody());
  }

  // when empty user credit details is sent as request, throwing server error
  @Test
  public void postServiceTestErrorWhenUserCreditDetailsIsEmpty() {
    TransactionDetailsRequest transactionDetailsRequest = new TransactionDetailsRequest();
    List<String> transactionDetails = new ArrayList<>(5);
    transactionDetails.add("John,Doe,john@doe.com,190,TR0001");
    transactionDetails.add("John,Doe1,john@doe1.com,200,TR0001");
    transactionDetails.add("John,Doe2,john@doe2.com,201,TR0003");
    transactionDetails.add("John,Doe,john@doe.com,9,TR0004");
    transactionDetails.add("John,Doe,john@doe.com,2,TR0005");

    transactionDetailsRequest.setUserCreditLimitDetails(null);
    transactionDetailsRequest.setTransactionDetails(transactionDetails);

    EntityExchangeResult<List<TransactionDetails>> entityExchangeResult = webTestClient.post()
        .uri(REJECTED_TRANSACTIONS_URI).bodyValue(transactionDetailsRequest)
        .accept(MediaType.APPLICATION_STREAM_JSON).exchange()
        .expectStatus().isOk().expectHeader()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .expectBodyList(TransactionDetails.class)
        .returnResult();

    assertEquals(expectedResponse(), entityExchangeResult.getResponseBody());

  }

  // when empty transaction details is sent as request, throwing Bad Request Error
  @Test
  public void postServiceTestErrorWhenTransactionDetailsIsEmpty() {
    TransactionDetailsRequest transactionDetailsRequest = new TransactionDetailsRequest();
    List<String> userCreditLimitDetails = new ArrayList<>(3);
    userCreditLimitDetails.add("john@doe.com,0");
    userCreditLimitDetails.add("john@doe1.com,0");
    userCreditLimitDetails.add("john@doe2.com,0");
    transactionDetailsRequest.setUserCreditLimitDetails(userCreditLimitDetails);

    transactionDetailsRequest.setTransactionDetails(null);
    webTestClient.post().uri(REJECTED_TRANSACTIONS_URI).bodyValue(transactionDetailsRequest)
        .accept(MediaType.APPLICATION_STREAM_JSON).exchange()
        .expectStatus().is4xxClientError();

  }

  // flux test
  @Test
  public void postServiceTestStepTest() {
    Flux<TransactionDetails> transactionDetails = webTestClient.post()
        .uri(REJECTED_TRANSACTIONS_URI)
        .bodyValue(transactionDetailsRequest(0, 0, 0)).accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON)
        .returnResult(TransactionDetails.class).getResponseBody();

    StepVerifier.create(transactionDetails).expectSubscription()
        .expectNext(new TransactionDetails("John", "Doe", "john@doe.com", "TR0001"))
        .expectNext(new TransactionDetails("John", "Doe1", "john@doe1.com", "TR0001"))
        .expectNext(new TransactionDetails("John", "Doe2", "john@doe2.com", "TR0003"))
        .expectNext(new TransactionDetails("John", "Doe", "john@doe.com", "TR0004"))
        .expectNext(new TransactionDetails("John", "Doe", "john@doe.com", "TR0005"))
        .verifyComplete();
  }

  // When Empty or null Object is sent
  @Test
  public void postServiceTestWhenEmptyRequestIsSent() {
    webTestClient.post().uri(REJECTED_TRANSACTIONS_URI).bodyValue(new TransactionDetailsRequest())
        .accept(MediaType.APPLICATION_STREAM_JSON).exchange().expectStatus().is4xxClientError();
  }

  // get service test
  @Test
  public void getServiceTest() {
    webTestClient.get().uri("/booking/rejectedTransactionsHardCodedData")
        .accept(MediaType.valueOf(String.valueOf(MediaType.APPLICATION_STREAM_JSON)))
        .exchange().expectStatus().isOk().expectHeader()
        .contentType(MediaType.valueOf(String.valueOf(MediaType.APPLICATION_STREAM_JSON)))
        .expectBodyList(TransactionDetails.class).hasSize(5);
  }

  private TransactionDetailsRequest transactionDetailsRequest(int creditLimit1, int creditLimit2,
      int creditLimit3) {
    TransactionDetailsRequest transactionDetailsRequest = new TransactionDetailsRequest();
    List<String> userCreditLimitDetails = new ArrayList<>(3);
    userCreditLimitDetails.add("john@doe.com," + creditLimit1 + "");
    userCreditLimitDetails.add("john@doe1.com," + creditLimit2 + "");
    userCreditLimitDetails.add("john@doe2.com," + creditLimit3 + "");

    List<String> transactionDetails = new ArrayList<>(5);

    transactionDetails.add("John,Doe,john@doe.com,190,TR0001");
    transactionDetails.add("John,Doe1,john@doe1.com,200,TR0001");
    transactionDetails.add("John,Doe2,john@doe2.com,201,TR0003");
    transactionDetails.add("John,Doe,john@doe.com,9,TR0004");
    transactionDetails.add("John,Doe,john@doe.com,2,TR0005");

    transactionDetailsRequest.setUserCreditLimitDetails(userCreditLimitDetails);
    transactionDetailsRequest.setTransactionDetails(transactionDetails);
    return transactionDetailsRequest;
  }

  private List<TransactionDetails> expectedResponse() {
    List<TransactionDetails> transactionDetails = new ArrayList<>();
    transactionDetails.add(new TransactionDetails("John", "Doe", "john@doe.com", "TR0001"));
    transactionDetails.add(new TransactionDetails("John", "Doe1", "john@doe1.com", "TR0001"));
    transactionDetails.add(new TransactionDetails("John", "Doe2", "john@doe2.com", "TR0003"));
    transactionDetails.add(new TransactionDetails("John", "Doe", "john@doe.com", "TR0004"));
    transactionDetails.add(new TransactionDetails("John", "Doe", "john@doe.com", "TR0005"));
    return transactionDetails;
  }
}

