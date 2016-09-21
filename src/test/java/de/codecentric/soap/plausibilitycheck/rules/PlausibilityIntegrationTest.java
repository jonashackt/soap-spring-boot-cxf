package de.codecentric.soap.plausibilitycheck.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.namespace.weatherservice.WeatherException;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIP;
import de.codecentric.soap.SoapTestApplication;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes=SoapTestApplication.class,
		webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = { "server.port:8093"})
public class PlausibilityIntegrationTest {
    
    @Value(value="classpath:requests/plausibility/GetCityForecastByZIPPostalcodeToLong.xml")
    private Resource getCityForecastByZipPostalcodeToLongXml;
    
	@Autowired
	private WeatherService weatherService;
	
	@Test
	public void getCityForecastByZIP_postalCodeToLong() throws BusinessException, WeatherException, IOException {
	    // Given
        GetCityForecastByZIP getCityForecastByZIP = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(
        		getCityForecastByZipPostalcodeToLongXml.getInputStream(), GetCityForecastByZIP.class);
        
        // When
        ForecastReturn forecastReturn = weatherService.getCityForecastByZIP(getCityForecastByZIP.getForecastRequest());
        
		// Then
		assertNotNull(forecastReturn);
		assertEquals(false, forecastReturn.isSuccess());
	}
}
