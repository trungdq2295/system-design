package com.trungnguyen.locationcaptureservice.configuration;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class MySqlTestConfiguration {

    @Bean
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer() throws InterruptedException {
        return new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
                .withInitScript("mysql.sql")
                .withDatabaseName("business")
                .withUsername("root")
                .withPassword("my-secret-pw");
    }
}
