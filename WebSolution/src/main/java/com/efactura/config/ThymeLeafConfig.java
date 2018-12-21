package com.efactura.config;

import org.springframework.context.annotation.Bean;

import nz.net.ultraq.thymeleaf.LayoutDialect;

public class ThymeLeafConfig {

	@Bean
	public LayoutDialect layoutDialect() {
	    return new LayoutDialect();
	}

}
