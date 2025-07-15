package br.edu.ufrn.cities.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufrn.cities.clients.ClientAIClient;
import br.edu.ufrn.cities.records.clientai.AskRequest;
import br.edu.ufrn.cities.records.clientai.AskResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ClientAIService {
    private static final Logger logger = LoggerFactory.getLogger(ClientAIService.class);

    @Autowired
    private ClientAIClient clientAIClient;

    public AskResponse askClientAIFallback(
        AskRequest request,
        Throwable t
    ) {
        logger.error(
            "Fallback calculateDistanceFallback() triggered due to exception."
        );

        return new AskResponse(request.input(), "Unavailable");
    }

    @Retry(name = "clientai", fallbackMethod = "askClientAIFallback")
    @CircuitBreaker(name = "clientai", fallbackMethod = "askClientAIFallback")
    public AskResponse askClientAI(AskRequest request) {
        AskResponse response = clientAIClient.ask(request);

        logger.info(
            "Ask successfully processed by clientai service: {}",
            response.toString()
        );

        return response;
    }
}
