package de.codecentric.soap.configuration;

import java.util.Map;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.soap.configuration.xmlvalidation.WeatherServiceXmlValidationInterceptor;
import de.codecentric.soap.endpoint.WeatherServiceEndpoint;

@Configuration
public class WebServiceConfiguration {
	
	public static final String SERVLET_MAPPING_URL_PATH = "/soap-api";
	public static final String SERVICE_NAME_URL_PATH = "/WeatherSoapService_1.0";
	public static final String SERVICE_LIST_TITLE = "BigWeatherCompanies´ List of Weather Services";
	
	@Bean
    public ServletRegistrationBean dispatcherServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(cxfServlet, SERVLET_MAPPING_URL_PATH + "/*");
        // Add custom Title to CXF´s ServiceList
        Map<String, String> initParameters = servletRegistrationBean.getInitParameters();
        initParameters.put("service-list-title", SERVICE_LIST_TITLE);
        
        return servletRegistrationBean;
    }
    
    // If you don´t want to import the cxf.xml-Springbean-Config you have to setUp this Bus for yourself
    // <bean id="cxf" class="org.apache.cxf.bus.spring.SpringBus" destroy-method="shutdown"/>
    @Bean(name=Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {    	
    	return new SpringBus();
    }
    
    
    @Bean
    public WeatherService weatherService() {
    	return new WeatherServiceEndpoint();
    }
    
    @Bean
    public Endpoint endpoint() {
    	EndpointImpl endpoint = new EndpointImpl(springBus(), weatherService());
    	endpoint.publish(SERVICE_NAME_URL_PATH);
    	endpoint.setWsdlLocation("Weather1.0.wsdl");
    	// Interceptor for custom Schema-validation-SoapFault-Response
    	endpoint.getOutFaultInterceptors().add(soapInterceptor());
    	return endpoint;
    }
    
    @Bean
    public AbstractSoapInterceptor soapInterceptor() {
    	return new WeatherServiceXmlValidationInterceptor();
    }

}
