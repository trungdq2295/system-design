package com.trungnguyen.synchornizedataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SynchronizeDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SynchronizeDataServiceApplication.class, args);
	}

}
