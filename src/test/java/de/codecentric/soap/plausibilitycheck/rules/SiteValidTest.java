package de.codecentric.soap.plausibilitycheck.rules;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.soap.Application;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.Site;
import de.codecentric.soap.plausibilitycheck.PlausibilityChecker;
import de.codecentric.soap.plausibilitycheck.PlausibilityResult;
import de.codecentric.soap.plausibilitycheck.PlausibilityStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class SiteValidTest {

    @Autowired
    private SiteRule siteRule;
    
    @Test
    public void flagColorNull() throws BusinessException {
        // Given
        siteRule.resetStatus();
        Site site = new Site();
        site.setFlagColor(null);
        site.setPostalCode("99425");
        siteRule.setSite(site);
        
        // When
        PlausibilityResult plausibilityResult = PlausibilityChecker.checkRule(siteRule);
        
        // Then
        assertEquals(PlausibilityStatus.ERROR, plausibilityResult.getStatus());
        assertEquals(SiteRule.ERRORTEXT, plausibilityResult.getMessage());
    }
    
    @Test
    public void flagColorFilled() throws BusinessException {
        // Given
        siteRule.resetStatus();
        Site site = new Site();
        site.setFlagColor("blue");
        site.setPostalCode("99425");
        siteRule.setSite(site);
        
        // When
        PlausibilityResult plausibilityResult = PlausibilityChecker.checkRule(siteRule);
        
        // Then
        assertEquals(PlausibilityStatus.SUCCESS, plausibilityResult.getStatus());
    }
    
    @Test
    public void postalcode() {
        // Given
        siteRule.resetStatus();
        Site site = new Site();
        site.setFlagColor("blue");
        site.setPostalCode("997654");
        siteRule.setSite(site);
        
        // When
        PlausibilityResult plausibilityResult = PlausibilityChecker.checkRule(siteRule);
        
        // Then
        assertEquals(PlausibilityStatus.ERROR, plausibilityResult.getStatus());
        assertEquals(SiteRule.ERRORTEXT, plausibilityResult.getMessage());
    }
}
