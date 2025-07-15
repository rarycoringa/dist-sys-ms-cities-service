package br.edu.ufrn.cities.services;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.cities.records.City;
import br.edu.ufrn.cities.records.DiscoverCityRequest;
import br.edu.ufrn.cities.records.DiscoverCityResponse;
import br.edu.ufrn.cities.records.Geolocation;
import br.edu.ufrn.cities.records.PlanTravelRequest;
import br.edu.ufrn.cities.records.PlanTravelResponse;
import br.edu.ufrn.cities.records.clientai.AskRequest;
import br.edu.ufrn.cities.records.clientai.AskResponse;
import br.edu.ufrn.cities.records.distances.DistanceRequest;
import br.edu.ufrn.cities.records.geokeo.SearchResponse;
import br.edu.ufrn.cities.records.geokeo.SearchResponse.Result;

@Service
public class CitiesService {
    private static final Logger logger = LoggerFactory.getLogger(CitiesService.class);

    @Autowired
    private ClientAIService clientAIService;

    @Autowired
    private DistancesService distancesService;

    @Autowired
    private GeoKeoService geoKeoService;

    private String buildDiscoverCityPrompt(String kind, String city, String country) {
        String prompt = String.format(
            "Assuming I'm a traveler, tell me good %s to visit on %s, %s."
            + " Without formatting. Without markdown. Only pure text."
            + " Not too many words. Not introduction. Splitted by semicolon (;)."
            + " Without whitespace between semicolon and first char of name."
            + " Example output: Some place;Another place;Good place",
            kind, city, country
        );

        return prompt;
    }

    private List<String> splitBySemicolon(String placesString) {
        List<String> places = Arrays.stream(placesString.split(";"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();

        return places;
    }

    public DiscoverCityResponse discoverCity(DiscoverCityRequest request) {
        String restaurantsPrompt = buildDiscoverCityPrompt("restaurants", request.name(), request.country());
        AskResponse restaurantsAskClientAI = clientAIService.askClientAI(new AskRequest(restaurantsPrompt));
        List<String> restaurants = splitBySemicolon(restaurantsAskClientAI.output());

        String barsPrompt = buildDiscoverCityPrompt("bars", request.name(), request.country());
        AskResponse barsAskClientAI = clientAIService.askClientAI(new AskRequest(barsPrompt));
        List<String> bars = splitBySemicolon(barsAskClientAI.output());

        String placesPrompt = buildDiscoverCityPrompt("places", request.name(), request.country());
        AskResponse placesAskClientAI = clientAIService.askClientAI(new AskRequest(placesPrompt));
        List<String> places = splitBySemicolon(placesAskClientAI.output());

        String storesPrompt = buildDiscoverCityPrompt("stores", request.name(), request.country());
        AskResponse storesAskClientAI = clientAIService.askClientAI(new AskRequest(storesPrompt));
        List<String> stores = splitBySemicolon(storesAskClientAI.output());

        DiscoverCityResponse response = new DiscoverCityResponse(restaurants, bars, places, stores);

        return response;
    }

    private String buildSearchCityPrompt(String input) {
        String prompt = String.format("I want the city of %s", input);

        return prompt;
    }

    private City searchCity(String input) {
        String prompt = this.buildSearchCityPrompt(input);

        SearchResponse searchResponse = geoKeoService.search(prompt);

        Result bestResult = searchResponse.results().get(0);

        Geolocation geolocation = new Geolocation(
            bestResult.geometry().location().lat(),
            bestResult.geometry().location().lng()
        );

        City city = new City(
            bestResult.address_components().name(),
            bestResult.address_components().country(),
            geolocation
        );

        return city;
    }

    public PlanTravelResponse planTravel(PlanTravelRequest request) {
        City fromCity = this.searchCity(request.from());
        City toCity = this.searchCity(request.to());

        double distance = distancesService.calculateDistance(
            new DistanceRequest(fromCity.location(), toCity.location())
        ).distance();

        PlanTravelResponse response = new PlanTravelResponse(fromCity, toCity, distance);

        return response;
    }

}
