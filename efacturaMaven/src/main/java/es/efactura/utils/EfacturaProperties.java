package es.efactura.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@PropertySource("classpath:config.properties")
public class EfacturaProperties {

	@Value("${databaseFile}")
	@Getter
	private String databaseFile;

	@Value("${driverManager}")
	@Getter
	private String driverManager;

}
