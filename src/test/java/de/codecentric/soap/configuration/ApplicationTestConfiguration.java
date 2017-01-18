package de.codecentric.soap.configuration;

import java.io.IOException;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.controller.WeatherServiceController;
import de.codecentric.soap.controller.WeatherServiceControllerImpl;
import de.codecentric.soap.endpoint.WeatherServiceEndpoint;
import de.codecentric.soap.rules.PlausibilityChecker;

@Configuration
public class ApplicationTestConfiguration {

    @Bean
    public WeatherServiceEndpoint weatherServiceEndpoint() {
        return new WeatherServiceEndpoint();
    }
    
	@Bean 
	public WeatherServiceController weatherServiceController() {
		return new WeatherServiceControllerImpl();
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
    public DmnEngine dmnEngine() {
        DmnEngineConfiguration configuration = DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
        return configuration.buildEngine();
    }
}
