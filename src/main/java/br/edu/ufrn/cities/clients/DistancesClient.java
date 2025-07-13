package br.edu.ufrn.cities.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.edu.ufrn.cities.records.distances.DistanceRequest;
import br.edu.ufrn.cities.records.distances.DistanceResponse;

@FeignClient(name = "distances")
public interface DistancesClient {
    @PostMapping("/distance")
    DistanceResponse distance(@RequestBody DistanceRequest request);

    @PostMapping("/convert")
    Object convert();
}
