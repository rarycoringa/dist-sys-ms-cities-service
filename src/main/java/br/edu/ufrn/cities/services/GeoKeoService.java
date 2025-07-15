package br.edu.ufrn.cities.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.cities.clients.GeoKeoApiClient;
import br.edu.ufrn.cities.properties.GeoKeoApiProperties;
import br.edu.ufrn.cities.records.geokeo.SearchResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class GeoKeoService {
    private static final Logger logger = LoggerFactory.getLogger(GeoKeoService.class);

    @Autowired
    private GeoKeoApiClient geoKeoApiClient;

    @Autowired
    private GeoKeoApiProperties geoKeoApiProperties;

    @Retry(name = "geokeo")
    @RateLimiter(name = "geokeo")
    public SearchResponse search(String query) {
        String key = geoKeoApiProperties.getKey();

        SearchResponse response = geoKeoApiClient.search(query, key);

        logger.info(
            "Search successfully retrieved by geokeo mcp service: {}",
            response.toString()
        );

        return response;
    }
}
