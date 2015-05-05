package de.codecentric.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.codecentric.soap.common.InternalBusinessException;



public class TestUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);
	
	public static String readTestResourceFile2String(String fileName) {
		String requestFolder = "requests/";
		String file = null;
		
		try {
			URI filepathUrl =  TestUtils.class.getClassLoader().getResource(requestFolder + fileName).toURI();
			
			Path filepath = Paths.get(filepathUrl);
			
			file = Files.lines(filepath).collect(Collectors.joining());
		} catch (Exception e) {
			LOGGER.debug("Could not readFile." + e.getMessage());
			e.printStackTrace();
		}
		return file;
	}
	
	
	public static <T> T unmarshallXMLString(String xml, Class<T> jaxbClass) {
		return JAXB.unmarshal(new StringReader(xml), jaxbClass);
	}
	
	public static <T> T readSoapMessageFromFileAndUnmarshallBody(String fileName, Class<T> jaxbClass) throws InternalBusinessException {
		T unmarshalledObject = null;
		
		try {
			String requestFolder = "requests/";
			URI filepathUrl =  TestUtils.class.getClassLoader().getResource(requestFolder + fileName).toURI();
			
			FileInputStream fileInputStream = new FileInputStream(new File(filepathUrl));
			Document soapMessage = parseContent2Document(fileInputStream);
			unmarshalledObject = getUnmarshalledObjectFromSoapMessage(soapMessage, jaxbClass);			
		} catch (Exception exception) {
			throw new InternalBusinessException("Problem beim unmarshalling des JAXBObjects " + jaxbClass.getSimpleName() + " aus der SoapMessage.");
		}			
		return unmarshalledObject;
	}
	
	public static <T> T getUnmarshalledObjectFromSoapMessage(Document httpBody, Class<T> jaxbClass) throws InternalBusinessException {
		T unmarshalledObject = null;
		try {
			String namespaceUri = getNamespaceUriFromJaxbClass(jaxbClass);
			Node nodeFromSoapMessage = httpBody.getElementsByTagNameNS(namespaceUri, getXmlTagNameFromJaxbClass(jaxbClass)).item(0);
			JAXBElement<T> jaxbElement = unmarshallNode(nodeFromSoapMessage, jaxbClass);
			unmarshalledObject = jaxbElement.getValue();
		} catch (Exception exception) {
			throw new InternalBusinessException("Die SoapMessage enthaelt keine Representation des JAXBObjects " + jaxbClass.getSimpleName());
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
			throw new InternalBusinessException("Problem beim Unmarshalling der Node in das JAXBElement: " + exception.getMessage());
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
	
	public static Document parseContent2Document(InputStream contentAsStream) throws InternalBusinessException {
		Document parsedDoc = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        documentBuilderFactory.setNamespaceAware(true);
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        parsedDoc = documentBuilder.parse(contentAsStream);
		} catch (Exception exception) {
			throw new InternalBusinessException("Problem beim Parsen des InputStream in ein Document: " + exception.getMessage());
		}
		return parsedDoc;
	}	
	
}