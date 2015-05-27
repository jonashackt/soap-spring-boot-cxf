package de.codecentric.soap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.soap.backend.WeatherBackend;
import de.codecentric.soap.internalmodel.GeneralOutlook;
import de.codecentric.soap.internalmodel.Postcode;
import de.codecentric.soap.transformation.GetCityForecastByZIPInMapper;
import de.codecentric.soap.transformation.GetCityForecastByZIPOutMapper;

@Component
public class WeatherServiceController {

	/*
	 *  Example of a Controller:
	 *  This Class would be responsible for Mapping from Request to internal Datamodel (and backwards),
	 *  for calling Backend-Services and handling Backend-Exceptions
	 *  So it decouples the WSDL-generated Classes from the internal Classes - for when the former changes,
	 *  nothing or only the mapping has to be changed
	 */ 
	
	@Autowired
	private WeatherBackend weatherBackend;
	
	public ForecastReturn processRequest(String request) {
		// Transformation incoming JAXB-Bind Objects to internal Model
		Postcode postcode = GetCityForecastByZIPInMapper.mapRequest2Zip(request);
		
		// Call Backend with internal Model
		GeneralOutlook generalOutlook = weatherBackend.generateGeneralOutlook(postcode);
		
		// Transformation internal Model to outgoing JAXB-Bind Objects
		return GetCityForecastByZIPOutMapper.mapGeneralOutlook2Forecast(generalOutlook);
	}
}
