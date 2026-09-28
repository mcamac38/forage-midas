# Midas Core — JPMorgan Chase Forage Software Engineering Project

Midas Core is a Java/Spring Boot transaction-processing application completed as part of the **JPMorgan Chase & Co. Advanced Software Engineering program on Forage**.

> **Repository Attribution:** This repository is a fork of the starter project provided by JPMorgan Chase & Co. through Forage. The original project structure, domain models, test suite, helper utilities, test data, and supporting Incentive API were provided as part of the simulation.  
>
> My work focused on completing the required application functionality, including Kafka transaction processing, validation and balance updates, database persistence, Incentive API integration, and a REST balance endpoint.

## Project Overview

The Midas Core application processes financial transactions received through Apache Kafka.

For each transaction, the application:

1. Receives transaction data from the `trader-updates` Kafka topic.
2. Looks up the sender and recipient in the database.
3. Validates that both users exist.
4. Verifies that the sender has sufficient funds.
5. Sends the valid transaction to the provided Incentive API.
6. Applies the returned incentive to the recipient's balance.
7. Updates both account balances.
8. Persists the completed transaction.
9. Exposes user balance information through a REST endpoint.

## Architecture

```text
Kafka Topic: trader-updates
          |
          v
  TransactionListener
          |
          v
 Validate Transaction
   |            |
 Invalid      Valid
   |            |
 Ignore         v
          Incentive API
        localhost:8080
               |
               v
        Calculate Balances
               |
               v
        DatabaseConduit
          /          \
         v            v
   User Records   Transaction Record
         |
         v
     H2 Database

GET /balance?userId={id}
         |
         v
 BalanceController
         |
         v
   Current Balance
```

## My Contributions

### Kafka Transaction Processing

Implemented `TransactionListener` to consume transaction messages from the configured Kafka topic using Spring Kafka.

The listener:

- Retrieves the sender and recipient from the database.
- Rejects transactions when either user does not exist.
- Rejects transactions when the sender does not have sufficient funds.
- Processes valid transactions through the Incentive API.
- Updates sender and recipient balances.
- Persists the completed transaction.

### Transaction Persistence

Created the persistence layer for processed transactions using Spring Data JPA.

Implemented:

- `TransactionRecord` as a JPA entity.
- Sender and recipient relationships to existing user records.
- Transaction amount and incentive fields.
- `TransactionRepository` using Spring Data `CrudRepository`.
- Additional `DatabaseConduit` functionality for transaction storage and user lookup.

### Incentive API Integration

Implemented `IncentiveQuerier` using Spring's `RestTemplate`.

Valid transactions are sent to the provided Incentive API:

```text
POST http://localhost:8080/incentive
```

The returned incentive amount is added to the recipient's balance and stored with the completed transaction.

I also created the `Incentive` model used to deserialize the API response.

### Balance REST API

Implemented `BalanceController`, providing:

```text
GET /balance?userId={id}
```

The endpoint retrieves the current balance for the requested user and returns a balance of zero when the user does not exist.

### Application Configuration

Configured the application for Kafka transaction processing in `application.yml`, including:

- Kafka topic: `trader-updates`
- Consumer group: `midas-core`
- JSON serialization and deserialization
- Consumer offset behavior
- Application server configuration

### Maven Dependencies

Expanded `pom.xml` with the dependencies required for the completed application, including:

- Spring Boot Data JPA
- Spring Boot Web
- Spring Kafka
- H2 Database
- Spring Boot Test
- Spring Kafka Test
- Testcontainers Kafka

## Contribution Breakdown

| File | Contribution |
|---|---|
| `pom.xml` | Modified starter file — added required application and testing dependencies |
| `application.yml` | Modified starter file — added Kafka and server configuration |
| `DatabaseConduit.java` | Modified starter file — added transaction persistence and user lookup |
| `TransactionListener.java` | Added — Kafka consumer and transaction-processing logic |
| `IncentiveQuerier.java` | Added — Incentive API integration |
| `BalanceController.java` | Added — REST balance endpoint |
| `TransactionRecord.java` | Added — persisted transaction entity |
| `TransactionRepository.java` | Added — Spring Data transaction repository |
| `Incentive.java` | Added — Incentive API response model |

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Kafka**
- **Spring Data JPA**
- **Spring Web**
- **Apache Kafka**
- **H2 Database**
- **Maven**
- **REST APIs**
- **JUnit / Spring Boot Test**
- **Testcontainers**

## Key Processing Flow

A valid transaction follows this path:

```text
Transaction received
        |
        v
Sender exists? ------ No -----> Reject
        |
       Yes
        |
        v
Recipient exists? --- No -----> Reject
        |
       Yes
        |
        v
Sufficient funds? --- No -----> Reject
        |
       Yes
        |
        v
Query Incentive API
        |
        v
Debit sender
        |
        v
Credit recipient
(transaction + incentive)
        |
        v
Persist users
        |
        v
Persist transaction
```

## Provided Starter Components

The Forage/JPMorgan Chase starter repository provided the foundation used to complete the simulation, including existing application structure, domain objects, repositories, testing infrastructure, helper utilities, test datasets, and the supporting Incentive API service.

Examples of provided components include:

- `MidasCoreApplication`
- `UserRecord`
- `Transaction`
- `Balance`
- `UserRepository`
- `KafkaProducer`
- `BalanceQuerier`
- Task test classes
- Test data files
- Incentive API service

These components were used as the foundation for the functionality described in **My Contributions** above.

## What I Practiced

This project provided hands-on experience working within an existing Java codebase and implementing functionality against defined requirements and automated tests.

The project involved:

- Event-driven transaction processing
- Kafka producers and consumers
- Business-rule validation
- REST API consumption
- REST endpoint development
- Relational persistence with JPA
- Entity relationships
- Dependency management with Maven
- Spring dependency injection
- Debugging and integration testing

## Acknowledgment

This project was completed through the **JPMorgan Chase & Co. Advanced Software Engineering program on Forage**. The repository contains starter code and project materials provided for the simulation alongside the implementation work I completed while progressing through the assigned tasks.