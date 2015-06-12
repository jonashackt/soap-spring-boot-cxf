package de.codecentric.soap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.controller.WeatherServiceController;
import de.codecentric.soap.endpoint.WeatherServiceEndpoint;
import de.codecentric.soap.plausibilitycheck.rules.SiteRule;

@Configuration
public class ApplicationTestConfiguration {

	@Bean
	public WeatherServiceEndpoint weatherServiceEndpoint() {
		return new WeatherServiceEndpoint();
	}
	
	@Bean 
	public WeatherServiceController weatherServiceController() {
		return new WeatherServiceController();
	}
	
	@Bean
	public WeatherBackend weatherRepository() {
		return new WeatherBackend();
	}
	
	@Bean
	public SiteRule siteRule() {
	    SiteRule siteRule = new SiteRule();
	    siteRule.setFlagcolorMandatory(true);
	    siteRule.setPostalcodeReqex("([0-9]{5})");
	    return siteRule;
	}
}
