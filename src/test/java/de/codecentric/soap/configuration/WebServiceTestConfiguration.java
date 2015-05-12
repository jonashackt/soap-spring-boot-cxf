package de.codecentric.soap.configuration;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.soap.soaprawclient.SoapRawClient;

@Configuration
@PropertySource("classpath:dev-test.properties")
public class WebServiceTestConfiguration {

	@Value("${webservice.client.port}")
	private String clientPort;
	
	@Value("${webservice.client.host}")
	private String clientHost;
	
	// Mind the "static"
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public WeatherService weatherService() {
		JaxWsProxyFactoryBean jaxWsFactory = new JaxWsProxyFactoryBean();
		jaxWsFactory.setServiceClass(WeatherService.class);
		jaxWsFactory.setAddress(buildUrl());
		return (WeatherService) jaxWsFactory.create();
	}
	
	private String buildUrl() {
		// return something like http://localhost:8084/soap-api/WeatherSoapService
		return "http://" + clientHost + ":" + clientPort + WebServiceConfiguration.SERVLET_MAPPING_URL_PATH + WebServiceConfiguration.SERVICE_NAME_URL_PATH;
	}
	

	@Bean
	public SoapRawClient soapRawClient() {
		SoapRawClient easyRawSOAPTester = new SoapRawClient();
		easyRawSOAPTester.setUrl(buildUrl());
		return easyRawSOAPTester;
	}
}
