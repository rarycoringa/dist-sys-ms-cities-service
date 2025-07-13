package br.edu.ufrn.cities.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "geokeo.api")
public class GeoKeoApiProperties {
    private String key;
    private String url;

    public GeoKeoApiProperties() {}

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
