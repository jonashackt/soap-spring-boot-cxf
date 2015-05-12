package de.codecentric.soap.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.soap.configuration.ApplicationTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ApplicationTestConfiguration.class)
public class WeatherServiceEndpointTest {

	/*
	 * Example of an UnitTest - You can simply test the Endpoint-Class like any other Pojo
	 * Here Spring Configuration with DI is used for better testability
	 */

	@Autowired
	private WeatherServiceEndpoint weatherServiceEndpoint;
	
	@Test
	public void getCityForecastByZIP() {
		// Given
		String request = "99425";
		
		// When
		ForecastReturn forecastReturn = weatherServiceEndpoint.getCityForecastByZIP(request);
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
	}
}
