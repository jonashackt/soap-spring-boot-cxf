package de.codecentric.soap.internalmodel;

import javax.validation.constraints.Size;

public class Postcode {
	
	@Size(min = 3, max = 5)
	private String code;
	
	public String getCode() {
		return code;
	}
	public void setCode(String zipCode) {
		this.code = zipCode;
	}
}
