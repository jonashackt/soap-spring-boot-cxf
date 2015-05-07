package de.codecentric.soap.common;

import java.io.InputStream;
import java.io.StringReader;
import java.lang.annotation.Annotation;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLUtils {

	public static <T> T readSoapMessageFromStreamAndUnmarshallBody2Object(InputStream fileStream, Class<T> jaxbClass) throws InternalBusinessException {
		T unmarshalledObject = null;
		try {
			Document soapMessage = parseFileStream2Document(fileStream);
			unmarshalledObject = getUnmarshalledObjectFromSoapMessage(soapMessage, jaxbClass);			
		} catch (Exception exception) {
			throw new InternalBusinessException("Problem unmarshalling JAXBObject " + jaxbClass.getSimpleName() + " from SoapMessage.");
		}			
		return unmarshalledObject;
	}
	
	public static <T> T unmarshallXMLString(String xml, Class<T> jaxbClass) {
		return JAXB.unmarshal(new StringReader(xml), jaxbClass);
	}	
	
	public static <T> T getUnmarshalledObjectFromSoapMessage(Document httpBody, Class<T> jaxbClass) throws InternalBusinessException {
		T unmarshalledObject = null;
		try {
			String namespaceUri = getNamespaceUriFromJaxbClass(jaxbClass);
			Node nodeFromSoapMessage = httpBody.getElementsByTagNameNS(namespaceUri, getXmlTagNameFromJaxbClass(jaxbClass)).item(0);
			JAXBElement<T> jaxbElement = unmarshallNode(nodeFromSoapMessage, jaxbClass);
			unmarshalledObject = jaxbElement.getValue();
		} catch (Exception exception) {
			throw new InternalBusinessException("SoapMessage doesn't contain representation of JAXBObject " + jaxbClass.getSimpleName());
		}
		return unmarshalledObject;
	}
	
	public static <T> JAXBElement<T> unmarshallNode(Node biproException, Class<T> jaxbClassName) throws InternalBusinessException {
		JAXBElement<T> jaxbElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(jaxbClassName);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			jaxbElement = unmarshaller.unmarshal(new DOMSource(biproException), jaxbClassName);
		} catch (Exception exception) {
			throw new InternalBusinessException("Problem unmarshalling node into JAXBElement: " + exception.getMessage());
		}		
		return jaxbElement;
	}	
	
	public static <T> String getNamespaceUriFromJaxbClass(Class<T> jaxbClass) {
		String nsURI = "";
	    for(Annotation annotation: jaxbClass.getPackage().getAnnotations()){
	        if(annotation.annotationType() == XmlSchema.class){
	            nsURI = ((XmlSchema)annotation).namespace();
	            break;
	        }
	    }
	    return nsURI;
	}
	
	public static <T> String getXmlTagNameFromJaxbClass(Class<T> jaxbClass) {
		String xmlTagName = "";
	    for(Annotation annotation: jaxbClass.getAnnotations()){
	        if(annotation.annotationType() == XmlRootElement.class){
	            xmlTagName = ((XmlRootElement)annotation).name();
	            break;
	        }
	    }
	    return xmlTagName;
	}
	
	public static Document parseFileStream2Document(InputStream contentAsStream) throws InternalBusinessException {
		Document parsedDoc = null;
		try {
	        parsedDoc = setUpDocumentBuilder().parse(contentAsStream);
		} catch (Exception exception) {
			throw new InternalBusinessException("Problem while parsing the InputStream into Document: " + exception.getMessage());
		}
		return parsedDoc;
	}


	private static DocumentBuilder setUpDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		return documentBuilderFactory.newDocumentBuilder();
	}	
	
}