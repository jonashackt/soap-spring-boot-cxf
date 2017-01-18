package de.codecentric.soap.configuration;

import java.io.IOException;

import de.codecentric.soap.controller.WeatherServiceControllerFacadeAspect;
import de.codecentric.soap.rules.Rules;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.controller.WeatherServiceController;
import de.codecentric.soap.controller.WeatherServiceControllerImpl;
import de.codecentric.soap.rules.PlausibilityChecker;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public WeatherServiceController weatherServiceController() {
		return new WeatherServiceControllerImpl();
	}

	@Bean
	public WeatherServiceControllerFacadeAspect weatherServiceControllerFacadeAspect() {
		return new WeatherServiceControllerFacadeAspect();
	}
	
	@Bean
	public WeatherBackend weatherRepository() {
		return new WeatherBackend();
	}
	
	@Bean
	public PlausibilityChecker plausibilityChecker() throws IOException {
	    return new PlausibilityChecker();
	}

	@Bean
	public Rules rules() {
		return new Rules();
	}
	
	@Bean
	public DmnEngine dmnEngine() {
	    DmnEngineConfiguration configuration = DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
	    return configuration.buildEngine();
	}
}
