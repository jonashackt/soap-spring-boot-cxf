package de.codecentric.soap.soaprawclient;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.common.XmlUtils;

public class SoapRawClientFileUtils {

	private static final String XML_FILES_FOLDER = "requests/";
	private static final String ERROR_LOADING_FILE = "Loading Testfile encounters problems: ";
	
	public static String readFileInClasspath2String(String fileName) throws BusinessException {
		String file;
		try {
			Path filepath = Paths.get(buildFileUri(fileName));
			file = Files.lines(filepath).collect(Collectors.joining());
		} catch (Exception exception) {
			throw new BusinessException(ERROR_LOADING_FILE + exception.getMessage());
		}
		return file;
	}

	public static <T> T readSoapMessageFromFileAndUnmarshallBody2Object(String fileName, Class<T> jaxbClass) throws BusinessException {
		return XmlUtils.readSoapMessageFromStreamAndUnmarshallBody2Object(readFileInClasspath2InputStream(fileName), jaxbClass);
	}
	
	public static InputStream readFileInClasspath2InputStream(String fileName) throws BusinessException {
		InputStream inputStream = null;
		try {
			Path filepath = Paths.get(buildFileUri(fileName));
			inputStream = Files.newInputStream(filepath);
		} catch (Exception exception) {
			throw new BusinessException(ERROR_LOADING_FILE + exception.getMessage());
		}
		return inputStream;		
	}		
	
	private static URI buildFileUri(String fileName) throws URISyntaxException, BusinessException {
		URL fileInClasspath = SoapRawClientFileUtils.class.getClassLoader().getResource(XML_FILES_FOLDER + fileName);
		if(fileInClasspath == null)
			throw new BusinessException("Filepath seems to be wrong.");
		return fileInClasspath.toURI();
	}
	
}