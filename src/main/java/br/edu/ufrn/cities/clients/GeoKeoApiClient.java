package br.edu.ufrn.cities.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "geokeo", url = "${geokeo.api.url}")
public interface GeoKeoApiClient {
    @GetMapping("/search.php")
    Object search(@RequestParam("q") String query, @RequestParam("api") String key);


    @GetMapping("/reverse.php")
    Object reverse(@RequestParam("lat") double latitude, @RequestParam("lng") double longitude, @RequestParam("api") String key);
}
