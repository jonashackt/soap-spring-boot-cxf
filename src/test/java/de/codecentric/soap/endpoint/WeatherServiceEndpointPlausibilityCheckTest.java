package de.codecentric.soap.endpoint;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.configuration.ApplicationTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ApplicationTestConfiguration.class)
public class WeatherServiceEndpointPlausibilityCheckTest {

	@Autowired
	private WeatherServiceEndpoint weatherServiceEndpoint;
		
	/*
	 * test-cases for plausibility checks
	 */
	
	@Test
	public void getCityForecastByZIP_zipDoesNotHaveCorrectSize() {
		// Given
		ForecastRequest forecastRequest = new ForecastRequest();
		forecastRequest.setZIP("994257");
		
		// When
		ForecastReturn forecastReturn = weatherServiceEndpoint.getCityForecastByZIP(forecastRequest);
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals(false, forecastReturn.isSuccess());
		assertThat(forecastReturn.getResponseText(), CoreMatchers.containsString("postalCode muss zwischen 3 und 5 liegen"));
	}
	
	@Test
	public void getCityForecastByZIP_zipHasCorrectSizeButNoFlag() {
		// Given
		ForecastRequest forecastRequest = new ForecastRequest();
		forecastRequest.setZIP("99425");
		
		// When
		ForecastReturn forecastReturn = weatherServiceEndpoint.getCityForecastByZIP(forecastRequest);
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals(true, forecastReturn.isSuccess());
	}
	
	@Test
	public void getCityWeatherByZIP_zipWithIncorrectSizeIsOk() {
		// Given
		ForecastRequest forecastRequest = new ForecastRequest();
		forecastRequest.setZIP("99425764563456");
		forecastRequest.setFlagcolor("bluered");
		
		// When
		WeatherReturn weatherReturn = weatherServiceEndpoint.getCityWeatherByZIP(forecastRequest);
		
		// Then
		assertNotNull(weatherReturn);
		assertEquals(true, weatherReturn.isSuccess());
	}
	
	@Test
	public void getCityWeatherByZIP_zipHasCorrectSizeButNoFlag() {
		// Given
		ForecastRequest forecastRequest = new ForecastRequest();
		forecastRequest.setZIP("99425");
		
		// When
		WeatherReturn weatherReturn = weatherServiceEndpoint.getCityWeatherByZIP(forecastRequest);
		
		// Then
		assertNotNull(weatherReturn);
		assertEquals(false, weatherReturn.isSuccess());
		assertThat(weatherReturn.getResponseText(), CoreMatchers.containsString("Site.flagColor darf nicht null sein"));
	}
}
