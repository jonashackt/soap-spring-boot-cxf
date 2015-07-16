package de.codecentric.soap.plausibilitycheck.rules;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.soap.SoapApplication;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.Site;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SoapApplication.class)
public class SiteValidTest {

    @Autowired
    private SiteRule siteRule;
    
    @Test
    public void flagColorNull() throws BusinessException {
        // Given
        Site site = new Site();
        site.setFlagColor(null);
        site.setPostalCode("99425");
        siteRule.setSite(site);
        
        
        // When
        
        
        // Then
        
    }
    
    @Test
    public void flagColorFilled() throws BusinessException {
        // Given
    	Site site = new Site();
        site.setFlagColor("blue");
        site.setPostalCode("99425");
        siteRule.setSite(site);
        
        
        
        // When
        
        
        // Then
        
    }
    
    @Test
    public void postalcode() {
        // Given
    	Site site = new Site();
        site.setFlagColor("blue");
        site.setPostalCode("997654");
        siteRule.setSite(site);
        
        
        // When
       
        
        // Then
    }
}
