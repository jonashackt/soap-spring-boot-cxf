package de.codecentric.soap.endpoint;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.datatypes.ArrayOfWeatherDescription;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.controller.WeatherServiceController;

@WebService(endpointInterface = "com.cdyne.ws.weatherws.WeatherSoap",
serviceName = "WeatherService")
public class WeatherServiceEndpoint implements WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceEndpoint.class);

    @Autowired
    private WeatherServiceController weatherServiceController;
    
	@Override
	public ArrayOfWeatherDescription getWeatherInformation() {
		LOGGER.debug("getWeatherInformation() was called successfully");
		//TODO: Fill with some data
		return null;
	}

	@Override
	public ForecastReturn getCityForecastByZIP(String request) {
		LOGGER.debug("getCityForecastByZIP() was called successfully - handing over to internal processing.");
				
		return weatherServiceController.processRequest(request);
	}

	@Override
	public WeatherReturn getCityWeatherByZIP(String zip) {
		LOGGER.debug("getCityWeatherByZIP() was called successfully");
		//TODO: Fill with some data
		return null;
	}
}
