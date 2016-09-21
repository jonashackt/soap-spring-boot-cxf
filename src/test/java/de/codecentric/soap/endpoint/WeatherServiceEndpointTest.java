package de.codecentric.soap.endpoint;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3._2005._05.xmlmime.Base64Binary;

import de.codecentric.namespace.weatherservice.datatypes.ProductName;
import de.codecentric.namespace.weatherservice.general.ForecastCustomer;
import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherInformationReturn;
import de.codecentric.soap.configuration.ApplicationTestConfiguration;
import de.codecentric.soap.internalmodel.MethodOfPayment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfiguration.class)
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
		ForecastRequest forecastRequest = new ForecastRequest();
		forecastRequest.setZIP("99425");
		forecastRequest.setFlagcolor("blackblue");
		forecastRequest.setProductName(ProductName.FORECAST_BASIC);
		ForecastCustomer customer = new ForecastCustomer();
		customer.setAge(67);
		customer.setContribution(500);
		customer.setMethodOfPayment(MethodOfPayment.Bitcoin.getName());
        forecastRequest.setForecastCustomer(customer);
		
		// When
		ForecastReturn forecastReturn = weatherServiceEndpoint.getCityForecastByZIP(forecastRequest);
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals(true, forecastReturn.isSuccess());
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
		
		// Given
		forecastRequest.setZIP("99999");
		// When
		forecastReturn = weatherServiceEndpoint.getCityForecastByZIP(forecastRequest);
		// Then
		assertNotNull(forecastReturn);
		assertEquals("A wrong ZIP should lead to Success=false", false, forecastReturn.isSuccess());
        assertThat(forecastReturn.getResponseText(), containsString("The postalcode isn´t in the correct range between 01001 and 99999"));
        
        // Given
        forecastRequest.setProductName(ProductName.FORECAST_PROFESSIONAL);
        forecastRequest.setZIP("46537");
        customer.setMethodOfPayment("Cash");
        forecastRequest.setForecastCustomer(customer);
        // When
        forecastReturn = weatherServiceEndpoint.getCityForecastByZIP(forecastRequest);
        // Then
        assertNotNull(forecastReturn);
        assertEquals("Unsupported MethodOfPayment should lead to Success=false", false, forecastReturn.isSuccess());
        assertThat(forecastReturn.getResponseText(), containsString("Sorry, we don´t support this method of payment"));
	}
	
	@Test
	public void getWeatherInformationPdfContents() throws IOException {
		// Given
		String zip = "99425";
		
		// When
		WeatherInformationReturn weatherInformationReturn = weatherServiceEndpoint.getWeatherInformation(zip);
		
		// Then
		byte[] pdf = extractPdfByteArrayFromResponse(weatherInformationReturn);
		
		String textInPdf = extractPdfText(pdf);
		assertThat(textInPdf, containsString("Weather in your city"));
		assertThat(textInPdf, containsString("Weimar"));
		assertThat(textInPdf, containsString("18.1"));
		assertThat(textInPdf, containsString("Wind Gentle Breeze 3.6 m/s"));
		assertThat(textInPdf, containsString("West-southwest"));
		assertThat(textInPdf, containsString("Cloudiness scattered clouds"));
		assertThat(textInPdf, containsString("Pressure 1018 hpa"));
		assertThat(textInPdf, containsString("Humidity 55 %"));
		assertThat(textInPdf, containsString("Sunrise 23:30"));
		assertThat(textInPdf, containsString("Sunset 13:1"));
	}

	private byte[] extractPdfByteArrayFromResponse(WeatherInformationReturn weatherInformationReturn) {
		assertNotNull(weatherInformationReturn);
		assertNotNull(weatherInformationReturn.getWeatherOverviewPdf());
		assertNotNull(weatherInformationReturn.getWeatherOverviewPdf().getData());
		Base64Binary pdfAsBase64 = weatherInformationReturn.getWeatherOverviewPdf().getData();
		return pdfAsBase64.getValue();
	}
	
	/**
	 * Extracts all the Text inside a Pdf
	 */
	private static String extractPdfText(byte[] pdfData) throws IOException {
	   PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData));
	   try {
	      return new PDFTextStripper().getText(pdfDocument);
	   } finally {
	      pdfDocument.close();
	   }
	}
}
