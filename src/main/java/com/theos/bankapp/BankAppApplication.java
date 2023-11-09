package com.theos.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Bank Application",
		description = "Backend Rest APIs for Bank",
		version = "v1.0"
	),
	externalDocs = @ExternalDocumentation(
		description = "Bank App Documentaiton",
		url = "https://github.com/the0ss/bank-application"
	)
)
@EnableCaching
public class BankAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAppApplication.class, args);
	}

}
