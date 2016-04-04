package de.codecentric.soap.plausibilitycheck;


import java.io.IOException;

import javax.annotation.PostConstruct;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.Weather;
import de.codecentric.soap.logging.SoapFrameworkLogger;

@Component
public class PlausibilityChecker {

    public static final String PRODUCT = "product";
    public static final String SERVICE_METHOD = "servicemethod";
    public static final String FIELDNAME = "fieldname";
    
    public static final String SHOULD_BE_CHECKED = "check";
    public static final String RULENUMBER = "rulenumber";
    public static final String RULEWORDS = "rulewords";
    public static final String ERROR_MSG = "errorMsg";
    
    private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(PlausibilityChecker.class);
    
    public static final String ERROR_TEXT = "Data in SOAP-Request is not valid for backend-processing!";
    
    @Value("classpath:rules/weatherFields2Check.dmn")
    private Resource weatherFields2CheckFile;
    
    @Value("classpath:rules/weatherRules.dmn")
    private Resource weatherRulesFile;
    
    private DmnDecision weatherFields2Check;
    private DmnDecision weatherRules; 
    
    @Autowired
    private DmnEngine dmnEngine;
    
    // Called after DI of needed DmnEngine
    @PostConstruct
    private void setUP() throws IOException {
        weatherFields2Check = dmnEngine.parseDecision("weatherFields2Check", weatherFields2CheckFile.getInputStream());
        weatherRules = dmnEngine.parseDecision("weatherRules", weatherRulesFile.getInputStream());
    }
    
    public void checkGetCityForecastByZIP(Weather site) throws BusinessException {
        String webServiceMethod2CheckFor = "getCityForecastByZIP";
        checkPostalCode(site, webServiceMethod2CheckFor);
        checkMethodOfPayment(site, webServiceMethod2CheckFor);
    }

    private void checkPostalCode(Weather weather, String webServiceMethod2CheckFor) throws BusinessException {
        String fieldName = "postalCode";

        VariableMap variables = Variables
                .putValue(PRODUCT, weather.getProduct().getName())
                .putValue(FIELDNAME, fieldName)
                .putValue(SHOULD_BE_CHECKED, shouldFieldBeChecked(weather, webServiceMethod2CheckFor, fieldName))
                .putValue(RULENUMBER, weather.getPostalCode())
                .putValue(RULEWORDS, "");

        checkInWeatherRulesIfDataValid(variables);
    }
    
    private void checkMethodOfPayment(Weather weather, String webServiceMethod2CheckFor) throws BusinessException {
        String fieldName = "methodOfPayment";
        
        VariableMap variables = Variables
                .putValue(PRODUCT, weather.getProduct().getName())
                .putValue(FIELDNAME, fieldName)
                .putValue(SHOULD_BE_CHECKED, shouldFieldBeChecked(weather, webServiceMethod2CheckFor, fieldName))
                .putValue(RULENUMBER, 0)
                .putValue(RULEWORDS, weather.getUser().getMethodOfPayment().getName());

        checkInWeatherRulesIfDataValid(variables);
    }
    
    private void checkInWeatherRulesIfDataValid(VariableMap variables) throws BusinessException {
        DmnDecisionTableResult resultWeatherRules = dmnEngine.evaluateDecisionTable(weatherRules, variables);
        
        if(!resultWeatherRules.isEmpty()) {
            String errorMsg = resultWeatherRules.getFirstResult().getEntry("errorMsg");
            throw LOG.plausibilityCheckDidntPass(variables.get(FIELDNAME).toString(), new BusinessException(errorMsg));
        }
        LOG.plausibilityCheckSuccessfulWithoutErrors();
    }
    
    private boolean shouldFieldBeChecked(Weather weather, String webServiceMethod2CheckFor, String fieldName) throws BusinessException {
        VariableMap variables = Variables
                .putValue(PRODUCT, weather.getProduct().getName())
                .putValue(SERVICE_METHOD, webServiceMethod2CheckFor)
                .putValue(FIELDNAME, fieldName);
        
        DmnDecisionTableResult resultWeatherFields2Check = dmnEngine.evaluateDecisionTable(weatherFields2Check, variables);

        if(resultWeatherFields2Check.getSingleResult() != null &&
                resultWeatherFields2Check.getSingleResult().getSingleEntry() != null) {
            // There should be a result, because we describe in the DMN-Table, if some field should be checked
            return resultWeatherFields2Check.getSingleResult().getSingleEntry();
        } else {
            throw LOG.plausibilityCheckDidntPass(fieldName, new BusinessException(ERROR_TEXT));
        }
    }
}
