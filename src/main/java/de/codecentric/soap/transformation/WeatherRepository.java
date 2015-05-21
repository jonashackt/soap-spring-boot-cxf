package de.codecentric.soap.transformation;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import de.codecentric.namespace.weatherservice.datatypes.ArrayOfForecast;
import de.codecentric.namespace.weatherservice.datatypes.Forecast;
import de.codecentric.namespace.weatherservice.datatypes.POP;
import de.codecentric.namespace.weatherservice.datatypes.Temp;
import de.codecentric.namespace.weatherservice.general.ForecastReturn;

@Repository
public class WeatherRepository {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherRepository.class);
	
	private de.codecentric.namespace.weatherservice.general.ObjectFactory objectFactoryGeneral;
	private de.codecentric.namespace.weatherservice.datatypes.ObjectFactory objectFactoryDatatypes;

	@PostConstruct
	public void initSomeData() {
		objectFactoryGeneral = new de.codecentric.namespace.weatherservice.general.ObjectFactory();
	}
		
	public ForecastReturn getForecast(String zip) {      
	
		ForecastReturn forecastReturn = objectFactoryGeneral.createForecastReturn();
		forecastReturn.setCity("Weimar");
		forecastReturn.setState("Deutschland");
		forecastReturn.setSuccess(true);
		forecastReturn.setWeatherStationCity("Weimar");
		
		forecastReturn.setForecastResult(generateForecastResult(forecastReturn.getCity()));
		
		return forecastReturn;
    }


	private ArrayOfForecast generateForecastResult(String city) {
		ArrayOfForecast forecastContainer = objectFactoryDatatypes.createArrayOfForecast();
		forecastContainer.getForecast().add(generateForecast(city));
		return forecastContainer;
	}


	private Forecast generateForecast(String city) {
		Forecast forecast = objectFactoryDatatypes.createForecast();	
		forecast.setDate(generateCalendarFromNow());
		forecast.setDesciption("Vorhersage für " + city);
		forecast.setTemperatures(generateTemp());
		forecast.setProbabilityOfPrecipiation(generateRegenwahrscheinlichkeit());
		return forecast;
	}

	
	private POP generateRegenwahrscheinlichkeit() {
		POP pop = objectFactoryDatatypes.createPOP();
		pop.setDaytime("22%");
		pop.setNighttime("5000%");
		return pop;
	}


	private Temp generateTemp() {
		Temp temp = objectFactoryDatatypes.createTemp();
		temp.setDaytimeHigh("90°");
		temp.setMorningLow("0°");
		return temp;
	}


	private XMLGregorianCalendar generateCalendarFromNow() {
		GregorianCalendar gregCal = GregorianCalendar.from(ZonedDateTime.now());
		XMLGregorianCalendar xmlGregCal = null;
		try {
			xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal);
		} catch (DatatypeConfigurationException e) {
			LOGGER.debug("Kein vernuenftiger Calender gesetzt - ist aber egal");
		}
		return xmlGregCal;
	}


   
}
