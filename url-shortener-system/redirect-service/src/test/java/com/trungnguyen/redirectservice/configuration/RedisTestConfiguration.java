package com.trungnguyen.redirectservice.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@TestConfiguration(proxyBeanMethods = false)
public class RedisTestConfiguration {

	@Bean
	public GenericContainer<?> redisContainer() {
		GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
				.withExposedPorts(6379)
				.waitingFor(Wait.forListeningPort());

		redisContainer.start();
		return redisContainer;
	}
}
