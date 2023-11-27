package ru.practicum.client;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewEndpointDto;

import java.util.List;

@Validated
public final class StatisticsClient {

    private final WebClient webClient;

    public StatisticsClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public List<ViewEndpointDto> findStats(String start, String end, List<String> uris, Boolean unique) {
        String urisString = String.join(",", uris);

        return webClient.get()
                .uri("/stats?start={start}&end={end}&uris={uris}&unique={unique}", start, end, urisString, unique)
                .retrieve()
                .bodyToFlux(ViewEndpointDto.class)
                .collectList()
                .block();
    }

    public void saveHit(EndpointHitDto endpointHitDto) {
        webClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(endpointHitDto))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}