package de.codecentric.soap.configuration;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.LoggingFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("logsoapmessages")
public class WebServiceMessageLoggerConfiguration {

	@Autowired
	private SpringBus springBus;
		
	@Bean
	public LoggingFeature loggingFeature() {
		// Log SoapMessages to Logfile
    	LoggingFeature logFeature = new LoggingFeature();
    	logFeature.setPrettyLogging(true);
    	logFeature.initialize(springBus);
    	springBus.getFeatures().add(logFeature);
    	return logFeature;
	}
}
