package de.codecentric.soap.endpoint;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3._2005._05.xmlmime.Base64Binary;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherInformationReturn;
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
		ForecastRequest forecastRequest = new ForecastRequest();
		forecastRequest.setZIP("99425");
		forecastRequest.setFlagcolor("blackblue");
		
		// When
		ForecastReturn forecastReturn = weatherServiceEndpoint.getCityForecastByZIP(forecastRequest);
		
		// Then
		assertNotNull(forecastReturn);
		assertEquals("Weimar", forecastReturn.getCity());
		assertEquals("22%", forecastReturn.getForecastResult().getForecast().get(0).getProbabilityOfPrecipiation().getDaytime());
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
		assertThat(textInPdf, containsString("18.1 °C"));
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
