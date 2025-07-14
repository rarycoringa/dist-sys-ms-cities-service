package br.edu.ufrn.cities.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.edu.ufrn.cities.records.clientai.AskRequest;
import br.edu.ufrn.cities.records.clientai.AskResponse;

@FeignClient(name = "clientai")
public interface ClientAIClient {
    @PostMapping("/ask")
    AskResponse ask(@RequestBody AskRequest request);
}
