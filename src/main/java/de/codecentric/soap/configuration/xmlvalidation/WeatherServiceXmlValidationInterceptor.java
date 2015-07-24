package de.codecentric.soap.configuration.xmlvalidation;


import javax.xml.bind.UnmarshalException;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctc.wstx.exc.WstxException;
import com.ctc.wstx.exc.WstxUnexpectedCharException;

import de.codecentric.soap.common.FaultConst;

public class WeatherServiceXmlValidationInterceptor extends AbstractSoapInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceXmlValidationInterceptor.class);
	
	public WeatherServiceXmlValidationInterceptor() {
		super(Phase.PRE_STREAM);
	}
	
	@Override
	public void handleMessage(SoapMessage soapMessage) throws Fault {
	    Fault fault = (Fault) soapMessage.getContent(Exception.class);
	    Throwable faultCause = fault.getCause();
	    String faultMessage = fault.getMessage();

	    if (containsFaultIndicatingNotSchemeCompliantXml(faultCause, faultMessage)) { 
	    	LOGGER.debug(FaultConst.SCHEME_VALIDATION_ERROR.getMessage() + ": {}", faultMessage);
	    	WeatherSoapFaultHelper.buildWeatherFaultAndSet2SoapMessage(soapMessage, FaultConst.SCHEME_VALIDATION_ERROR);
	    }	    
	    if (containsFaultIndicatingSyntacticallyIncorrectXml(faultCause)) {
	    	LOGGER.debug(FaultConst.SYNTACTICALLY_INCORRECT_XML_ERROR.getMessage() + ": {}", faultMessage);
	    	WeatherSoapFaultHelper.buildWeatherFaultAndSet2SoapMessage(soapMessage, FaultConst.SYNTACTICALLY_INCORRECT_XML_ERROR);	        
	    }
	}

	private boolean containsFaultIndicatingNotSchemeCompliantXml(Throwable faultCause, String faultMessage) {
		if(faultCause instanceof UnmarshalException
	    	// 1.) If the root-Element of the SoapBody is syntactically correct, but not scheme-compliant,
			// 		there is no UnmarshalException and we have to look for
			// 2.) Missing / lead to Faults without Causes, but to Messages like "Unexpected wrapper element XYZ found. Expected"
			// 		One could argue, that this is syntactically incorrect, but here we just take it as Non-Scheme-compliant
	    	|| isNotNull(faultMessage) && faultMessage.contains("Unexpected wrapper element")) {
			return true;
		}
		return false;
	}
	
	private boolean containsFaultIndicatingSyntacticallyIncorrectXml(Throwable faultCause) {
		if(faultCause instanceof WstxException
			// If Xml-Header is invalid, there is a wrapped Cause in the original Cause we have to check
			|| isNotNull(faultCause) && faultCause.getCause() instanceof WstxUnexpectedCharException
	    	|| faultCause instanceof IllegalArgumentException) {
			return true;
		}
		return false;
	}
	
	private boolean isNotNull(Object object) {
		return object != null;
	}
}
