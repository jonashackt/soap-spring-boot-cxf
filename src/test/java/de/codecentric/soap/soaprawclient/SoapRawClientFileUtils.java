package de.codecentric.soap.soaprawclient;

import java.io.InputStream;

import de.codecentric.soap.FileUtils;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;

public class SoapRawClientFileUtils {

	private static final String XML_FILES_FOLDER = "requests/";
	
	public static String readFileInClasspath2String(String fileName) throws BusinessException {
		return FileUtils.readFileInClasspath2String(XML_FILES_FOLDER, fileName);
	}

	public static <T> T readSoapMessageFromFileAndUnmarshallBody2Object(String fileName, Class<T> jaxbClass) throws BusinessException {
		InputStream xmlFile = FileUtils.readFileInClasspath2InputStream(XML_FILES_FOLDER, fileName);
		return XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(xmlFile, jaxbClass);
	}	
}