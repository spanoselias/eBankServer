# Transfer Money Server

## Description
Restful APIs that support the creation of bank accounts and the transfer 
of money between the accounts. Multi-currency is supported.

## Requires
* Java 11
* Gradle 5 and above

## How to run
Retrieve the server from the git
    gradle clean build

To run the server:
    java -jar build/libs/core-bank-bankV1.0.jar
    
## API Structure    
    {
            "id": number,
            "ownerName": string,
            "balance": double,
            "currency": string ("EUR", "USD", "GBP")
    }    

##### GET /bankAccount/{id}
###### Response:
    {
      "ownerName": "Elias Spanos",
      "balance": 1000.5,
      "currency": "GBP",
      "accountId": 1
    }

The below api created a new bank account and return the id for the 
created account.

##### POST /bankAccount
###### Json Input:
    {
        "ownerName": "Elias Andreou",
        "balance": 12.6,
        "currency": "RUB"
    }
###### Reponse:
    {
      "bankAccountIdCreated": 4
    }
    
##### POST bankaccount/transfer?fromId=1&toId=2&moneyToTranfer=0.1
###### Reponse:
    {
      "fromBankAccountTrasactionId": 1,
      "toBankAccountTrasactionId": 2
    }
    
##### GET bankAccount/{transactionId}/transactions
###### Response:
    [
      {
        "sourceBankAccountId": 1,
        "destinationBankAccountId": 2,
        "amount": 0.2,
        "currency": "GBP",
        "dateCreated": "2019-09-29T18:30:26",
        "status": "Approved",
        "trasactionType": "WITHDRAWAL",
        "transactionId": 3
      },
      {
        "sourceBankAccountId": 1,
        "destinationBankAccountId": 2,
        "amount": 0.1,
        "currency": "GBP",
        "dateCreated": "2019-09-29T18:27:19",
        "status": "Approved",
        "trasactionType": "WITHDRAWAL",
        "transactionId": 1
      }
    ]
    
##### Examples for calling the APIS: 
    GET: http://localhost:8080/bankaccount/1
    POST: http://localhost:8080/bankaccount/transfer?fromId=1&toId=2&moneyToTranfer=0.2
    GET: http://localhost:8080/bankaccount/1/transactions
    POST: http://localhost:8080/bankaccount/add
        Body: {
                  "ownerName": "Andreas",
                  "balance": 200.0,
                  "currency": "USD"
              }
    