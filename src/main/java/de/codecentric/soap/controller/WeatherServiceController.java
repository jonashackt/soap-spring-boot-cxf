package de.codecentric.soap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cdyne.ws.weatherws.ForecastReturn;

import de.codecentric.soap.transformation.WeatherRepository;

@Component
public class WeatherServiceController {

	/*
	 *  Example of a Controller:
	 *  This Class would be responsible for Mapping from Request to internal Datamodel (and backwards),
	 *  for calling Backend-Services and handling Backend-Exceptions
	 */ 
	
	@Autowired
	private WeatherRepository weatherRepository;
	
	public ForecastReturn processRequest(String request) {
		
		return weatherRepository.getForecast(request);
	}
}
