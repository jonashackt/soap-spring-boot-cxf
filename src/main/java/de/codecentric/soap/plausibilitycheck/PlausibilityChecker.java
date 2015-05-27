package de.codecentric.soap.plausibilitycheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.Site;

public class PlausibilityChecker {

	private static List<String> errors = new ArrayList<String>();
	
	private static Validator validator;
	
	/**
	 * @throws BusinessException, if plausibility check results in errors
	 */
	public static void check(Site site, Class<? extends Default> validationClass) throws BusinessException {
		
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		
		Set<ConstraintViolation<Site>> violations = validator.validate(site, validationClass);
		for(ConstraintViolation<Site> violation : violations) {
			String variableName = violation.getPropertyPath().toString();
			
			String message = violation.getMessage();
			
			String errorMessage = "Plausibilitycheck: " + site.getClass().getSimpleName() + "." + variableName + " " + message + "\n";
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
