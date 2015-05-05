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
import de.codecentric.soap.EasyRawSOAPTester;
import de.codecentric.soap.EasyRawSoapResponse;
import de.codecentric.soap.TestUtils;
import de.codecentric.soap.common.InternalBusinessException;
import de.codecentric.soap.common.WeatherConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port:8094") // This Configuration overrides the config of the embedded server, that is used (and re-used) in the Tests 
public class WeatherServiceEndpointIntegrationTest {
	
	@Autowired
	private WeatherSoap weatherService;
	
	@Autowired
	private EasyRawSOAPTester rawSoapTester;

	@Test
	public void getCityForecastByZIP() {

		// Given
		// Use readSoapMessageFromFileAndUnmarshallBody
		String zip = TestUtils.unmarshallXML2String(TestUtils.readTestResourceFile2String("GetCityForecastByZIPTestCase.xml"));
		
		// When
		ForecastReturn forecastReturn = weatherService.getCityForecastByZIP(zip);
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
	}
	
	// Raw HTTP-Communication needed
	@Test
	public void xmlErrorNichtXmlSchemaKonformTest() throws InternalBusinessException {
		// Given
		String schemaInvalidGetQuote = TestUtils.readTestResourceFile2String("xmlErrorNichtXmlSchemaKonformTest.xml");
		
		// When
		EasyRawSoapResponse rawSoapResponse = rawSoapTester.callSoapService(schemaInvalidGetQuote);
		
		// Then
		assertNotNull(rawSoapResponse);
		assertEquals("Erwarte 500 Internal Server Error", 500, rawSoapResponse.getHttpStatusCode());
        assertEquals(WeatherConstants.SCHEMA_VALIDATION_ERROR_MESSAGE, rawSoapResponse.getFaultstringValue());
        
        InnerException innerException = rawSoapResponse.getUnmarshalledObjectFromSoapMessage(InnerException.class);		
		assertNotNull("<soap:Fault><detail> should contain InnerException",  innerException);
		
		
	}
	
	
	@Test
	public void xmlErrorFehlendeSpitzeKlammernImSoapHeaderTest() throws InternalBusinessException {
		// Given
		String schemaInvalidGetQuote = TestUtils.readTestResourceFile2String("xmlErrorFehlendeSpitzeKlammernImSoapHeaderTest.xml");
		
		// When
		EasyRawSoapResponse rawSoapResponse = rawSoapTester.callSoapService(schemaInvalidGetQuote);
		
		// Then
		assertNotNull(rawSoapResponse);
		assertEquals("Erwarte 500 Internal Server Error", 500, rawSoapResponse.getHttpStatusCode());
        assertEquals(WeatherConstants.SYNTACTICALLY_INCORRECT_XML_ERROR_MESSAGE, rawSoapResponse.getFaultstringValue());
        
        InnerException innerException = rawSoapResponse.getUnmarshalledObjectFromSoapMessage(InnerExceptionclass);		
		assertNotNull("<soap:Fault><detail> should contain InnerException",  innerException);
		
		}
}
