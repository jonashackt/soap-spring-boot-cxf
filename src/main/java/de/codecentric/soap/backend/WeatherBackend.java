package de.codecentric.soap.backend;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import de.codecentric.soap.internalmodel.GeneralOutlook;
import de.codecentric.soap.internalmodel.Postcode;

@Service
public class WeatherBackend {
    
	public GeneralOutlook generateGeneralOutlook(Postcode postcode) {      
		/*
		 * Some incredible Businesslogic...
		 */
		GeneralOutlook generalOutlook = new GeneralOutlook();
		generalOutlook.setCity("Weimar");
		generalOutlook.setDate(LocalDateTime.now());
		generalOutlook.setState("Germany");
		generalOutlook.setWeatherStation("BestStationInTown");
		return generalOutlook;
    }
   
}
