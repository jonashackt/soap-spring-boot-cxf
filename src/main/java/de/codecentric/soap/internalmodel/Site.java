package de.codecentric.soap.internalmodel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Site {
	
	@Size(min = 3, max = 5)
	private String postalCode;
	
	@NotNull
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
