package de.codecentric.soap.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.codecentric.soap.Application;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.FaultConst;
import de.codecentric.soap.soaprawclient.SoapRawClient;
import de.codecentric.soap.soaprawclient.SoapRawClientFileUtils;
import de.codecentric.soap.soaprawclient.SoapRawClientResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port:8093") // This Configuration overrides the config of the embedded server, that is used (and re-used) in the Tests 
public class WeatherServiceEndpointXMLErrorTest {

	@Autowired
	private SoapRawClient soapRawClient;
	// TODO: Fix Configuration of Url is null
	private String url = "http://localhost:8092/soap-api/WeatherSoapService_1.0";
	
	@Test
	public void xmlErrorNotXmlSchemeCompliantUnderRootElementTest() throws BusinessException {
		checkXMLErrorNotSchemeCompliant("xmlErrorNotXmlSchemeCompliantUnderRootElementTest.xml");
	}
	
	@Test
	public void xmlErrorNotXmlSchemeCompliantRootElementTest() throws BusinessException {
		checkXMLErrorNotSchemeCompliant("xmlErrorNotXmlSchemeCompliantRootElementTest.xml");
	}
	
	private void checkXMLErrorNotSchemeCompliant(String testFileName) throws BusinessException {
		checkXMLError(testFileName, FaultConst.SCHEME_VALIDATION_ERROR);
	}	
	
	
	@Test
	public void xmlErrorSoapBodyTagMissingBracketTest() throws BusinessException {
		checkXMLErrorSyntacticallyIncorrect("xmlErrorSoapBodyTagMissingBracketTest.xml");
	}
	
	@Test
	public void xmlErrorSoapHeaderTagMissingBracketTest() throws BusinessException {
		checkXMLErrorSyntacticallyIncorrect("xmlErrorSoapHeaderTagMissingBracketTest.xml");
	}
	
	@Test
	public void xmlErrorSoapEnvelopeTagMissingBracketTest() throws BusinessException {
		checkXMLErrorSyntacticallyIncorrect("xmlErrorSoapEnvelopeTagMissingBracketTest.xml");
	}
	
	@Test
	public void xmlErrorXMLHeaderDefinitionMissingBracket() throws BusinessException {
		checkXMLErrorSyntacticallyIncorrect("xmlErrorXMLHeaderDefinitionMissingBracket.xml");
	}
	
	@Test
	public void xmlErrorSoapHeaderMissingSlash() throws BusinessException {
		checkXMLErrorSyntacticallyIncorrect("xmlErrorSoapHeaderMissingSlash.xml");
	}
	
	@Test
	public void xmlErrorXMLTagNotClosedInsideBodyTest() throws BusinessException {
		checkXMLErrorSyntacticallyIncorrect("xmlErrorXMLTagNotClosedInsideBodyTest.xml");
	}
	
	
	private void checkXMLErrorSyntacticallyIncorrect(String testFileName) throws BusinessException {
		checkXMLError(testFileName, FaultConst.SYNTACTICALLY_INCORRECT_XML_ERROR);
	}
	
	private void checkXMLError(String testFileName, FaultConst faultContent) throws BusinessException {
		// Given
		String schemaInvalidGetQuote = SoapRawClientFileUtils.readFileInClasspath2String(testFileName);
		soapRawClient.setUrl(url);
		
		// When
		SoapRawClientResponse soapRawResponse = soapRawClient.callSoapService(schemaInvalidGetQuote);
		
		// Then
		assertNotNull(soapRawResponse);
		assertEquals("Erwarte 500 Internal Server Error", 500, soapRawResponse.getHttpStatusCode());
        assertEquals(faultContent.getMessage(), soapRawResponse.getFaultstringValue());
        
        de.codecentric.namespace.weatherservice.exception.WeatherException weatherException = soapRawResponse.getUnmarshalledObjectFromSoapMessage(de.codecentric.namespace.weatherservice.exception.WeatherException.class);		
		assertNotNull("<soap:Fault><detail> has to contain a de.codecentric.namespace.weatherservice.exception.WeatherException",  weatherException);
		
		assertEquals("ExtremeRandomNumber", weatherException.getUuid());
		assertEquals("The correct BusinessId is missing in WeatherException according to XML-scheme.", faultContent.getId(), weatherException.getBusinessErrorId());
	}
}
