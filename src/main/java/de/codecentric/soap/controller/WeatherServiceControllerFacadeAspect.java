package de.codecentric.soap.controller;

import de.codecentric.namespace.weatherservice.general.*;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;
import de.codecentric.soap.logging.SoapFrameworkLogger;
import de.codecentric.soap.rules.Rules;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 *  Facade-Controller:
 *  This Class just returns Dummy-Responses - e.g. if you want to protect your backends or build up a new stage,
 *  that doesn´t have the complete backend-connections in place.
 */
@Component
@Aspect
public class WeatherServiceControllerFacadeAspect {
    	
    private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(WeatherServiceControllerFacadeAspect.class);

    @Autowired
    private Rules rules;

    @Value(value="classpath:responses/GetCityForecastByZIPDummyResponse.xml")
    private Resource dummyResponseGetCityForecastByZIP;
    
    @Value(value="classpath:responses/GetCityWeatherByZIPDummyResponse.xml")
    private Resource dummyResponseGetCityWeatherByZIP;
    
    @Value(value="classpath:responses/GetWeatherInformationDummyResponse.xml")
    private Resource dummyResponseGetWeatherInformation;

	@Around("execution(* de.codecentric.soap.controller.WeatherServiceControllerImpl.getCityForecastByZIP(de.codecentric.namespace.weatherservice.general.ForecastRequest)) && args(forecastRequest)")
	public Object getCityForecastByZIP(ProceedingJoinPoint proceedingJoinPoint, ForecastRequest forecastRequest) throws Throwable {
	    if(rules.activateFacadeMode(forecastRequest)){
			LOG.facadeModeReturningDummyResponseWithResponseType(ForecastReturn.class);
			return getResponseObjectFromFile(dummyResponseGetCityForecastByZIP, GetCityForecastByZIPResponse.class).getGetCityForecastByZIPResult();
		} else {
			return proceedingJoinPoint.proceed();
		}
	}	
	
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) throws BusinessException {
		LOG.facadeModeReturningDummyResponseWithResponseType(WeatherReturn.class);
		return getResponseObjectFromFile(dummyResponseGetCityWeatherByZIP, GetCityWeatherByZIPResponse.class).getGetCityWeatherByZIPResult();
	}

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
