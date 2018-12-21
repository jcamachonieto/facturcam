package com.efactura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.efactura"}) 
public class Efactura {

	public static void main(String[] args) {
		SpringApplication.run(Efactura.class, args);
	}

}

