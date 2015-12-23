package de.codecentric.soap.logging;

public enum ElasticsearchField {

    SOAP_METHOD_LOG_NAME("soap-method-name"),
    HTTP_HEADER_INBOUND("http-header-inbound"),
    SOAP_MESSAGE_INBOUND("soap-message-inbound"),
    SOAP_MESSAGE_OUTBOUND("soap-message-outbound"),
    ID_KEY("service-call-id"),
    TIME_INBOUND("time-inbound"),
    TIME_OUTBOUND("time-outbound"),
    TIME_CALLTIME("time-calltime");
    
    private String fieldname;
    
    private ElasticsearchField(String fieldname) {
       this.fieldname = fieldname; 
    }
    
    public String getName() {
        return fieldname;
    }
}
