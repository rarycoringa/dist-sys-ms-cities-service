package br.edu.ufrn.cities.records;

import java.util.List;

public record DiscoverCityResponse(
    List<String> restaurants,
    List<String> bars,
    List<String> places,
    List<String> stores
) {}
