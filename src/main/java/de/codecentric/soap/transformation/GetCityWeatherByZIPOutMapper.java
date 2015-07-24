package de.codecentric.soap.transformation;

import de.codecentric.namespace.weatherservice.general.WeatherReturn;
import de.codecentric.soap.internalmodel.GeneralOutlook;

public class GetCityWeatherByZIPOutMapper {
	
	// private Constructor for Utility-Class
	private GetCityWeatherByZIPOutMapper() {};
	
	private static de.codecentric.namespace.weatherservice.general.ObjectFactory objectFactoryGeneral = new de.codecentric.namespace.weatherservice.general.ObjectFactory();
	
	public static WeatherReturn mapGeneralOutlook2Weather(GeneralOutlook generalOutlook) {      
	
		WeatherReturn weatherReturn = objectFactoryGeneral.createWeatherReturn();
		weatherReturn.setCity(generalOutlook.getCity());
		//TODO: Map more fields
		weatherReturn.setState("Deutschland");
		weatherReturn.setSuccess(true);
		weatherReturn.setWeatherStationCity("Weimar");
		
		return weatherReturn;
    }
	
}
