package br.edu.ufrn.cities.services;

import org.bouncycastle.jcajce.provider.keystore.util.AdaptingKeyStoreSpi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.cities.clients.ClientAIClient;
import br.edu.ufrn.cities.clients.DistancesClient;
import br.edu.ufrn.cities.clients.GeoKeoApiClient;
import br.edu.ufrn.cities.enums.Unit;
import br.edu.ufrn.cities.properties.CitiesProperties;
import br.edu.ufrn.cities.properties.GeoKeoApiProperties;
import br.edu.ufrn.cities.records.City;
import br.edu.ufrn.cities.records.Geolocation;
import br.edu.ufrn.cities.records.clientai.AskRequest;
import br.edu.ufrn.cities.records.clientai.AskResponse;
import br.edu.ufrn.cities.records.distances.DistanceRequest;
import br.edu.ufrn.cities.records.distances.DistanceResponse;
import br.edu.ufrn.cities.records.geokeo.SearchResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class CitiesService {
    private static final Logger logger = LoggerFactory.getLogger(CitiesService.class);

    @Autowired
    private ClientAIClient clientAIClient;

    @Autowired
    private DistancesClient distancesClient;

    @Autowired
    private GeoKeoApiClient geoKeoApiClient;

    @Autowired
    private CitiesProperties citiesProperties;

    @Autowired
    private GeoKeoApiProperties geoKeoApiProperties;

    public DistanceResponse calculateDistanceFallback(
        DistanceRequest request,
        Throwable t
    ) {
        logger.error(
            "Fallback calculateDistanceFallback() triggered due to exception."
        );
        
        Geolocation origin = request.origin();
        Geolocation destination = request.destination();

        double deltaLat = (
            Math.max(origin.lat(), destination.lat())
            - Math.min(origin.lat(), destination.lat())
        );

        double deltaLon = (
            Math.max(origin.lon(), destination.lon())
            - Math.min(origin.lon(), destination.lon())
        );

        double distance = Math.sqrt(
            Math.pow(
                deltaLat * citiesProperties.getKilometersPerDegree(),
                2
            )
            + Math.pow(
                deltaLon * citiesProperties.getKilometersPerDegree(),
                2
            ));

        return new DistanceResponse(distance, Unit.KILOMETERS);
    }

    @Retry(name = "distances", fallbackMethod = "calculateDistanceFallback")
    @CircuitBreaker(name = "distances", fallbackMethod = "calculateDistanceFallback")
    @Bulkhead(name = "distances", fallbackMethod = "calculateDistanceFallback")
    public DistanceResponse calculateDistance(
        DistanceRequest request
    ) {
        DistanceResponse response = distancesClient.distance(request);

        logger.info(
            "Distance successfully calculated by distances function service: {}",
            response.toString()
        );

        return response;
    }

    private String buildClientAIPrompt(String kind, String city, String country) {
        String prompt = String.format(
            "Assuming I'm a traveler, tell me good {} to visit on {}, {}."
            + " Without formatting. Without markdown. Only pure text."
            + " Not too many words. Not introduction. Splitted by semicolon (;)."
            + " Without whitespace between semicolon and first char of name."
            + " Example output: Some place;Another place;Good place",
            kind, city, country
        );

        return prompt;
    }

    public AskResponse askClientAIFallback(
        AskRequest request,
        Throwable t
    ) {
        logger.error(
            "Fallback calculateDistanceFallback() triggered due to exception."
        );

        return new AskResponse(request.input(), "Unavailable");
    }

    @Retry(name = "clientai", fallbackMethod = "askClientAIFallback")
    @CircuitBreaker(name = "clientai", fallbackMethod = "askClientAIFallback")
    @Bulkhead(name = "clientai", fallbackMethod = "askClientAIFallback")
    public AskResponse askClientAI(AskRequest request) {
        AskResponse response = clientAIClient.ask(request);

        logger.info(
            "Ask successfully processed by clientai service: {}",
            response.toString()
        );

        return response;
    }

    @Retry(name = "geokeo")
    @RateLimiter(name = "geokeo")
    public SearchResponse searchWithGeoKeo(String query) {
        String key = geoKeoApiProperties.getKey();

        SearchResponse response = geoKeoApiClient.search(query, key);

        logger.info(
            "Search successfully retrieved by geokeo mcp service: {}",
            response.toString()
        );

        return response;
    }

}
