# Yoga

## Description

This project is a full-stack application providing a reservation system for a yoga studio.

## Prerequisites

- Node.js 20.13.1
- Angular CLI 14.1.0
- Java JDK 1.8
- Maven 3.9.9
- MySQL 8.0.39
- Postman 11.12.0

## Start the project

Git clone:

> git clone https://github.com/tetho/NumDev

### Start front-end

Go inside folder:

> cd front

Install dependencies:

> npm install

Launch front-end:

> npm run start

### Start back-end

Go inside folder:

> cd back

Install dependencies:

> mvn clean install

Launch back-end:

> mvn spring-boot:run

## Resources

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman

### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

### Front-end tests

#### E2E

Launching e2e test:

> npm run e2e

Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report is available here:

> front/coverage/lcov-report/index.html

#### Unit and integration tests

Launching test:

> npm run test

for following change:

> npm run test:watch

Report is available here:

> front/coverage/jest/lcov-report/index.html

### Back-end tests

#### Unit and integration tests

Launching test:

> mvn clean verify

Report is available here:

> back/target/site/jacoco/index.html

