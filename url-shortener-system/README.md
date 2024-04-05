## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [System Design](#system-design)
* [API Endpoint](#api-endpoint)
* [Testing Solution](#testing-solution)

## General info
**URL Shortener System**

A system aims to help user to shorten the link so it will easier to send
## Technologies
* Java 17
* Springboot 3.1.10
* MongoDB
* Redis
* TestContainer
* Maven

## System Design
![System Design](https://github.com/trungdq2295/system-design/blob/main/url-shortener-system/system.png)

## API Endpoint
For now, I will focus on these endpoint only
|Endpoint|Method|Description|
|---|---|---|
|/v1/test|GET|Endpoint to check if service is available|
|/v1/link-shorten|POST|Shorten a long url and return a short url|

## Testing Solution
* Unit Test - JUnit 5
* Integration Test - JUnit 5 and TestContainers
* Load Test - JMeter



