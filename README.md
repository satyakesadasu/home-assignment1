# ASSIGNMENT 1
---------
Write a rest API for identifying the number of rejected booking
transactions
To make a successful booking transaction, the user needs to have enough balance in the
account. Your application will be provided with a list of transactions, and the API should return a
list of failed transactions. You will also be provided with the initial credit limit which the users
have. When the available limit exceeds the booking cost, the transaction cannot be successful.
The transactions should be processed in the same order as they appear as part of the supplied
input. The use case described above has not been described 100% but has enough details. You
can make assumptions where needed, but please mention your assumptions as part of a
readme.

Input data:
The input consists of four fields separated with a comma(,).
1. The first field is the combination of first name and last name separated by a comma (,)
2. The second field is the email id of the user.
3. The third field is the cost required to perform the current transaction.
4. The fourth field is the transaction id.
Your application should be able to process data in the following format. Please note that the
example below is just a sample.
"John,Doe,john@doe.com,190,TR0001"
"John,Doe1,john@doe1.com,200,TR0001"
"John,Doe2,john@doe2.com,201,TR0003"
"John,Doe,john@doe.com,9,TR0004"
"John,Doe,john@doe.com,2,TR0005"

Output data:
The REST API should return data in the following format.
{
"Rejected Transactions":[
{
"First Name":"John",
"Last Name":"Doe2",
"Email Id":"john@doe2.com",
"Transaction Number":"TR0003"
},
{
"First Name":"John",
"Last Name":"Doe",
"Email Id":"john@doe.com",
"Transaction Number":"TR0005"
}
]
}
---------

## Technologies Used:

1. Spring boot 2 with spring webflux
2. Maven as build tool
3. Junit for unit test cases
4. Git for Version Control
5. Deployed in Docker Container

## Assumptions

1. Initial credit limit for each user is sent as a list in a request along with the transaction
   details. Assuming each user is having unique email id. The input consists of list of string
   fields separated with a comma(,). The first field is email id and second field is the initial
   credit limit of the user.

         "e-mail id","credit limit" 
         "john@doe.com,5000",
         "john@doe1.com,6000",
         "john@doe2.com,7000"
2. Assuming if no credit limit is given , we are processing the request considering the user credit
   limit as zero and adding them in rejected transactions

3.Assuming input request will be as below.

    {
      "userCreditLimitDetails": [
	        "john@doe.com,5000",
	        "john@doe1.com,0",
	        "john@doe2.com,0"
      ],
      "transactionDetails": [ 
         "John,Doe,john@doe.com,190,TR0001",
         "John,Doe1,john@doe1.com,200,TR0001",
         "John,Doe2,john@doe2.com,201,TR0003",
         "John,Doe,john@doe.com,9,TR0004",
         "John,Doe,john@doe.com,2,TR0005"	 
      ]
    }

## Rest API Services:

### 1. To get the list of Rejected Transactions

### Description:

This rest API is for identifying the number of rejected booking transactions which returns an object
containing list of Rejected Transaction Details where response is as expected Object

`POST http://localhost:8080/booking/rejectedTransactionsAsList`

### Request:

    {
      "userCreditLimitDetails": [
	        "john@doe.com,5000",
	        "john@doe1.com,0",
	        "john@doe2.com,0"
      ],
      "transactionDetails": [ 
         "John,Doe,john@doe.com,190,TR0001",
         "John,Doe1,john@doe1.com,200,TR0001",
         "John,Doe2,john@doe2.com,201,TR0003",
         "John,Doe,john@doe.com,9,TR0004",
         "John,Doe,john@doe.com,2,TR0005"	 
      ]
    }

#### Response:

      {
         "Rejected Transactions": [
            {
               "First Name": "John",
               "Last Name": "Doe1",
               "Email Id": "john@doe1.com",
               "Transaction Number": "TR0002"
            },
            {
               "First Name": "John",
               "Last Name": "Doe2",
               "Email Id": "john@doe2.com",
               "Transaction Number": "TR0003"
            }
         ]
      }

### 2. To get the list of Rejected Transactions as Flux(Streaming of response)

### Description:

This rest API is for identifying the number of rejected booking transactions which returns an object
containing list of Rejected Transaction Details where response is processed and sent as an object

This is same as the above api, but we are using Webflux for this api

`POST http://localhost:8080/booking/rejectedTransactionsAsFlux`

#### Request:

    {
      "userCreditLimitDetails": [
	        "john@doe.com,5000",
	        "john@doe1.com,0",
	        "john@doe2.com,0"
      ],
      "transactionDetails": [ 
         "John,Doe,john@doe.com,190,TR0001",
         "John,Doe1,john@doe1.com,200,TR0001",
         "John,Doe2,john@doe2.com,201,TR0003",
         "John,Doe,john@doe.com,9,TR0004",
         "John,Doe,john@doe.com,2,TR0005"	 
      ]
    }

#### Response:

      {
         "First Name": "John",
         "Last Name": "Doe1",
         "Email Id": "john@doe1.com",
         "Transaction Number": "TR0002"
      }
      {
         "First Name": "John",
         "Last Name": "Doe2",
         "Email Id": "john@doe2.com",
         "Transaction Number": "TR0003"
      }

---

## How to run the Application

We have used Docker for running the application in docker container, following steps will create the
application as jar file and run in the Docker container.

#### Pre Requisite

* Since Docker file is created from base package from java 1.8 make sure Java 8 is installed in the
  machine
* Make sure Docker is installed
* Run all the below step from the terminal of the root directory of the application package
* Make sure port 8080 is free , if not please change the port number in 4th step

##### Step by step process to run a jar file in docker container

1. Once repo is fetched from GIT run mvn clean install from root directory to make sure .jar file is
   created in the target folder
2. run `docker build -t homeassnapp .` to build the image of the service using jar file form root
   directory
3. run `docker image ls` to make sure homeassnapp image file is created
4. run `docker run -p8080:8080 homeassnapp` this command fetches the image file and run it in the
   docker container in the 8080 port
5. Test the rest api as given above with sample request format and verify the response
6. run `docker ps` to get the CONTAINER ID
7. run `docker container stop CONTAINER ID` to stop the docker container

##### Commands to run in Terminal in root directory of the package

```docker
docker build -t homeassnapp .       
docker image ls                   
docker run -p8080:8080 homeassnapp  
docker ps                         
docker container stop 1235AHSY54Y 
```

---

## Additional Rest APIs

### 1. To get the list of Rejected Transactions (Request is an Object)

### Description:

This rest API is for identifying the number of rejected booking transactions which returns an object
containing an object of Rejected Transaction Details, this is the approach where we can see the
request as object and how we are processing them in the service layer and sending the expected
response.

`POST http://localhost:8080/booking/rejectedTransactionsAsObject`

### Request:

      {
         "userCreditLimitDetails": [
            {
               "emailId": "john@doe.com",
               "creditLimit": 191 
            }, 
            {
               "emailId": "john@doe1.com",
               "creditLimit": 1 
            }
         ],
         "transactionDetails": [
            {
               "First Name": "John",
               "Last Name": "Doe",
               "Email Id": "john@doe.com",
               "transactionCost": 190,
               "Transaction Number": "TR0001"
            },
            {
               "First Name": "John",
               "Last Name": "Doe1",
               "Email Id": "john@doe1.com",
               "transactionCost": 200,
               "Transaction Number": "TR0002"
            },
            {
               "First Name": "John",
               "Last Name": "Doe",
               "Email Id": "john@doe.com",
               "transactionCost": 201,
               "Transaction Number": "TR0003"
            }
         ]
      }

#### Response:

      {
         "First Name": "John",
         "Last Name": "Doe1",
         "Email Id": "john@doe1.com",
         "Transaction Number": "TR0002"
      }
      {
         "First Name": "John",
         "Last Name": "Doe2",
         "Email Id": "john@doe.com",
         "Transaction Number": "TR0003"
      }

### 2. To get the list of Rejected Transactions (To visualize the streaming of response with 1 sec delay)

### Description:

This rest API is for identifying the number of rejected booking transactions which returns an object
containing an object of Rejected Transaction Details, this is the approach where we can see the
request as object and how we are processing them in the service layer and sending the expected
response.

Here Request is hard coded and when run in chrome we can see the output with 1 sec delay

`GET http://localhost:8080/booking/rejectedTransactionsHardCodedData`

#### Response:

      {"First Name":"John","Last Name":"Doe","Email Id":"john@doe.com","Transaction Number":"TR0001"}
      {"First Name":"John","Last Name":"Doe1","Email Id":"john@doe1.com","Transaction Number":"TR0002"}
      {"First Name":"John","Last Name":"Doe2","Email Id":"john@doe2.com","Transaction Number":"TR0003"}
      {"First Name":"John","Last Name":"Doe","Email Id":"john@doe.com","Transaction Number":"TR0004"}
      {"First Name":"John","Last Name":"Doe","Email Id":"john@doe.com","Transaction Number":"TR0005"}

---

## To view the Rest API in Swagger format

Please refer to the yaml file in infrastructure/src/main/resource/rejectedTransactionaApi.yml in
[Swagger Editor View](https://editor.swagger.io/)

---

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.0/maven-plugin/reference/html/#build-image)
* [Docker documentation](https://docs.docker.com/get-started/overview/)  


# home-assignment1
