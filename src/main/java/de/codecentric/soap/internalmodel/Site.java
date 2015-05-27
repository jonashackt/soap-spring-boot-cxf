package de.codecentric.soap.internalmodel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.codecentric.soap.plausibilitycheck.servicemethod.CheckWithGetCityForecastByZIP;
import de.codecentric.soap.plausibilitycheck.servicemethod.CheckWithGetCityWeatherByZIP;

public class Site {
	
	// Only, when Method getCityForecastByZIP() is called, this Constraint should be valid
	@Size(min = 3, max = 5, groups=CheckWithGetCityForecastByZIP.class)
	private String postalCode;
	
	// Only, when Method getCityWeatherByZIP() is called, this Constraint should be valid
	@NotNull(groups=CheckWithGetCityWeatherByZIP.class)
	private String flagColor;
	
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String zipCode) {
		this.postalCode = zipCode;
	}
	public String getFlagColor() {
		return flagColor;
	}
	public void setFlagColor(String flagColor) {
		this.flagColor = flagColor;
	}
	
	
}
