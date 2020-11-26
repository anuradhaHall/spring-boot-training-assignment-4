package com.wipro.assignment;


import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class Assignment4Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment4Application.class, args);
	}

	@Bean
	public Docket swaggerConfiguration(){
		return new Docket(DocumentationType.SWAGGER_2)
		.select()
		.apis(RequestHandlerSelectors.basePackage("com.wipro.assignment"))
		.paths(PathSelectors.ant("/users/**"))
		.build()
		.apiInfo(apiDetails());
	}

	private ApiInfo apiDetails(){
		return new ApiInfo(
			"To Do API", 
			"Assignment 4: making a to do app with testing and swagger", 
			"1.0", 
			null, 
			new Contact("Anuradha Hall", "", "anuradha.hall@wipro.com"), 
			"Free to use", 
			null,
			Collections.emptyList()
		);
	}

}
