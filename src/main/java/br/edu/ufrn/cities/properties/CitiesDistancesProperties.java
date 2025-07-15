package br.edu.ufrn.cities.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cities.distances")
public class CitiesDistancesProperties {
    private double kilometersPerDegree;

    public CitiesDistancesProperties() {}

    public double getKilometersPerDegree() {
        return this.kilometersPerDegree;
    }

    public void setKilometersPerDegree(double kilometersPerDegree) {
        this.kilometersPerDegree = kilometersPerDegree;
    }
    
}
