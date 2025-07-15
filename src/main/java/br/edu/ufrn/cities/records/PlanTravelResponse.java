package br.edu.ufrn.cities.records;

public record PlanTravelResponse(
    City from,
    City to,
    double distance
) {}
