package de.codecentric.soap.internalmodel;

public enum Product {

    ForecastBasic("ForecastBasic"),
    ForecastProfessional("ForecastProfessional"),
    ForecastUltimateXL("ForecastUltimateXL"),
    Unknown("Unknown");
    
    private String name;
    
    private Product(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
