package br.edu.ufrn.cities.records.geokeo;

import java.util.List;

public record SearchResponse(
    List<Result> results,
    String credits,
    String status
) {
    public record Result(String type, Address address_components, Geometry geometry) {}

    public record Address(String name, String country) {}

    public record Geometry(Location location) {}

    public record Location(double lat, double lng) {}
}
