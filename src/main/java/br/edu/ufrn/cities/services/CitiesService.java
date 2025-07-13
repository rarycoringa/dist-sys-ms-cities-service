package br.edu.ufrn.cities.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.cities.clients.DistancesClient;
import br.edu.ufrn.cities.enums.Unit;
import br.edu.ufrn.cities.properties.CitiesProperties;
import br.edu.ufrn.cities.records.distances.DistanceRequest;
import br.edu.ufrn.cities.records.distances.DistanceResponse;
import br.edu.ufrn.cities.records.distances.Geolocation;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class CitiesService {
    private static final Logger logger = LoggerFactory.getLogger(CitiesService.class);

    @Autowired
    private DistancesClient distancesClient;

    @Autowired
    private CitiesProperties citiesProperties;

    // @SuppressWarnings("unused")
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
        DistanceResponse distance = distancesClient.distance(request);

        logger.info(
            "Distance successfully calculated by distances function service: "
            + distance.toString());

        return distance;
    }

}
