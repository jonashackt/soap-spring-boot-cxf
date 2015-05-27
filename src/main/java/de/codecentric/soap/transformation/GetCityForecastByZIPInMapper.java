package de.codecentric.soap.transformation;

import de.codecentric.soap.internalmodel.Postcode;

public class GetCityForecastByZIPInMapper {

	public static Postcode mapRequest2Zip(String zip) {
		Postcode postcode = new Postcode();
		postcode.setCode(zip);
		return postcode;
	}
}
