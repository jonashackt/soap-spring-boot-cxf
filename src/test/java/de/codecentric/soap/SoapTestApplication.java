package de.codecentric.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import de.codecentric.soap.configuration.ApplicationConfiguration;
import de.codecentric.soap.configuration.WebServiceConfiguration;

@ComponentScan("de.codecentric.soap")
@EnableAutoConfiguration
@Import({WebServiceConfiguration.class, ApplicationConfiguration.class})
public class SoapTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoapTestApplication.class, args);
    }    
}