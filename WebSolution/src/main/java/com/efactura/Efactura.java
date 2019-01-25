package com.efactura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.efactura", "com.efactura.user.model"})
@EnableJpaRepositories(basePackages ={ "com.efactura.user.service"})
@EntityScan(basePackages ={ "com.efactura.bill.model", "com.efactura.client.model", "com.efactura.user.model"})
public class Efactura extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Efactura.class, args);
	}

}

