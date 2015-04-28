package de.codecentric.soap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.soap.controller.WeatherServiceController;
import de.codecentric.soap.transformation.WeatherRepository;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public WeatherServiceController weatherServiceController() {
		return new WeatherServiceController();
	}
	
	@Bean
	public WeatherRepository weatherRepository() {
		return new WeatherRepository();
	}
}
