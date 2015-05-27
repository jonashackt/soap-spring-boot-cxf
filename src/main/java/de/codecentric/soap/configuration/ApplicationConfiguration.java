package de.codecentric.soap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.controller.WeatherServiceController;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public WeatherServiceController weatherServiceController() {
		return new WeatherServiceController();
	}
	
	@Bean
	public WeatherBackend weatherRepository() {
		return new WeatherBackend();
	}
}
