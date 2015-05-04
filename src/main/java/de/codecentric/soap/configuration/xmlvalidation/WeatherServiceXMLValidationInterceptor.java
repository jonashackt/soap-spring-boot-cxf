package de.codecentric.soap.configuration.xmlvalidation;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.cdyne.ws.weatherws.WeatherException;

public class WeatherServiceXMLValidationInterceptor extends AbstractSoapInterceptor {

	private static final String SCHEMA_VALIDATION_ERROR_MESSAGE = "XML Schema-Validierung fehlgeschlagen";
	private static final String SCHEMA_VALIDATION_ERROR_ID = "00007";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceXMLValidationInterceptor.class);
	
	public WeatherServiceXMLValidationInterceptor() {
		super(Phase.PRE_STREAM);
	}
	
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
	    Fault exceptionFault = (Fault) message.getContent(Exception.class);

	    if (exceptionFault.getCause() instanceof UnmarshalException) { 
	    	LOGGER.debug("Error while validating the XML-Request: {}", exceptionFault.getMessage());
	    	String originalFaultMessage = exceptionFault.getMessage();
	    	// und ueberschreiben die SoapFaultMessage hier mit dem in weather geforderten Worten
	    	exceptionFault.setMessage(SCHEMA_VALIDATION_ERROR_MESSAGE);
	    	exceptionFault.setDetail(createFaultDetailWithWeatherException(originalFaultMessage));
	        message.setContent(Exception.class, exceptionFault);
	    }
	}
	
	private Element createFaultDetailWithWeatherException(String originalFaultMessage) {
		WeatherException weatherException = createWeatherException(originalFaultMessage);
    	Document weatherExc = convertWeatherException2W3cElement(weatherException);
    	// As the Root-Element is deleted while adding the WeatherException to the Fault-Details, we have to use a Workaround:
    	// we append it to a new Element, which then gets deleted again
    	return appendAsChild(weatherExc);
	}

	private WeatherException createWeatherException(String originalFaultMessage) {
		WeatherFehler fehler = new WeatherFehler();
		fehler.setMeldungId(SCHEMA_VALIDATION_ERROR_ID);
		fehler.setText(originalFaultMessage);
		return WeatherExceptionDetailOut.mappeFehlerAufResponse(fehler);
	}

	private Document convertWeatherException2W3cElement(WeatherException weatherException) {
		Document weatherExceptionDoc = null;
		try {
	        // Create Document
			JAXBContext jaxbContext = JAXBContext.newInstance(WeatherException.class);
			weatherExceptionDoc = getDocumentBuilder().newDocument();
        	
			// Marshal the Object to a Document
        	Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(weatherException, weatherExceptionDoc);			
		} catch (Exception e) {
			LOGGER.error("Fatal Error while generating the Fault for Weather-SoapFault-Details: " + e.getMessage());
			// We don´t want an Exception in the Exceptionhandling
		}
        return weatherExceptionDoc;
	}


	private Element appendAsChild(Document weatherExc) {
		Document docWithWeatherExceptionAsChild = null;
		try {
			// Create the Document			
			docWithWeatherExceptionAsChild = getDocumentBuilder().newDocument();			
			
			// Copy the weatherExc as ChildElement under a new Element
			Element rootElement2DeleteByCxfFault = docWithWeatherExceptionAsChild.createElement("root2kick");
			docWithWeatherExceptionAsChild.appendChild(rootElement2DeleteByCxfFault);
						
			Node importedNode = docWithWeatherExceptionAsChild.importNode(weatherExc.getDocumentElement(), true);
			docWithWeatherExceptionAsChild.getDocumentElement().appendChild(importedNode);
			
		} catch (Exception e) {
			LOGGER.error("Fatal Error while generating the Fault for Weather-SoapFault-Details: " + e.getMessage());
			// We don´t want an Exception in the Exceptionhandling
		}
        return docWithWeatherExceptionAsChild.getDocumentElement();
	}
	
	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory docBuilderfactory = DocumentBuilderFactory.newInstance();
        return docBuilderfactory.newDocumentBuilder();
	}

}
