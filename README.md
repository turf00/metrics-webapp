# Metrics WebApp

[![Build Status](https://travis-ci.com/turf00/metrics-webapp.svg?branch=master)](https://travis-ci.org/turf00/metrics-webapp)
[![codecov](https://codecov.io/gh/turf00/metrics-webapp/branch/master/graph/badge.svg)](https://codecov.io/gh/turf00/metrics-webapp)

## What

An example Spring Boot application showing the capturing of metrics related to HTTP requests.



## Why

This section contains some information on design considerations.

+ Maven was used over Gradle as the build tool as I have more experience with Maven.  Although for any new projects I would prefer to use Gradle.
+ I was forced to use Junit 4 rather than Junit 5 as code coverage did not appear to work correctly with Jacoco and Junit 5.

## Test APIs

Rather than pages I've added RESTful endpoints to the application as I am more familiar with these and haven't done a lot of web UI work in Java.

The list of endpoints available can be found in the Swagger docs here: http://localhost:8080/swagger-ui.html#

The general APIs represent the Accounts and Transactions for a bank.

## Using the app

### Building the app

You can build the app with Maven (3.6.0+) and Java 8 installed.  The following should be executed in the root of the project:

`mvn clean package`

### Running the app

You can start the app from the command line as follows in the root of the project:

`java -jar contrast-example-webapp/target/contrast-example-webapp-0.1-SNAPSHOT.jar`

### Application configuration

There are a number of configuration settings which can be adjusted from the command line:

