package de.codecentric.soap.transformation;

import org.w3._2005._05.xmlmime.Base64Binary;

import de.codecentric.namespace.weatherservice.datatypes.WeatherOverviewPdf;
import de.codecentric.namespace.weatherservice.general.WeatherInformationReturn;

public final class GetWeatherInformationOutMapper {
	
	// private Constructor for Utility-Class
	private GetWeatherInformationOutMapper() {};
	
	private static de.codecentric.namespace.weatherservice.datatypes.ObjectFactory objectFactoryDatatypes = new de.codecentric.namespace.weatherservice.datatypes.ObjectFactory();
	private static de.codecentric.namespace.weatherservice.general.ObjectFactory objectFactoryGeneral = new de.codecentric.namespace.weatherservice.general.ObjectFactory();
	
	public static WeatherInformationReturn mapPdf2WeatherOverviewPdf(byte[] pdf) {      
		WeatherInformationReturn weatherInformationReturn = objectFactoryGeneral.createWeatherInformationReturn();
		weatherInformationReturn.setSuccess(true);
		weatherInformationReturn.setResponseText("WeatherInformation successfully generated and converted to Pdf");
		weatherInformationReturn.setWeatherOverviewPdf(createWeatherOverviewPdf(pdf));
		return weatherInformationReturn;
    }
	
	private static WeatherOverviewPdf createWeatherOverviewPdf(byte[] pdf) {
		WeatherOverviewPdf weatherOverviewPdf = objectFactoryDatatypes.createWeatherOverviewPdf();
		weatherOverviewPdf.setFilename("WeatherInfo.pdf");
		weatherOverviewPdf.setData(byte2Base64Binary(pdf));
		return weatherOverviewPdf;
	}

	private static Base64Binary byte2Base64Binary(byte[] pdf) {
		Base64Binary pdfAsBase64Binary = new Base64Binary();
		pdfAsBase64Binary.setContentType("application/pdf");
		pdfAsBase64Binary.setValue(pdf);
		return pdfAsBase64Binary;
	}
}
