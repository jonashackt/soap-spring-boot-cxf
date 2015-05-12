package de.codecentric.soap.soaprawclient;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;

@Component
public class SoapRawClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoapRawClient.class);
	
	private String soapServiceUrl = null;
	
	public void setUrl(String soapServiceUrl) {
		this.soapServiceUrl = soapServiceUrl;
	}
	
	public SoapRawClientResponse callSoapService(String xmlFile) throws BusinessException {
		SoapRawClientResponse easyRawSoapResponse = null;
		
		if(soapServiceUrl == null) {
			throw new BusinessException("Could not Call Soap-Service, because the Url is not set: soapServiceUrl = " + soapServiceUrl);
		}
		
		try {
			easyRawSoapResponse = new SoapRawClientResponse();
			
			LOGGER.debug("Calling SoapService with POST on Apache HTTP-Client and configured URL: {}", soapServiceUrl);
			
			Response httpResponseContainer = Request
					.Post(soapServiceUrl)
					.bodyString(xmlFile, contentTypeTextXmlUtf8())
					.addHeader("SOAPAction", "\"urn:getQuote\"")
					.execute();
			
			HttpResponse httpResponse = httpResponseContainer.returnResponse();			
			easyRawSoapResponse.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
			easyRawSoapResponse.setHttpResponseBody(XmlUtils.parseFileStream2Document(httpResponse.getEntity().getContent()));
			
		} catch (Exception exception) {
			throw new BusinessException("Some Error accured while trying to Call SoapService for test: " + exception.getMessage());
		}		
		return easyRawSoapResponse;
	}

	private ContentType contentTypeTextXmlUtf8() {
		return ContentType.create(ContentType.TEXT_XML.getMimeType(), Consts.UTF_8);
	}
	
	
}
