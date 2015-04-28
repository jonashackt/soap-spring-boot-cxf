package de.codecentric.soap.endpoint;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cdyne.ws.weatherws.ForecastReturn;
import com.cdyne.ws.weatherws.WeatherSoap;

import de.codecentric.soap.Application;
import de.codecentric.soap.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port:8094") // This Configuration overrides the config of the embedded server, that is used (and re-used) in the Tests 
public class WeatherServiceEndpointIntegrationTest {
	
	@Autowired
	private WeatherSoap weatherService;

	@Test
	public void getCityForecastByZIP() {

		// Given
		String zip = TestUtils.unmarshallXML2String(TestUtils.readTestResourceFile2String("GetCityForecastByZIPTestCase.xml"));
		
		// When
		ForecastReturn forecastReturn = weatherService.getCityForecastByZIP(zip);
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
	}
}
