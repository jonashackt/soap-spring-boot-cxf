package de.codecentric.soap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherInformationReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.GeneralOutlook;
import de.codecentric.soap.internalmodel.Weather;
import de.codecentric.soap.logging.SoapFrameworkLogger;
import de.codecentric.soap.plausibilitycheck.PlausibilityChecker;
import de.codecentric.soap.transformation.GetCityForecastByZIPInMapper;
import de.codecentric.soap.transformation.GetCityForecastByZIPOutMapper;
import de.codecentric.soap.transformation.GetCityWeatherByZIPOutMapper;
import de.codecentric.soap.transformation.GetWeatherInformationOutMapper;

/*
 *  Example-Controller:
 *  This Class would be responsible for Mapping from Request to internal Datamodel (and backwards),
 *  for calling Backend-Services and handling Backend-Exceptions
 *  So it decouples the WSDL-generated Classes from the internal Classes - for when the former changes,
 *  nothing or only the mapping has to be changed
 */ 
@Component
public class WeatherServiceControllerImpl implements WeatherServiceController {
    
    private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(WeatherServiceControllerImpl.class);
	
	@Autowired
	private WeatherBackend weatherBackend;
	
	@Autowired
	private PlausibilityChecker plausibilityChecker;
	
	@Override
	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) throws BusinessException {
	    LOG.transformIncomingJaxbObjects2InternalModel();
		Weather weather = GetCityForecastByZIPInMapper.mapRequest2Weather(forecastRequest);
		
		LOG.checkInternalModelsFunctionalPlausibilityAfterRequest();
		plausibilityChecker.checkGetCityForecastByZIP(weather);
		
		LOG.callBackendWithInternalModel();
		GeneralOutlook generalOutlook = weatherBackend.generateGeneralOutlook(weather);
		
		LOG.transformInternalModel2OutgoingJaxbObjects();
		return GetCityForecastByZIPOutMapper.mapGeneralOutlook2Forecast(generalOutlook);
	}
	
	@Override
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) throws BusinessException {
	    LOG.transformIncomingJaxbObjects2InternalModel();
		Weather site = GetCityForecastByZIPInMapper.mapRequest2Weather(forecastRequest);
				
		LOG.callBackendWithInternalModel();
		GeneralOutlook generalOutlook = weatherBackend.generateGeneralOutlook(site);
		
		LOG.transformInternalModel2OutgoingJaxbObjects();
		return GetCityWeatherByZIPOutMapper.mapGeneralOutlook2Weather(generalOutlook);
	}

	@Override
	public WeatherInformationReturn getWeatherInformation(String zip) throws BusinessException {
		LOG.transformIncomingJaxbObjects2InternalModel();
		// Nothing to do here
		
		LOG.callBackendWithInternalModel();
		byte[] pdf = weatherBackend.getWeatherInformationPdf(zip);
		
		LOG.transformInternalModel2OutgoingJaxbObjects();
		return GetWeatherInformationOutMapper.mapPdf2WeatherOverviewPdf(pdf);
	}
}
