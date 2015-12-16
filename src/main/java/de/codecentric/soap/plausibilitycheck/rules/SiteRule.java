package de.codecentric.soap.plausibilitycheck.rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import de.codecentric.soap.internalmodel.Weather;

/**
 * When Sites` flagColor is mandatory {@value #flagcolorMandatory} but is Null or the postalcode doesn´t match {@value #postalcodeReqex},
 * then {@value #ERRORTEXT}
 */
@Component
@ConfigurationProperties(locations="rules.yml", prefix="site", ignoreUnknownFields=false) // this should load configuration via spring autoconfiguration to the rules fields
@Rule(name="SiteValid")
public class SiteRule {

    public static final String ERRORTEXT = "Site´s data isn´t valid!";
    
    private Weather site;
    private String postalcodeReqex;
    private boolean flagcolorMandatory;
    
    @Condition
    public boolean when() {
        System.out.println("############################################" + toString());
    	return flagcolorMandatory && site.getFlagColor() == null || !site.getPostalCode().matches(postalcodeReqex);
    }
    
    @Action
    public void then() {
        System.out.println(ERRORTEXT);
        
    }
    
    public void setSite(Weather site) {
        this.site = site;
    }

    public void setPostalcodeReqex(String postalcodeReqex) {
        this.postalcodeReqex = postalcodeReqex;
    }
    public void setFlagcolorMandatory(boolean flagcolorMandatory) {
        this.flagcolorMandatory = flagcolorMandatory;
    }

	public String getPostalcodeReqex() {
		return postalcodeReqex;
	}

	public boolean isFlagcolorMandatory() {
		return flagcolorMandatory;
	}

	@Override
	public String toString() {
		return "SiteRule [postalcodeReqex=" + postalcodeReqex
				+ ", flagcolorMandatory=" + flagcolorMandatory + "]";
	}
    
    
    
}
