package de.codecentric.soap.configuration.logging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.slf4j.MDC;

public class LoggingInInterceptorXmlOnly extends LoggingInInterceptor {
    
	private static final String SOAP_METHOD_LOG_NAME = "soap-method-name";
	
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        StringBuilder buffer = new StringBuilder();
        
        String soapMethodName = extractSoapMethodNameFromHttpHeader(loggingMessage.getHeader().toString());
        MDC.put(SOAP_METHOD_LOG_NAME, soapMethodName);
        
        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            buffer.append("0000 >>> Inbound Message:\n");
            buffer.append(loggingMessage.getPayload());
        }
        return buffer.toString();
    }

	private String extractSoapMethodNameFromHttpHeader(String header) {
		// Regex for SOAPAction with "urn:" inside - Without Escapes for Java-String: (?<=SOAPAction=\["urn:)[a-zA-Z]+(?=\"]) 
	    String regex_find_soap_method_name_in_http_header_format_with_urn = "(?<=SOAPAction=\\[\"urn:)[a-zA-Z]+(?=\"])";
	    // Regex for SOAPAction with an URL inside - Without Escapes for Java-String: (?<=SOAPAction=\[")[:./a-zA-Z]+(?=\"]) 
		String regex_find_soap_method_name_in_http_header_format_with_url = "(?<=SOAPAction=\\[\")[:./a-zA-Z]+(?=\"])";
		
        Matcher matcher = buildMatcher(header, regex_find_soap_method_name_in_http_header_format_with_urn);
        if (matcher.find()) {
            return matcher.group(0);
        }
        matcher = buildMatcher(header, regex_find_soap_method_name_in_http_header_format_with_url);
        if (matcher.find()) {
            return eliminateStartingUrl(matcher.group(0));
        }
        return ""; // This should´nt happen in reality, because the SOAP-Spec demands the SOAP-Service-Method as SOAPAction
	}

    private String eliminateStartingUrl(String methodWithUrl) {
        int lastSlash = methodWithUrl.lastIndexOf("/");
        return methodWithUrl.substring(lastSlash + 1);
    }

    private Matcher buildMatcher(String string2SearchIn, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string2SearchIn);
    }
}
