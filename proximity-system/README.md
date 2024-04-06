## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [System Design](#system-design)
* [Set up](#set-up)
* [API Endpoint](#api-endpoint)
* [Testing Solution](#testing-solution)

## General info
**Proximity System**

A system aims to help user to find the nearest business shop they need

## Technologies
* Java 17
* Springboot 3.1.10
* Elasticsearch
* MySql
* Cassandra
* TestContainer
* Maven

## System Design
![System Design](https://github.com/trungdq2295/system-design/blob/main/proximity-system/proximity.png)

## Set up
* You can use docker to set up the database ( this is not requirement, just an optional)
* Run Cassandra in docker
> docker run -p 9042:9042 --name cassandra-spring-boot -d cassandra:latest
* Run MySQL in docker
> docker run -p 3306:3306 --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:latest

## API Endpoint

[//]: # (For now, I will focus on these endpoint only. For more information, you can start process-order-service and view http://localhost:8083/swagger-ui/index.html)

[//]: # (|Endpoint|Method|Description|)

[//]: # (|---|---|---|)

[//]: # (|/v1/test|GET|Endpoint to check if service is available|)

[//]: # (|/v1/link-shorten|POST|Shorten a long url and return a short url|)


## Testing Solution
* Unit Test - JUnit 5
* Integration Test - JUnit 5 and TestContainers
* Load Test - JMeter



