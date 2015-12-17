package de.codecentric.soap.plausibilitycheck;

import static de.codecentric.soap.plausibilitycheck.decisions.WeatherFields2Check.FIELDNAME;
import static de.codecentric.soap.plausibilitycheck.decisions.WeatherFields2Check.PRODUCT;
import static de.codecentric.soap.plausibilitycheck.decisions.WeatherFields2Check.RULENUMBER;
import static de.codecentric.soap.plausibilitycheck.decisions.WeatherFields2Check.RULEWORDS;
import static de.codecentric.soap.plausibilitycheck.decisions.WeatherFields2Check.SERVICE_METHOD;
import static de.codecentric.soap.plausibilitycheck.decisions.WeatherFields2Check.SHOULD_BE_CHECKED;

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

    private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(PlausibilityChecker.class);
    
    private static final String ERROR_TEXT = "Data in SOAP-Request is not valid for backend-processing!";
    
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
        VariableMap variables = Variables
                .putValue(PRODUCT, site.getProduct().getName())
                .putValue(SERVICE_METHOD, "getCityForecastByZIP")
                .putValue(FIELDNAME, "postalCode");
        
        DmnDecisionTableResult resultWeatherFields2Check = dmnEngine.evaluateDecisionTable(weatherFields2Check, variables);

        // Input the Result of first Decisiontable into the Ruletable
        if(resultWeatherFields2Check.getSingleResult() != null &&
                resultWeatherFields2Check.getSingleResult().getSingleEntry() != null) {
            
            boolean shouldFieldBeChecked = resultWeatherFields2Check.getSingleResult().getSingleEntry();
            
            variables = Variables
                    .putValue(PRODUCT, site.getProduct().getName())
                    .putValue(FIELDNAME, "postalCode")
                    .putValue(SHOULD_BE_CHECKED, shouldFieldBeChecked)
                    .putValue(RULENUMBER, site.getPostalCode())
                    .putValue(RULEWORDS, "");

            DmnDecisionTableResult resultWeatherRules = dmnEngine.evaluateDecisionTable(weatherRules, variables);
            
            if(resultWeatherRules.getSingleResult() != null
                    && "data valid".equals(resultWeatherRules.getSingleResult().getSingleEntry())) {
                // do nothing, everything is fine
                LOG.plausibilityCheckSuccessful();
            } else {
                //TODO: Rewrite from DMN-Messages
                throw LOG.plausibilityCheckDidntPass(new BusinessException(ERROR_TEXT));
            }
        } else {
            throw LOG.plausibilityCheckDidntPass(new BusinessException(ERROR_TEXT));
        } 
    }
}
