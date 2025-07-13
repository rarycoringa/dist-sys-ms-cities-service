package br.edu.ufrn.cities.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cities")
public class CitiesProperties {
    private double kilometersPerDegree;

    public CitiesProperties() {}

    public double getKilometersPerDegree() {
        return this.kilometersPerDegree;
    }

    public void setKilometersPerDegree(double kilometersPerDegree) {
        this.kilometersPerDegree = kilometersPerDegree;
    }
    
}
