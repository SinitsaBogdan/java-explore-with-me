package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.service.EndpointService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatisticController.class)
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EndpointService service;

    @Test
    void getStats_allValid() throws Exception {
        String start = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String uris = "uri1, uri2";
        String unique = "false";

        when(service.findStats(any(), any(), anyList(), anyBoolean())).thenReturn(null);

        mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", uris)
                        .param("unique", unique))
                .andExpect(status().is2xxSuccessful());

        verify(service, times(1)).findStats(any(), any(), anyList(), anyBoolean());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getStats_onlyRequiredValid() throws Exception {
        String start = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        when(service.findStats(any(), any(), anyList(), anyBoolean())).thenReturn(null);

        mockMvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end))
                .andExpect(status().is2xxSuccessful());

        verify(service, times(1))
                .findStats(LocalDateTime.now().plusDays(1).withNano(0),
                        LocalDateTime.now().plusDays(2).withNano(0),
                        null,
                        false);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getStats_noRequiredParam() throws Exception {
        String start = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        mockMvc.perform(get("/stats")
                        .param("start", start))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(service);
    }

    @Test
    void createEndpointHit_allValid() throws Exception {
        EndpointHitDto endpointHitDto = getValidEndpointHitDto();

        when(service.save(any())).thenReturn(any());

        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(service, times(1)).save(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void createEndpointHit_blankApp() throws Exception {
        EndpointHitDto endpointHitDto = getValidEndpointHitDto();
        endpointHitDto.setIp("     ");

        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(service);
    }

    @Test
    void createEndpointHit_blankUri() throws Exception {
        EndpointHitDto endpointHitDto = getValidEndpointHitDto();
        endpointHitDto.setUri("     ");

        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(service);
    }

    @Test
    void createEndpointHit_blankIp() throws Exception {
        EndpointHitDto endpointHitDto = getValidEndpointHitDto();
        endpointHitDto.setIp("     ");

        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(service);
    }

    private EndpointHitDto getValidEndpointHitDto() {
        return EndpointHitDto.builder()
                .app("app")
                .uri("uri")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();
    }
}