package de.codecentric.soap.logging.soapmsg;

import java.util.logging.Logger;

import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import de.codecentric.soap.logging.SoapFrameworkLogger;

public class LoggingOutInterceptorSlf4jSoapMsgExtractor extends LoggingOutInterceptor {

    private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(LoggingInInterceptorXmlOnly.class);
    
    @Override
    protected void log(Logger logger, String message) {
        // just do nothing, because we don´t want CXF-Implementation to log,
        // we just want to Push the SOAP-Message to Logback -> Logstash -> Elasticsearch -> Kibana
    }
    
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            LOG.logOutboundSoapMessage(loggingMessage.getPayload().toString());
        }
        
        // This is just hook into CXF and get the SOAP-Message.
        // The returned String will never be logged somewhere.
        return ""; 
    }
}
