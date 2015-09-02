package de.codecentric.soap.backend;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.SoapFrameworkLogger;
import de.codecentric.soap.internalmodel.GeneralOutlook;
import de.codecentric.soap.internalmodel.Site;

@Service
public class WeatherBackend {

	private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(WeatherBackend.class);
	
	@Value(value="classpath:responses/forecast.pdf")
	private Resource forecastPdf;
	
	public GeneralOutlook generateGeneralOutlook(Site postcode) {      
		/*
		 * Some incredible Businesslogic...
		 */
		GeneralOutlook generalOutlook = new GeneralOutlook();
		generalOutlook.setCity("Weimar");
		generalOutlook.setDate(LocalDateTime.now());
		generalOutlook.setState("Germany");
		generalOutlook.setWeatherStation("BestStationInTown");
		return generalOutlook;
    }
	
	public byte[] getWeatherInformationPdf(String zip) throws BusinessException {
		byte[] pdf = null;
		try {
			pdf = IOUtils.toByteArray(forecastPdf.getInputStream());
		} catch (IOException e) {
			throw LOG.problemReadingPdf(e);
		}
		return pdf;
	}
   
}
