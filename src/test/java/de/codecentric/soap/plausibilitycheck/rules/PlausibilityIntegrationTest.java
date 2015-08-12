package de.codecentric.soap.plausibilitycheck.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.hamcrest.CoreMatchers;
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
@WebIntegrationTest("server.port:8093") // This Configuration overrides the config of the embedded server, that is used (and re-used) in the Tests 
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
		//TODO: Fix with DMN
//		assertEquals(false, forecastReturn.isSuccess());
//		assertThat(forecastReturn.getResponseText(), CoreMatchers.containsString(SiteRule.ERRORTEXT));
	}
}
