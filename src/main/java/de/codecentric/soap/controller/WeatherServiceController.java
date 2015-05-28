package de.codecentric.soap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.GeneralOutlook;
import de.codecentric.soap.internalmodel.Site;
import de.codecentric.soap.plausibilitycheck.Plausibility;
import de.codecentric.soap.plausibilitycheck.servicemethod.CheckWithGetCityForecastByZIP;
import de.codecentric.soap.plausibilitycheck.servicemethod.CheckWithGetCityWeatherByZIP;
import de.codecentric.soap.transformation.GetByZIPInMapper;
import de.codecentric.soap.transformation.GetCityForecastByZIPOutMapper;
import de.codecentric.soap.transformation.GetCityWeatherByZIPOutMapper;

@Component
public class WeatherServiceController {

	/*
	 *  Example-Controller:
	 *  This Class would be responsible for Mapping from Request to internal Datamodel (and backwards),
	 *  for calling Backend-Services and handling Backend-Exceptions
	 *  So it decouples the WSDL-generated Classes from the internal Classes - for when the former changes,
	 *  nothing or only the mapping has to be changed
	 */ 
	
	@Autowired
	private WeatherBackend weatherBackend;
	
	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) throws BusinessException {
		// Transformation incoming JAXB-Bind Objects to internal Model
		Site site = GetByZIPInMapper.mapRequest2Zip(forecastRequest);
		
		Plausibility.check(site, CheckWithGetCityForecastByZIP.class);
		
		// Call Backend with internal Model
		GeneralOutlook generalOutlook = weatherBackend.generateGeneralOutlook(site);
		
		// Transformation internal Model to outgoing JAXB-Bind Objects
		return GetCityForecastByZIPOutMapper.mapGeneralOutlook2Forecast(generalOutlook);
	}
	
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) throws BusinessException {
		// Transformation incoming JAXB-Bind Objects to internal Model
		Site site = GetByZIPInMapper.mapRequest2Zip(forecastRequest);
		
		Plausibility.check(site, CheckWithGetCityWeatherByZIP.class);
		
		// Call Backend with internal Model
		GeneralOutlook generalOutlook = weatherBackend.generateGeneralOutlook(site);
		
		// Transformation internal Model to outgoing JAXB-Bind Objects
		return GetCityWeatherByZIPOutMapper.mapGeneralOutlook2Weather(generalOutlook);
	}
}
