package br.edu.ufrn.cities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufrn.cities.records.DiscoverCityRequest;
import br.edu.ufrn.cities.records.DiscoverCityResponse;
import br.edu.ufrn.cities.records.PlanTravelRequest;
import br.edu.ufrn.cities.records.PlanTravelResponse;
import br.edu.ufrn.cities.services.CitiesService;

@RestController
public class CitiesController {
    
    @Autowired
    private CitiesService citiesService;

    @PostMapping("/discover")
    public DiscoverCityResponse discoverCity(@RequestBody DiscoverCityRequest request) {
        return citiesService.discoverCity(request);
    }

    @PostMapping("/travel")
    public PlanTravelResponse planTravel(@RequestBody PlanTravelRequest request) {
        return citiesService.planTravel(request);
    }

    // @PostMapping("/debug/distance")
    // public DistanceResponse distance(@RequestBody DistanceRequest request) {
    //     return citiesService.calculateDistance(request);
    // }

    // @GetMapping("/debu/gask")
    // public AskResponse ask(@RequestParam("input") String input) {
    //     return citiesService.askClientAI(new AskRequest(input));
    // }

    // @GetMapping("/debug/search")
    // public SearchResponse search(@RequestParam("query") String query) {
    //     return citiesService.searchWithGeoKeo(query);
    // }

}
