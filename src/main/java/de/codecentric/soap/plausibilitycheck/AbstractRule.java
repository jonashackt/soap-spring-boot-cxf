package de.codecentric.soap.plausibilitycheck;


public class AbstractRule {

	private PlausibilityResult plausibilityResult = new PlausibilityResult();

	public PlausibilityResult getResult() {
		return plausibilityResult;
	}

	public void setResult(PlausibilityResult plausibilityResult) {
		this.plausibilityResult = plausibilityResult;
	}
	
	/**
     * Reset 2 SUCCESS after running the Rue
     */
    public void resetStatus() {
        plausibilityResult.setStatus(PlausibilityStatus.SUCCESS);
    }
}
