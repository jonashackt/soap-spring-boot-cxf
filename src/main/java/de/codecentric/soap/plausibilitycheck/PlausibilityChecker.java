package de.codecentric.soap.plausibilitycheck;

import static org.easyrules.core.RulesEngineBuilder.aNewRulesEngine;

import org.easyrules.api.RulesEngine;

import de.codecentric.soap.common.BusinessException;

/**
 * Wrapper for EasyRules RulesEngine - registers and fires Rules
 */
public class PlausibilityChecker {

	private static RulesEngine rulesEngine = aNewRulesEngine().build();  
	
	public static PlausibilityResult checkRule(AbstractRule regel) {
		rulesEngine.registerRule(regel);
		rulesEngine.fireRules();
		return regel.getResult();
	}
	
	/**
	 * Checkt, ob Fehler im PlausiErgebnis sind und wirft bei Vorhandensein eine BusinessException.
	 */
	public static void checkForError(PlausibilityResult plausibilityResult) throws BusinessException {
		
		if(PlausibilityStatus.ERROR.equals(plausibilityResult.getStatus()))
				throw new BusinessException(plausibilityResult.getMessage());
	}
}
