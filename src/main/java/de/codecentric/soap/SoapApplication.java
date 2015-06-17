package de.codecentric.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import de.codecentric.soap.plausibilitycheck.rules.SiteRule;

@ComponentScan("de.codecentric.soap")
@EnableAutoConfiguration
@EnableConfigurationProperties // needed, to automatically load rules.yml defined properties to Rule-Pojos fields, see http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
public class SoapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoapApplication.class, args);
    }
    
    @Autowired
    void getSiteRule(SiteRule siteRule) {
    	System.out.println(siteRule.toString());
    }
    
}