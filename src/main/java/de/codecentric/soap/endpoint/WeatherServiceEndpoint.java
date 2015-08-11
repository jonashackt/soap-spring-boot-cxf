package de.codecentric.soap.endpoint;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.springframework.beans.factory.annotation.Autowired;

import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.namespace.weatherservice.datatypes.ArrayOfWeatherDescription;
import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.SoapFrameworkLogger;
import de.codecentric.soap.controller.WeatherServiceController;

@WebService(endpointInterface = "de.codecentric.namespace.weatherservice.WeatherService",
serviceName = "WeatherService")
public class WeatherServiceEndpoint implements WeatherService {

	private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(WeatherServiceEndpoint.class);
	
	@Autowired
    private WeatherServiceController weatherServiceController;
    
    @Resource  
	WebServiceContext wsContext;  
    
	@Override
	public ArrayOfWeatherDescription getWeatherInformation() {
		LOG.successfullyCalledServeEndpointWithMethod("getWeatherInformation");
		//TODO: Fill with some data
		return null;
	}

	@Override
	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) {
		LOG.successfullyCalledServeEndpointWithMethod("getCityForecastByZIP");
		
		try {
			return weatherServiceController.getCityForecastByZIP(forecastRequest);
		
		} catch (BusinessException exception) {
			LOG.errorAccuredInBackendProcessing(exception);
			ForecastReturn forecastReturn = new ForecastReturn();
			forecastReturn.setSuccess(false);
			forecastReturn.setResponseText(exception.getMessage());
		    return forecastReturn;
		}
	}

	@Override
	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) {
		LOG.successfullyCalledServeEndpointWithMethod("getCityWeatherByZIP");
		try {
			return weatherServiceController.getCityWeatherByZIP(forecastRequest);
		
		} catch (BusinessException exception) {
			LOG.errorAccuredInBackendProcessing(exception);
			WeatherReturn weatherReturn = new WeatherReturn();
			weatherReturn.setSuccess(false);
			weatherReturn.setResponseText(exception.getMessage());
		    return weatherReturn;
		}
	}
}
