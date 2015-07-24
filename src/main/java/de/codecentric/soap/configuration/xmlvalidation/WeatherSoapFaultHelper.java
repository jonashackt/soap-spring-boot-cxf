package de.codecentric.soap.configuration.xmlvalidation;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.codecentric.soap.common.FaultConst;
import de.codecentric.soap.common.XmlUtils;
import de.codecentric.soap.transformation.WeatherOutError;

public class WeatherSoapFaultHelper {

	// private Constructor for Utility-Class
	private WeatherSoapFaultHelper() {};
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherSoapFaultHelper.class);
	
	public static void buildWeatherFaultAndSet2SoapMessage(SoapMessage message, FaultConst faultContent) {
		Fault exceptionFault = (Fault) message.getContent(Exception.class);
		String originalFaultMessage = exceptionFault.getMessage();
		exceptionFault.setMessage(faultContent.getMessage());
		exceptionFault.setDetail(createFaultDetailWithWeatherException(originalFaultMessage, faultContent));
		message.setContent(Exception.class, exceptionFault);
	}
	
	private static Element createFaultDetailWithWeatherException(String originalFaultMessage,  FaultConst faultContent) {
		Element weatherExceptionElementAppended = null;
		try {
			Document weatherExcecption = XmlUtils.marhallJaxbElement(WeatherOutError.createWeatherException(faultContent, originalFaultMessage));
			// As the Root-Element is deleted while adding the WeatherException to the Fault-Details, we have to use a Workaround:
	    	// we append it to a new Element, which then gets deleted again
	    	weatherExceptionElementAppended = XmlUtils.appendAsChildElement2NewElement(weatherExcecption);
		} catch (Exception exception) {
			LOGGER.error("Failed to build Weather-compliant SoapFault-details: " + exception.getMessage());
			// We don´t want an Exception in the Exceptionhandling
		}
		return weatherExceptionElementAppended;
	}

}
