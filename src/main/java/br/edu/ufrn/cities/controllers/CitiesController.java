package br.edu.ufrn.cities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufrn.cities.records.clientai.AskRequest;
import br.edu.ufrn.cities.records.clientai.AskResponse;
import br.edu.ufrn.cities.records.distances.DistanceRequest;
import br.edu.ufrn.cities.records.distances.DistanceResponse;
import br.edu.ufrn.cities.records.geokeo.SearchResponse;
import br.edu.ufrn.cities.services.CitiesService;

@RestController
public class CitiesController {
    
    @Autowired
    private CitiesService citiesService;

    @PostMapping("/distance")
    public DistanceResponse distance(@RequestBody DistanceRequest request) {
        return citiesService.calculateDistance(request);
    }

    @GetMapping("/ask")
    public AskResponse ask(@RequestParam("input") String input) {
        return citiesService.askClientAI(new AskRequest(input));
    }

    @GetMapping("/search")
    public SearchResponse search(@RequestParam("query") String query) {
        return citiesService.searchWithGeoKeo(query);
    }

}
