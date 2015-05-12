package de.codecentric.soap.common;

import java.io.InputStream;
import java.io.StringReader;
import java.lang.annotation.Annotation;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlUtils {

	public static <T> T readSoapMessageFromStreamAndUnmarshallBody2Object(InputStream fileStream, Class<T> jaxbClass) throws BusinessException {
		T unmarshalledObject = null;
		try {
			Document soapMessage = parseFileStream2Document(fileStream);
			unmarshalledObject = getUnmarshalledObjectFromSoapMessage(soapMessage, jaxbClass);			
		} catch (Exception exception) {
			throw new BusinessException("Problem beim unmarshalling des JAXBObjects " + jaxbClass.getSimpleName() + " aus der SoapMessage.");
		}			
		return unmarshalledObject;
	}
	
	public static <T> T unmarshallXMLString(String xml, Class<T> jaxbClass) {
		return JAXB.unmarshal(new StringReader(xml), jaxbClass);
	}	
	
	public static <T> T getUnmarshalledObjectFromSoapMessage(Document httpBody, Class<T> jaxbClass) throws BusinessException {
		T unmarshalledObject = null;
		try {
			String namespaceUri = getNamespaceUriFromJaxbClass(jaxbClass);
			Node nodeFromSoapMessage = httpBody.getElementsByTagNameNS(namespaceUri, getXmlTagNameFromJaxbClass(jaxbClass)).item(0);
			JAXBElement<T> jaxbElement = unmarshallNode(nodeFromSoapMessage, jaxbClass);
			unmarshalledObject = jaxbElement.getValue();
		} catch (Exception exception) {
			throw new BusinessException("Die SoapMessage enthaelt keine Representation des JAXBObjects " + jaxbClass.getSimpleName());
		}
		return unmarshalledObject;
	}
	
	public static <T> JAXBElement<T> unmarshallNode(Node biproException, Class<T> jaxbClassName) throws BusinessException {
		JAXBElement<T> jaxbElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(jaxbClassName);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			jaxbElement = unmarshaller.unmarshal(new DOMSource(biproException), jaxbClassName);
		} catch (Exception exception) {
			throw new BusinessException("Problem beim Unmarshalling der Node in das JAXBElement: " + exception.getMessage());
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
	
	public static Document parseFileStream2Document(InputStream contentAsStream) throws BusinessException {
		Document parsedDoc = null;
		try {
	        parsedDoc = setUpDocumentBuilder().parse(contentAsStream);
		} catch (Exception exception) {
			throw new BusinessException("Problem beim Parsen des InputStream in ein Document: " + exception.getMessage());
		}
		return parsedDoc;
	}

	private static DocumentBuilder setUpDocumentBuilder() throws BusinessException {
		DocumentBuilder documentBuilder = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException parserConfigurationException) {
			throw new BusinessException("Problem beim Erstellen des DocumentBuilders: " + parserConfigurationException.getMessage());
		}
		return documentBuilder;
	}
	
	public static Document marhallJaxbElement(Object jaxbElement) throws BusinessException {
		Document jaxbDoc = null;
		try {
			Marshaller marshaller = setUpMarshaller(jaxbElement.getClass());
			jaxbDoc = createNewDocument();        	
			marshaller.marshal(jaxbElement, jaxbDoc);			
		} catch (Exception exception) {
			throw new BusinessException("Problem beim marshallen des JAXBElements in ein Document: " + exception.getMessage());
		}
        return jaxbDoc;
	}

	private static Document createNewDocument() throws BusinessException {
		return setUpDocumentBuilder().newDocument();
	}

	private static <T> Marshaller setUpMarshaller(Class<T> jaxbElementClass) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbElementClass);
		return jaxbContext.createMarshaller();
	}
	
	
	public static Element appendAsChildElement2NewElement(Document document) throws BusinessException {
		Document docWithDocumentAsChild = copyDocumentAsChildelementUnderNewDocument(document);
		return docWithDocumentAsChild.getDocumentElement();
	}

	private static Document copyDocumentAsChildelementUnderNewDocument(Document document) throws BusinessException {
		Document docWithDocumentAsChild = createNewDocument();
		docWithDocumentAsChild.appendChild(docWithDocumentAsChild.createElement("root2kick"));			
		Node importedNode = docWithDocumentAsChild.importNode(document.getDocumentElement(), true);
		docWithDocumentAsChild.getDocumentElement().appendChild(importedNode);
		return docWithDocumentAsChild;
	}
	
}