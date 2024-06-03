## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [System Design](#system-design)
* [Set up](#set-up)
* [API Endpoint](#api-endpoint)
* [Testing Solution](#testing-solution)

## General info
**Youtube System**

A system aims to provide user where user can watch and upload their video

## Technologies
* Java 17
* Springboot 3.1.10
* Elasticsearch
* MongoDb
* Kafka
* Amazon S3
* Amazon CloudFront
* TestContainer
* Maven

## System Design
![System Design](https://github.com/trungdq2295/system-design/blob/main/youtube-system/systemn.png)

## Set up
* You can use docker-compose.yml which is located in setup/mongodb-kafka-base to set up the configuration
* Remember to add configuration from source_configuration and sink_configuration to sync the data between mongodb and elasticsearch through kafka connect
* Source: https://www.mongodb.com/docs/kafka-connector/current/quick-start/

## API Endpoint


## Testing Solution
* Unit Test - JUnit 5
* Integration Test - JUnit 5 and TestContainers
* Load Test - JMeter



