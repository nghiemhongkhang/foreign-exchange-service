package com.nhk.fx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForeignExchangeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForeignExchangeServiceApplication.class, args);
	}

}
