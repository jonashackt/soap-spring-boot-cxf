package de.codecentric.soap.endpoint;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import de.codecentric.soap.SoapApplication;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SoapApplication.class)
//@WebIntegrationTest-Configuration "server.port:XYZ" overrides the config of the embedded server, that is used (and re-used) in the Tests 
//If you want to log the actual SOAP-Messages, e.g. by using TCP/IP-Monitor in Eclipse, just change port/url in dev-test.properties 
@WebIntegrationTest("server.port:8093")
public class WeatherServiceEndpointIntegrationTest {
	
	@Autowired
	private WeatherService weatherService;

	@Value(value="classpath:requests/GetCityForecastByZIPTest.xml")
	private Resource GetCityForecastByZIPTestXml;
	
	@Test
	public void getCityForecastByZIP() throws BusinessException, WeatherException, IOException {
		// Given
		GetCityForecastByZIP getCityForecastByZIP = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
				GetCityForecastByZIPTestXml.getInputStream(), GetCityForecastByZIP.class);
		
		// When
		ForecastReturn forecastReturn = weatherService.getCityForecastByZIP(getCityForecastByZIP.getForecastRequest());
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
	}
}
