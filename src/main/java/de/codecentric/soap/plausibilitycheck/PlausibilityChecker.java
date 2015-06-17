package de.codecentric.soap.plausibilitycheck;

import static org.easyrules.core.RulesEngineBuilder.aNewRulesEngine;

import java.util.ArrayList;
import java.util.List;

import org.easyrules.api.RulesEngine;

/**
 * Wrapper for EasyRules RulesEngine - registers and fires Rules
 */
public class PlausibilityChecker {

private List<AbstractRule> rules = new ArrayList<AbstractRule>();
	
	private List<String> messages = new ArrayList<String>();
	private PlausibilityStatus status = PlausibilityStatus.SUCCESS;  // default
	
	public static PlausibilityChecker aNewPlausibilityChecker() {
		return new PlausibilityChecker();
	}
	
	private PlausibilityChecker() {
		super();
	}
	
	/**
	 * Checking all the rules, that were added
	 */
	public PlausibilityStatus fireRules() {
		RulesEngine rulesEngine = aNewRulesEngine().build(); 
		
		for(AbstractRule rule : rules) {
			rulesEngine.registerRule(rule);
		}
		
		rulesEngine.fireRules();
		
		for(AbstractRule rule : rules) {
			if(PlausibilityStatus.ERROR.equals(rule.getStatus())) {
				status = PlausibilityStatus.ERROR;
				messages.add(rule.getMessage());
			}			
		}
		return status;
		
	}
	
	/**
	 * Resets Rule to defaults and adds it to rules, that can be fired with {@link #fireRules() fireRules()}
	 */
	public void addRule(AbstractRule rule) {
		rule.reset2Default();
		rules.add(rule);
	}
	
	public List<String> getMessages() {
		return messages;
	}
}
