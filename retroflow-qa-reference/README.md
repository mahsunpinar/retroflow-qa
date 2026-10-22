# RetroFlow

Backend API for StackCraft's internal retrospective tool. Replaces the mix of sticky notes,
Miro boards, and shared Google Docs the 12 dev teams currently use to run retros.

## Tech stack

- Java 21
- Spring Boot 3
- Maven
- H2 (in-memory — no persistence needed between restarts)

## Getting started

```
mvn spring-boot:run
```

The API is then available at http://localhost:8080. The H2 console (if you want to poke at
the data directly) is at http://localhost:8080/h2-console.

## Running tests

```
mvn test
```

## Status

First version. Core domain model and endpoints for teams, retrospectives, feedback items, and
action items are implemented. Not yet reviewed by QA — that review is what this handover is
for.
