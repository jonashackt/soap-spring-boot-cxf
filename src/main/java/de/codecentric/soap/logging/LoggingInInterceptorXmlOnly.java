package de.codecentric.soap.logging;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.slf4j.MDC;

public class LoggingInInterceptorXmlOnly extends LoggingInInterceptor {

	private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(LoggingInInterceptorXmlOnly.class);
	
	private static final String SOAP_METHOD_LOG_NAME = "soap-method-name";
	
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        String headers = loggingMessage.getHeader().toString();
        
        String soapMethodName = CxfLoggingSoapActionUtil.extractSoapMethodNameFromHttpHeader(headers);
        MDC.put(SOAP_METHOD_LOG_NAME, soapMethodName);
        
        StringBuilder buffer = new StringBuilder();
        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            LOG.logSoapMessage(loggingMessage.getPayload().toString());
        }
        
        LOG.logHttpHeader(headers);
        buffer.append("Called Service Inbound");
        
        return buffer.toString();
    }

}
