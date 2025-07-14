package br.edu.ufrn.cities.records.distances;

import br.edu.ufrn.cities.records.Geolocation;

public record DistanceRequest(Geolocation origin, Geolocation destination) {}
