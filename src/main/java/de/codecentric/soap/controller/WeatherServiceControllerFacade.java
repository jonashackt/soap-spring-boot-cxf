package de.codecentric.soap.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.GetCityForecastByZIPResponse;
import de.codecentric.namespace.weatherservice.general.GetCityWeatherByZIPResponse;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;

/*
 *  Facade-Controller:
 *  This Class just returns Dummy-Responses - e.g. if you want to protect your backends or build up a new stage,
 *  that doesn´t have the complete backend-connections in place.
 */ 
@Component
public class WeatherServiceControllerFacade implements WeatherServiceController {
    	
    private static final Logger LOG = LoggerFactory.getLogger(WeatherServiceControllerFacade.class);
	
    @Value(value="classpath:responses/GetCityForecastByZIPDummyResponse.xml")
    private org.springframework.core.io.Resource dummyResponseGetCityForecastByZIP;
    
    @Value(value="classpath:responses/GetCityWeatherByZIPDummyResponse.xml")
    private org.springframework.core.io.Resource dummyResponseGetCityWeatherByZIP;
	
	@Override
	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) throws BusinessException {
	    LOG.debug("Facade-Mode: Returning Dummy-Response");
	    GetCityForecastByZIPResponse dummyResponse = null;
		try {
			dummyResponse = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(dummyResponseGetCityForecastByZIP.getInputStream(), GetCityForecastByZIPResponse.class);
		} catch (IOException ioException) {
			throw new BusinessException("Problem reading or marshalling Dummy-Response: " + ioException.getMessage(), ioException);
		}
		return dummyResponse.getGetCityForecastByZIPResult();
	}	
	
	@Override
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) throws BusinessException {
		LOG.debug("Facade-Mode: Returning Dummy-Response");
		GetCityWeatherByZIPResponse dummyResponse = null;
		try {
			dummyResponse = XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(dummyResponseGetCityWeatherByZIP.getInputStream(), GetCityWeatherByZIPResponse.class);
		} catch (IOException ioException) {
			throw new BusinessException("Problem reading or marshalling Dummy-Response: " + ioException.getMessage(), ioException);
		}
		return dummyResponse.getGetCityWeatherByZIPResult();
		
	}
}
