package de.codecentric.soap;

import java.io.StringReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	
	public static String unmarshallXML2String(String getQuoteXml) {
		return JAXB.unmarshal(new StringReader(getQuoteXml), String.class);
	}
	
}