package de.codecentric.soap.transformation;

import de.codecentric.namespace.weatherservice.exception.WeatherException;
import de.codecentric.soap.common.FaultConst;


public class WeatherOutError {

	private static de.codecentric.namespace.weatherservice.exception.ObjectFactory objectFactoryDatatypes = new de.codecentric.namespace.weatherservice.exception.ObjectFactory();
	
	public static WeatherException createWeatherException(FaultConst faultContent, String originalFaultMessage) {
		// Build SOAP-Fault detail <datatypes:WeatherException>
		WeatherException weatherException = objectFactoryDatatypes.createWeatherException();		
		weatherException.setBigBusinessErrorCausingMoneyLoss(true);
		weatherException.setBusinessErrorId(faultContent.getId());
		weatherException.setExceptionDetails(originalFaultMessage);
		weatherException.setUuid("ExtremeRandomNumber");
		return weatherException;
	}

}
