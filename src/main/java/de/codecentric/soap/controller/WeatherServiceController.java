package de.codecentric.soap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.GeneralOutlook;
import de.codecentric.soap.internalmodel.Site;
import de.codecentric.soap.plausibilitycheck.PlausibilityChecker;
import de.codecentric.soap.plausibilitycheck.PlausibilityStatus;
import de.codecentric.soap.plausibilitycheck.rules.SiteRule;
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
    private static final Logger LOG = LoggerFactory.getLogger(WeatherServiceController.class);
	
	@Autowired
	private WeatherBackend weatherBackend;
	
	@Autowired
	private SiteRule siteValid;
	
	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) throws BusinessException {
	    LOG.debug("Transformation incoming JAXB-Bind Objects to internal Model");
		Site site = GetByZIPInMapper.mapRequest2Zip(forecastRequest);
		
		LOG.debug("Check functional plausibility of internal Model after Request");
		checkPlausibilityGetCityForecastByZIP(site);
		
		LOG.debug("Call Backend with internal Model");
		GeneralOutlook generalOutlook = weatherBackend.generateGeneralOutlook(site);
		
		LOG.debug("Transformation internal Model to outgoing JAXB-Bind Objects");
		return GetCityForecastByZIPOutMapper.mapGeneralOutlook2Forecast(generalOutlook);
	}

	private void checkPlausibilityGetCityForecastByZIP(Site site) throws BusinessException {
		PlausibilityChecker plausiChecker = PlausibilityChecker.aNewPlausibilityChecker();
		siteValid.setSite(site);
		plausiChecker.addRule(siteValid);
		PlausibilityStatus plausiStatus = plausiChecker.fireRules();
		if(PlausibilityStatus.ERROR.equals(plausiStatus))
			throw new BusinessException(plausiChecker.getMessages().get(0));
	}
	
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) throws BusinessException {
	    LOG.debug("Transformation incoming JAXB-Bind Objects to internal Model");
		Site site = GetByZIPInMapper.mapRequest2Zip(forecastRequest);
				
		LOG.debug("Call Backend with internal Model");
		GeneralOutlook generalOutlook = weatherBackend.generateGeneralOutlook(site);
		
		LOG.debug("Transformation internal Model to outgoing JAXB-Bind Objects");
		return GetCityWeatherByZIPOutMapper.mapGeneralOutlook2Weather(generalOutlook);
	}
}
