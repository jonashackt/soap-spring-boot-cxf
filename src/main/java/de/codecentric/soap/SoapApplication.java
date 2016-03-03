package de.codecentric.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.codecentric.soap.configuration.ApplicationConfiguration;
import de.codecentric.soap.configuration.WebServiceConfiguration;
import de.codecentric.soap.configuration.WebServiceMessageLoggerConfiguration;

@Configuration
@Import({
	WebServiceConfiguration.class,
	WebServiceMessageLoggerConfiguration.class,
	ApplicationConfiguration.class
})
@EnableAutoConfiguration
public class SoapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoapApplication.class, args);
    }    
}