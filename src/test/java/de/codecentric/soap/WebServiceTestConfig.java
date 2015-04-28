package de.codecentric.soap;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cdyne.ws.weatherws.WeatherSoap;

@Configuration
public class WebServiceTestConfig {

	@Bean
	public WeatherSoap weatherService() {
		JaxWsProxyFactoryBean jaxWsFactory = new JaxWsProxyFactoryBean();
		jaxWsFactory.setServiceClass(WeatherSoap.class);
		jaxWsFactory.setAddress("http://localhost:8084/soap-api/WeatherSoapService");
		return (WeatherSoap) jaxWsFactory.create();
	}
}
