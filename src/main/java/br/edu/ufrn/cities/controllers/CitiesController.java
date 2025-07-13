package br.edu.ufrn.cities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufrn.cities.clients.DistancesClient;
import br.edu.ufrn.cities.clients.GeoKeoApiClient;
import br.edu.ufrn.cities.properties.GeoKeoApiProperties;
import br.edu.ufrn.cities.records.distances.DistanceRequest;
import br.edu.ufrn.cities.records.distances.DistanceResponse;
import br.edu.ufrn.cities.services.CitiesService;

@RestController
public class CitiesController {
    
    @Autowired
    private CitiesService citiesService;

    // @GetMapping("/discover")
    // public Object discover(@RequestParam("query") String query) {
    //     return geoKeoApiClient.search(query, geoKeoApiProperties.getKey());
    // }

    @PostMapping("/distance")
    public DistanceResponse distance(@RequestBody DistanceRequest request) {
        return citiesService.calculateDistance(request);
    }
}
