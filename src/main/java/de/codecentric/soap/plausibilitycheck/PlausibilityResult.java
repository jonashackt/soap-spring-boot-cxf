package de.codecentric.soap.plausibilitycheck;


/**
 * Default-Status is SUCCESSful
 */
public class PlausibilityResult {

	private PlausibilityStatus status = PlausibilityStatus.SUCCESS;
	private String message;
	
	public PlausibilityStatus getStatus() {
		return status;
	}
	
	public void setStatus(PlausibilityStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
