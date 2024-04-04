package com.trungnguyen.redirectservice.configuration;

import com.trungnguyen.redirectservice.RedirectServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class MongoDBTestConfiguration {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    }

    public static void main(String[] args) {
        SpringApplication.from(RedirectServiceApplication::main).with(MongoDBTestConfiguration.class).run(args);
    }

}
