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

public class Plausibility {
	
	private static Validator validator = setUpValidator();
	
	/**
	 * @throws BusinessException, if plausibility check results in errors
	 */
	public static <T> void check(T pojo2Validate, Class<? extends Default> validationClass) throws BusinessException {
		Set<ConstraintViolation<T>> violations = validator.validate(pojo2Validate, validationClass);
		List<String> errors = fillErrorsIfValidationFailes(pojo2Validate, violations);
		if(validationFailed(errors)) {			
			// TODO: Handle List of Error more elegant
			throw new BusinessException(appendAllErrorsToOneString(errors));
		}
	}

	private static <T> List<String> fillErrorsIfValidationFailes(T pojo2Validate, Set<ConstraintViolation<T>> violations) {
		List<String> errors = new ArrayList<String>();
		
		for(ConstraintViolation<T> violation : violations) {
			String variableName = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.add("Plausibilitycheck: " + pojo2Validate.getClass().getSimpleName() + "." + variableName + " " + message + "\n");
		}
		return errors;
	}
	
	private static String appendAllErrorsToOneString(List<String> errors) {
		String allMessages = "";
		for(String message : errors) {
			allMessages += message;
		}
		return allMessages;
	}
	
	private static boolean validationFailed(List<String> errors) {
		return !errors.isEmpty();
	}
	
	private static Validator setUpValidator() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		return validatorFactory.getValidator();
	}
}
