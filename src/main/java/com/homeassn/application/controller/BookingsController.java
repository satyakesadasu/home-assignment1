package com.homeassn.application.controller;


import com.homeassn.application.dto.ResponseData;
import com.homeassn.application.dto.TransactionDetails;
import com.homeassn.application.dto.TransactionDetailsRequest;
import com.homeassn.application.dto.TransactionsRequest;
import com.homeassn.application.service.BookingsService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/booking")
@Validated
public class BookingsController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

  @Autowired
  BookingsService bookingsService;

  @PostMapping("/rejectedTransactionsAsList")
  public ResponseEntity<ResponseData> rejectedTransactionDetailsWithoutFlux(
      @RequestBody TransactionDetailsRequest transactionDetailsRequest) {
    return new ResponseEntity<>(
        bookingsService.rejectedTransactionDetailsAsList(transactionDetailsRequest),
        HttpStatus.OK);
  }

  @PostMapping(value = "/rejectedTransactionsAsFlux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public ResponseEntity<Flux<TransactionDetails>> rejectedTransactionDetails(
      @Valid @RequestBody TransactionDetailsRequest transactionDetailsRequest) {
    LOGGER.info("Received transactions request {}",
        transactionDetailsRequest);
    return new ResponseEntity<>(
        bookingsService.rejectedTransactionDetails(transactionDetailsRequest),
        HttpStatus.OK);
  }

  /*
  This Rest API is to get the request body as Object and process the data
   */

  @PostMapping(value = "/rejectedTransactionsAsObject", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public ResponseEntity<Flux<TransactionDetails>> rejectedTransactionDetailsinJsonFormat(
      @RequestBody TransactionsRequest transactionsRequest) {
    LOGGER.debug("Recieved Transaction request {}", transactionsRequest);
    return new ResponseEntity<>(
        bookingsService.rejectedTransactionDetailsObjectFormat(transactionsRequest),
        HttpStatus.OK);
  }

  /*
  To see the transaction with 1 sec delay using Flux
  Once the application in up and running git the below Rest Point in Chrome to see the
  transactions with 1 sec delay "http://localhost:8080/rejectedTransactionDetailsHardCodedData"
   */
  @GetMapping(value = "/rejectedTransactionsHardCodedData", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public ResponseEntity<Flux<TransactionDetails>> rejectedTransactionDetailsin() {
    return new ResponseEntity<>(bookingsService.rejectedTransactionDetailsWithDataHardCoding(),
        HttpStatus.OK);
  }
}
