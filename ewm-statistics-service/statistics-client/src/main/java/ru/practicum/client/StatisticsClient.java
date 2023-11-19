package ru.practicum.client;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewEndpointDto;

import java.util.List;

public class StatisticsClient {

    private final WebClient webClient;

    public StatisticsClient(String serverUrl) {
        webClient = WebClient.builder().baseUrl(serverUrl).build();
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
                .bodyValue(endpointHitDto)
                .retrieve()
                .bodyToMono(Void.class);
    }
}