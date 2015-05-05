package de.codecentric.soap;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.codecentric.soap.common.InternalBusinessException;



@Component
public class EasyRawSOAPTester {

	private static final Logger LOGGER = LoggerFactory.getLogger(EasyRawSOAPTester.class);
	
	private String soapServiceUrl;
	
	public void setUrl(String soapServiceUrl) {
		this.soapServiceUrl = soapServiceUrl;
	}
	
	public EasyRawSoapResponse callSoapService(String xmlFile) throws InternalBusinessException {
		EasyRawSoapResponse easyRawSoapResponse = null;
		
		try {
			easyRawSoapResponse = new EasyRawSoapResponse();
			
			LOGGER.debug("Calling SoapService with POST on Apache HTTP-Client and configured URL: {}", soapServiceUrl);
			
			Response httpResponseContainer = Request
					.Post(soapServiceUrl)
					.bodyString(xmlFile, contentTypeTextXmlUtf8())
					.addHeader("SOAPAction", "\"urn:getQuote\"")
					.execute();
			
			HttpResponse httpResponse = httpResponseContainer.returnResponse();			
			easyRawSoapResponse.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
			easyRawSoapResponse.setHttpResponseBody(TestUtils.parseContent2Document(httpResponse.getEntity().getContent()));
			
		} catch (Exception exception) {
			throw new InternalBusinessException("Some Error accured while trying to Call SoapService for test: " + exception.getMessage());
		}		
		return easyRawSoapResponse;
	}
	
	private ContentType contentTypeTextXmlUtf8() {
		return ContentType.create(ContentType.TEXT_XML.getMimeType(), Consts.UTF_8);
	}
	
	
}
