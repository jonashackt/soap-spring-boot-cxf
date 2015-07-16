package de.codecentric.soap.endpoint;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import de.codecentric.soap.SoapApplication;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.soaprawclient.SoapRawClientFileUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SoapApplication.class) 
@WebIntegrationTest("server.port:8093") // This Configuration overrides the config of the embedded server, that is used (and re-used) in the Tests 
public class WeatherServiceEndpointIntegrationTest {
	
	@Autowired
	private WeatherService weatherService;

	@Test
	public void getCityForecastByZIP() throws BusinessException, WeatherException {
		// Given
		GetCityForecastByZIP getCityForecastByZIP = SoapRawClientFileUtils.readSoapMessageFromFileAndUnmarshallBody2Object("GetCityForecastByZIPTest.xml", GetCityForecastByZIP.class);
		
		// When
		ForecastReturn forecastReturn = weatherService.getCityForecastByZIP(getCityForecastByZIP.getForecastRequest());
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
	}
}
