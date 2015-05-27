package de.codecentric.soap.endpoint;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.datatypes.ArrayOfWeatherDescription;
import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.controller.WeatherServiceController;

@WebService(endpointInterface = "de.codecentric.namespace.weatherservice.WeatherService",
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
	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) {
		LOGGER.debug("getCityForecastByZIP() was called successfully - handing over to internal processing.");
				
		try {
			return weatherServiceController.getCityForecastByZIP(forecastRequest);
		
		} catch (BusinessException exception) {
			ForecastReturn forecastReturn = new ForecastReturn();
			forecastReturn.setSuccess(false);
			forecastReturn.setResponseText(exception.getMessage());
		    return forecastReturn;
		}
	}

	@Override
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) {
		LOGGER.debug("getCityWeatherByZIP() was called successfully");
		try {
			return weatherServiceController.getCityWeatherByZIP(forecastRequest);
		
		} catch (BusinessException exception) {
			WeatherReturn weatherReturn = new WeatherReturn();
			weatherReturn.setSuccess(false);
			weatherReturn.setResponseText(exception.getMessage());
		    return weatherReturn;
		}
	}
}
