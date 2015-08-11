package de.codecentric.soap.controller;

import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;
import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.common.BusinessException;

public interface WeatherServiceController {

	public ForecastReturn getCityForecastByZIP(ForecastRequest forecastRequest) throws BusinessException;

	public WeatherReturn getCityWeatherByZIP(ForecastRequest forecastRequest) throws BusinessException;

}