package de.codecentric.soap.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIPResponse;
import de.codecentric.namespace.weatherservice.general.GetCityWeatherByZIPResponse;
import de.codecentric.namespace.weatherservice.general.GetWeatherInformationResponse;
import de.codecentric.namespace.weatherservice.general.WeatherInformationReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.SoapFrameworkLogger;
import de.codecentric.soap.common.XmlUtils;

/*
 *  Facade-Controller:
 *  This Class just returns Dummy-Responses - e.g. if you want to protect your backends or build up a new stage,
 *  that doesn´t have the complete backend-connections in place.
 */ 
@Component
public class WeatherServiceControllerFacade implements WeatherServiceController {
    	
    private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(WeatherServiceControllerFacade.class);
	
    @Value(value="classpath:responses/GetCityForecastByZIPDummyResponse.xml")
    private Resource dummyResponseGetCityForecastByZIP;
    
    @Value(value="classpath:responses/GetCityWeatherByZIPDummyResponse.xml")
    private Resource dummyResponseGetCityWeatherByZIP;
    
    @Value(value="classpath:responses/GetWeatherInformationDummyResponse.xml")
    private Resource dummyResponseGetWeatherInformation;
	
	@Override
	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) throws BusinessException {
	    LOG.facadeModeReturningDummyResponseWithResponseType(ForecastReturn.class);
		return getResponseObjectFromFile(dummyResponseGetCityForecastByZIP, GetCityForecastByZIPResponse.class).getGetCityForecastByZIPResult();
	}	
	
	@Override
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) throws BusinessException {
		LOG.facadeModeReturningDummyResponseWithResponseType(WeatherReturn.class);
		return getResponseObjectFromFile(dummyResponseGetCityWeatherByZIP, GetCityWeatherByZIPResponse.class).getGetCityWeatherByZIPResult();
	}

	@Override
	public WeatherInformationReturn getWeatherInformation(String zip) throws BusinessException {
		LOG.facadeModeReturningDummyResponseWithResponseType(WeatherInformationReturn.class);
		return getResponseObjectFromFile(dummyResponseGetWeatherInformation, GetWeatherInformationResponse.class).getGetWeatherInformationResult();
	}
	
	private <R> R getResponseObjectFromFile(Resource dummyFile, Class<R> responseClass) throws BusinessException {
		R dummyResponse = null;
		try {
			dummyResponse = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(dummyFile.getInputStream(), responseClass);
		} catch (IOException ioException) {
			throw LOG.problemReadingOrMarshallingDummyResponse(ioException);
		}
		return dummyResponse;
	}
	
	
}
