package de.codecentric.soap.controller;


import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import de.codecentric.namespace.weatherservice.general.GetCityWeatherByZIP;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.SoapTestApplication;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes=SoapTestApplication.class,
		webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = { "server.port:8093"})
public class WeatherServiceFacadeSystemTest {
	
	@Autowired
	private WeatherService weatherService;

	@Value(value="classpath:requests/rules/GetCityForecastByZIPActivateFacadeMode.xml")
	private Resource getCityForecastByZIPActivateFacadeModeXml;

	@Value(value="classpath:requests/GetCityWeatherByZIP.xml")
	private Resource getCityWeatherByZIPXml;


	@Test
	public void getCityForecastByZIP_should_respond_with_Facade_Response() throws BusinessException, WeatherException, IOException {
		GetCityForecastByZIP getCityForecastByZIPActivateFacadeMode = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
				getCityForecastByZIPActivateFacadeModeXml.getInputStream(), GetCityForecastByZIP.class);
		
		ForecastReturn forecastReturn = weatherService.getCityForecastByZIP(getCityForecastByZIPActivateFacadeMode.getForecastRequest());
		
		assertNotNull(forecastReturn);
		assertEquals("WeimarFacade", forecastReturn.getCity());
	}

	@Test
	public void getCityWeatherByZIP_should_Not_respond_with_Facade_Response() throws BusinessException, WeatherException, IOException {
		GetCityWeatherByZIP getCityWeatherByZIP = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
				getCityWeatherByZIPXml.getInputStream(), GetCityWeatherByZIP.class);

		WeatherReturn weatherReturn = weatherService.getCityWeatherByZIP(getCityWeatherByZIP.getForecastRequest());

		assertNotNull(weatherReturn);
		assertEquals("Weimar", weatherReturn.getCity());
	}
}
