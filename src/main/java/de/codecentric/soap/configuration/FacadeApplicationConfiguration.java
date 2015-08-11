package de.codecentric.soap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.codecentric.soap.controller.WeatherServiceController;
import de.codecentric.soap.controller.WeatherServiceControllerFacade;

@Configuration
@Profile("facade")
public class FacadeApplicationConfiguration {

	@Bean
	public WeatherServiceController weatherServiceController() {
		return new WeatherServiceControllerFacade();
	}
}
