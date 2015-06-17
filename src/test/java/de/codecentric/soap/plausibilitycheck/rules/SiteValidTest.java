package de.codecentric.soap.plausibilitycheck.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.soap.SoapApplication;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.Site;
import de.codecentric.soap.plausibilitycheck.PlausibilityChecker;
import de.codecentric.soap.plausibilitycheck.PlausibilityStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SoapApplication.class)
public class SiteValidTest {

    @Autowired
    private SiteRule siteRule;
    
    @Test
    public void flagColorNull() throws BusinessException {
        // Given
        PlausibilityChecker plausiChecker = PlausibilityChecker.aNewPlausibilityChecker();
        Site site = new Site();
        site.setFlagColor(null);
        site.setPostalCode("99425");
        siteRule.setSite(site);
        
        plausiChecker.addRule(siteRule);
        
        // When
        PlausibilityStatus plausiStatus = plausiChecker.fireRules();
        
        // Then
        assertEquals(PlausibilityStatus.ERROR, plausiStatus);
        assertEquals(SiteRule.ERRORTEXT, plausiChecker.getMessages().get(0));
    }
    
    @Test
    public void flagColorFilled() throws BusinessException {
        // Given
    	PlausibilityChecker plausiChecker = PlausibilityChecker.aNewPlausibilityChecker();    	
        Site site = new Site();
        site.setFlagColor("blue");
        site.setPostalCode("99425");
        siteRule.setSite(site);
        
        plausiChecker.addRule(siteRule);
        
        // When
        PlausibilityStatus plausiStatus = plausiChecker.fireRules();
        
        // Then
        assertEquals(PlausibilityStatus.SUCCESS, plausiStatus);
    }
    
    @Test
    public void postalcode() {
        // Given
    	PlausibilityChecker plausiChecker = PlausibilityChecker.aNewPlausibilityChecker();    	
        Site site = new Site();
        site.setFlagColor("blue");
        site.setPostalCode("997654");
        siteRule.setSite(site);
        
        plausiChecker.addRule(siteRule);
        
        // When
        PlausibilityStatus plausiStatus = plausiChecker.fireRules();
        
        // Then
        assertEquals(PlausibilityStatus.ERROR, plausiStatus);
        assertEquals(SiteRule.ERRORTEXT, plausiChecker.getMessages().get(0));
    }
}
