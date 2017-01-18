package de.codecentric.soap.rules;


import de.codecentric.namespace.weatherservice.general.ForecastRequest;
import de.codecentric.soap.common.BusinessException;
import de.codecentric.soap.internalmodel.Weather;
import de.codecentric.soap.logging.SoapFrameworkLogger;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class Rules {

    private static final SoapFrameworkLogger LOG = SoapFrameworkLogger.getLogger(Rules.class);
    
    public static final String ERROR_TEXT = "Data in SOAP-Request is not valid for backend-processing!";
    
    @Value("classpath:rules/facadeModeActive.dmn")
    private Resource facadeModeActiveFile;
    
    private DmnDecision facadeModeActive;
    
    @Autowired
    private DmnEngine dmnEngine;
    
    // Called after DI of needed DmnEngine
    @PostConstruct
    private void setUP() throws IOException {
        facadeModeActive = dmnEngine.parseDecision("facadeModeActive", facadeModeActiveFile.getInputStream());
    }

    public boolean activateFacadeMode(ForecastRequest forecastRequest) throws BusinessException {

        VariableMap variables = Variables
                .putValue(RuleKeys.POSTAL_CODE, forecastRequest.getZIP())
                .putValue(RuleKeys.FLAG_COLOR, forecastRequest.getFlagcolor());

        DmnDecisionTableResult resultFacadeModeActive = dmnEngine.evaluateDecisionTable(facadeModeActive, variables);

        return resultFacadeModeActive.getFirstResult().getEntry(RuleKeys.FACADE_MODE_ACTIVE);
    }

}
