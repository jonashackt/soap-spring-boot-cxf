package de.codecentric.soap.rules;

import de.codecentric.soap.SoapApplication;
import de.codecentric.soap.internalmodel.MethodOfPayment;
import de.codecentric.soap.internalmodel.Product;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SoapApplication.class)
public class PlausibilityTests {

    @Value("classpath:rules/weatherFields2Check.dmn")
    private Resource weatherFields2CheckFile;
    
    @Value("classpath:rules/weatherRules.dmn")
    private Resource weatherRulesFile;
    
    @Rule
    public DmnEngineRule dmnEngineRule = new DmnEngineRule();

    public DmnEngine dmnEngine;
    
    public DmnDecision weatherFields2Check;
    public DmnDecision weatherRules;

    @Before
    public void parseDecision() throws IOException {
      dmnEngine = dmnEngineRule.getDmnEngine();
      weatherFields2Check = dmnEngine.parseDecision("weatherFields2Check", weatherFields2CheckFile.getInputStream());
      weatherRules = dmnEngine.parseDecision("weatherRules", weatherRulesFile.getInputStream());
    }
    
	@Test
	public void zipShouldAlwaysBeChecked() {		
	    // Given
	    VariableMap variables = Variables
	            .putValue(RuleKeys.PRODUCT, Product.ForecastProfessional.getName())
	            .putValue(RuleKeys.SERVICE_METHOD, "getCityForecastByZIP")
	            .putValue(RuleKeys.FIELDNAME, "postalCode");
	    // When
	    DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(weatherFields2Check, variables);
	    // Then
	    assertNotNull("no result found", result.getSingleResult());
	    assertEquals(true, result.getSingleResult().getSingleEntry());
	    
	    // Given
	    variables = Variables
                .putValue(RuleKeys.PRODUCT, Product.ForecastUltimateXL.getName())
                .putValue(RuleKeys.SERVICE_METHOD, "getCityWeatherByZIP")
                .putValue(RuleKeys.FIELDNAME, "postalCode");
	    // When
        result = dmnEngine.evaluateDecisionTable(weatherFields2Check, variables);
        // Then
        assertNotNull("no result found", result.getSingleResult());
        assertEquals(true, result.getFirstResult().getFirstEntry());
	}
	
	@Test
	public void forecastProfessionalContribution() {
	    // Given
	    VariableMap variables = Variables
	            .putValue(RuleKeys.PRODUCT, Product.ForecastProfessional.getName())
	            .putValue(RuleKeys.SERVICE_METHOD, "getCityForecastByZIP")
	            .putValue(RuleKeys.FIELDNAME, "contribution");
	    // When
	    DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(weatherFields2Check, variables);
	    // Then
	    assertNotNull(result.getSingleResult());
	    assertEquals(false, result.getSingleResult().getSingleEntry());
	}
	
	@Test
	public void postalCodeValid() {
	    // Given
	    VariableMap variables = Variables
	            .putValue(RuleKeys.PRODUCT, Product.ForecastProfessional.getName())
	            .putValue(RuleKeys.FIELDNAME, "postalCode")
	            .putValue(RuleKeys.SHOULD_BE_CHECKED, true)
	            .putValue(RuleKeys.RULENUMBER, 99423)
	            .putValue(RuleKeys.RULEWORDS, "");
	    // When
	    DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(weatherRules, variables);
	    // Then
	    assertTrue("Resultset should be empty, for Data is not valid", result.isEmpty());
	}
	
	@Test
    public void postalCodeNotValid() {
        // Given
	    VariableMap variables = Variables
                .putValue(RuleKeys.PRODUCT, Product.ForecastBasic.getName())
                .putValue(RuleKeys.FIELDNAME, "postalCode")
                .putValue(RuleKeys.SHOULD_BE_CHECKED, true)
                .putValue(RuleKeys.RULENUMBER, 9942388)
                .putValue(RuleKeys.RULEWORDS, "");
	    // When
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        // Then
        assertNotNull(result.getFirstResult());
        String errorMsg = result.getFirstResult().getEntry(RuleKeys.ERROR_MSG);
        assertThat(errorMsg, containsString("The postalcode isn´t in the correct range between 01001 and 99999"));
        
        // Given
        variables = Variables
                .putValue(RuleKeys.PRODUCT, Product.ForecastProfessional.getName())
                .putValue(RuleKeys.FIELDNAME, "postalCode")
                .putValue(RuleKeys.SHOULD_BE_CHECKED, true)
                .putValue(RuleKeys.RULENUMBER, 1000)
                .putValue(RuleKeys.RULEWORDS, "");
        // When
        result = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        // Then
        assertNotNull(result.getFirstResult());
        errorMsg = result.getFirstResult().getEntry(RuleKeys.ERROR_MSG);
        assertThat(errorMsg, containsString("The postalcode isn´t in the correct range between 01001 and 99999"));
        
        // Given
        variables = Variables
                .putValue(RuleKeys.PRODUCT, Product.ForecastProfessional.getName())
                .putValue(RuleKeys.FIELDNAME, "postalCode")
                .putValue(RuleKeys.SHOULD_BE_CHECKED, true)
                .putValue(RuleKeys.RULENUMBER, 100000)
                .putValue(RuleKeys.RULEWORDS, "");
        // When
        result = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        // Then
        assertNotNull(result.getFirstResult());
        errorMsg = result.getFirstResult().getEntry(RuleKeys.ERROR_MSG);
        assertThat(errorMsg, containsString("The postalcode isn´t in the correct range between 01001 and 99999"));
    }
	
	@Test
	public void checkPostalcodeDependendOn2DecisionTables() {
	    // Given
	    VariableMap variables = Variables
                .putValue(RuleKeys.PRODUCT, Product.ForecastProfessional.getName())
                .putValue(RuleKeys.SERVICE_METHOD, "getCityForecastByZIP")
                .putValue(RuleKeys.FIELDNAME, "postalCode");
	    // When
        DmnDecisionTableResult resultWeatherFields2Check = dmnEngine.evaluateDecisionTable(weatherFields2Check, variables);
        
        // Then
        assertNotNull("no result found", resultWeatherFields2Check.getSingleResult());
        
        // Given the Result of first Test as Input for second Test
        boolean shouldFieldBeChecked = resultWeatherFields2Check.getSingleResult().getSingleEntry();
        assertEquals(true, shouldFieldBeChecked);
        
        variables = Variables
                .putValue(RuleKeys.PRODUCT, Product.ForecastProfessional.getName())
                .putValue(RuleKeys.FIELDNAME, "postalCode")
                .putValue(RuleKeys.SHOULD_BE_CHECKED, shouldFieldBeChecked)
                .putValue(RuleKeys.RULENUMBER, 99423)
                .putValue(RuleKeys.RULEWORDS, "");

        // When
        DmnDecisionTableResult resultWeatherRules = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        
        // Then
        assertTrue("Resultset should be empty, for Data is not valid", resultWeatherRules.isEmpty());
	}
	
	@Test
    public void methodOfPaymentCheckWrong() {
        // Given
        VariableMap variables = Variables
                .putValue(RuleKeys.PRODUCT, "")
                .putValue(RuleKeys.FIELDNAME, "methodOfPayment")
                .putValue(RuleKeys.SHOULD_BE_CHECKED, true)
                .putValue(RuleKeys.RULENUMBER, 0)
                .putValue(RuleKeys.RULEWORDS, "Cash");
        // When
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        // Then
        assertNotNull(result.getFirstResult());
        String errorMsg = result.getFirstResult().getEntry(RuleKeys.ERROR_MSG);
        assertThat(errorMsg, containsString("Sorry, we don´t support this method of payment"));
        
        // Given
        variables = Variables
                .putValue(RuleKeys.PRODUCT, "")
                .putValue(RuleKeys.FIELDNAME, "methodOfPayment")
                .putValue(RuleKeys.SHOULD_BE_CHECKED, true)
                .putValue(RuleKeys.RULENUMBER, 0)
                .putValue(RuleKeys.RULEWORDS, "MasterCard");
        // When
        result = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        // Then
        assertNotNull(result.getFirstResult());
        errorMsg = result.getFirstResult().getEntry(RuleKeys.ERROR_MSG);
        assertThat(errorMsg, containsString("Sorry, we don´t support this method of payment"));
    }
	
	@Test
    public void methodOfPaymentCheckCorrect() {
        // Given
        VariableMap variables = Variables
                .putValue(RuleKeys.PRODUCT, "")
                .putValue(RuleKeys.FIELDNAME, "methodOfPayment")
                .putValue(RuleKeys.SHOULD_BE_CHECKED, true)
                .putValue(RuleKeys.RULENUMBER, 0)
                .putValue(RuleKeys.RULEWORDS, MethodOfPayment.Paypal.getName());
        // When
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        // Then
        assertTrue("Resultset should be empty, for Data is not valid", result.isEmpty());
    }
}
