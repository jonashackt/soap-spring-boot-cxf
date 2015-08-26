package de.codecentric.soap.configuration;

import javax.annotation.PostConstruct;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("logsoapmessages")
public class WebServiceMessageLoggerConfiguration {

	@Autowired
	private SpringBus springBus;
		
	@PostConstruct
	public void activateLoggingFeature() {
		// Log SoapMessages to Logfile
    	springBus.getInInterceptors().add(logInInterceptor());
    	springBus.getInFaultInterceptors().add(logInInterceptor());
    	springBus.getOutInterceptors().add(logOutInterceptor());
    	springBus.getOutFaultInterceptors().add(logOutInterceptor());
	}

	@Bean
	public AbstractLoggingInterceptor logInInterceptor() {
		LoggingInInterceptor logInInterceptor = new LoggingInInterceptor();
		// The In-Messages are pretty without setting it, when setting it Apache CXF throws empty lines into the In-Messages
		return logInInterceptor; 
	}
	
	@Bean
	public AbstractLoggingInterceptor logOutInterceptor() {
		LoggingOutInterceptor logOutInterceptor = new LoggingOutInterceptor();
		logOutInterceptor.setPrettyLogging(true);
		return logOutInterceptor; 
	}
}
