package com.efactura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.efactura"}) 
@EntityScan("com.efactura.*") 
public class Efactura extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Efactura.class, args);
	}

}

