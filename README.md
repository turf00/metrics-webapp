# Metrics WebApp

[![Build Status](https://travis-ci.com/turf00/metrics-webapp.svg?branch=master)](https://travis-ci.org/turf00/metrics-webapp)
[![codecov](https://codecov.io/gh/turf00/metrics-webapp/branch/master/graph/badge.svg)](https://codecov.io/gh/turf00/metrics-webapp)

## What

An example Spring Boot application showing the capturing of metrics related to HTTP requests.

## Why

This section contains some information on design considerations.

### General

+ Maven was used over Gradle as I have more experience with Maven.  Although for any new projects I would prefer to use Gradle.
+ I was forced to use Junit 4 rather than Junit 5 as code coverage did not appear to work correctly with Jacoco and Junit 5.
+ I used Spring Boot and Spring Web MVC for implementing the APIs that we are going to capture metrics on.
+ Mustache was used as a simple templating language for the very basic UI pages to fetch metrics.
+ I created some RESTful endpoints rather than pages as I am more familiar with that.  Most Web UI experience I have is with React and JS.

### Metric Capture

I created three methods of capturing the metrics:

+ Filter ()

+ In order to remove the need for shared state when possible and to avoid delaying the response back to the client for metric capture overhead, the 

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

## Future Work

+ Dockerise the application and the build.
+ The consumer that parses the incoming metrics to produce the stats, etc should handle parsing multiple metrics at a single time.  This would involve using smart batching, i.e. batching by count and time.

