package de.codecentric.soap.logging;

import static org.junit.Assert.*;

import org.junit.Test;

public class CxfLoggingSoapActionUtilTest {

    @Test
    public void testSoapActionStringWithUrn() {
     // Given
        String soapActionString = "{accept-encoding=[gzip,deflate], "
                + "connection=[Keep-Alive], "
                + "Content-Length=[260], "
                + "content-type=[text/xml;charset=UTF-8], "
                + "host=[localhost:8095], "
                + "SOAPAction=[\"urn:GetWeatherInformation\"], "
                + "user-agent=[Apache-HttpClient/4.1.1 (java 1.5)]}";
        
        // When
        String soapMethodName = CxfLoggingSoapActionUtil.extractSoapMethodNameFromHttpHeader(soapActionString);
        
        // Then
        assertEquals("GetWeatherInformation", soapMethodName);
    }
    
    @Test
    public void testSoapActionStringWithUrnWithoutQuotes() {
     // Given
        String soapActionString = "{accept-encoding=[gzip,deflate], "
                + "connection=[Keep-Alive], "
                + "Content-Length=[260], "
                + "content-type=[text/xml;charset=UTF-8], "
                + "host=[localhost:8095], "
                + "SOAPAction=[urn:GetWeatherInformation], "
                + "user-agent=[Apache-HttpClient/4.1.1 (java 1.5)]}";
        
        // When
        String soapMethodName = CxfLoggingSoapActionUtil.extractSoapMethodNameFromHttpHeader(soapActionString);
        
        // Then
        assertEquals("GetWeatherInformation", soapMethodName);
    }
    
    @Test
    public void testSoapActionStringWithUrl() {
        // Given
        String soapActionString = "{accept-encoding=[gzip,deflate], "
                + "connection=[Keep-Alive], "
                + "Content-Length=[260], "
                + "content-type=[text/xml;charset=UTF-8], "
                + "host=[localhost:8095], "
                + "SOAPAction=[\"http://www.codecentric.de/namespace/weatherservice/GetWeatherInformation\"], "
                + "user-agent=[Apache-HttpClient/4.1.1 (java 1.5)]}";
        
        // When
        String soapMethodName = CxfLoggingSoapActionUtil.extractSoapMethodNameFromHttpHeader(soapActionString);
        
        // Then
        assertEquals("GetWeatherInformation", soapMethodName);
    }
}
