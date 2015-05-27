package de.codecentric.soap.plausibilitycheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.Site;

public class PlausibilityChecker {

	private static List<String> errors = new ArrayList<String>();
	
	private static Validator validator;
	
//	public PlausibilityChecker() {
//		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//		validator = validatorFactory.getValidator();
//	}
	
	public static void checkPostcode(Site postcode) throws BusinessException {
		
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		
		Set<ConstraintViolation<Site>> violations = validator.validate(postcode);
		for(ConstraintViolation<Site> violation : violations) {
			String variableName = violation.getPropertyPath().toString();
			
			String message = violation.getMessage();
			
			String errorMessage = "Plausibilitycheck: " + postcode.getClass().getSimpleName() + "." + variableName + " " + message + "\n";
			System.out.println(errorMessage);
			errors.add(errorMessage);
		}
		
		if(!violations.isEmpty()) {
			String allMessages = "";
			for(String message : errors) {
				allMessages += message;
			}
			throw new BusinessException(allMessages);
		}
	}
}
