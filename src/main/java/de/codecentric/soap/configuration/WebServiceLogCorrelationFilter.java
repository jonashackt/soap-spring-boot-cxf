package de.codecentric.soap.configuration;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;

public class WebServiceLogCorrelationFilter implements Filter {

    public static final String ID_KEY = "service-call-id";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Put an unique-Logging-Id to the logback Mapped Diagnostic Context to correlate
        // against one Customer-Request, see http://logback.qos.ch/manual/mdc.html
        MDC.put(ID_KEY, generateUUID());
        try {
            chain.doFilter(request, response);
        } finally {
            // finally remove unique-Logging-Id, so that it could´nt be accidentally
            // reused for another Customer-Request
            MDC.remove(ID_KEY);
        }       
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void destroy() {}

}
