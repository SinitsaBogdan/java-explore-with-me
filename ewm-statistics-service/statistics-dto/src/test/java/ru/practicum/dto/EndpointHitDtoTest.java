package ru.practicum.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {EndpointHitDtoTest.class})
public class EndpointHitDtoTest {

    @Autowired
    private JacksonTester<EndpointHitDto> endpointHitDtoJacksonTester;

    @Test
    void endpointHitDtoTest() throws IOException {
        LocalDateTime timestamp = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("app")
                .uri("uri")
                .ip("127.0.0.1")
                .endpointTimestamp(timestamp)
                .build();

        JsonContent<EndpointHitDto> jsonContent = endpointHitDtoJacksonTester.write(endpointHitDto);

        assertThat(jsonContent).extractingJsonPathStringValue("$.app").isEqualTo("app");
        assertThat(jsonContent).extractingJsonPathStringValue("$.uri").isEqualTo("uri");
        assertThat(jsonContent).extractingJsonPathStringValue("$.ip").isEqualTo("127.0.0.1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.timestamp").isEqualTo(timestamp.toString());
    }
}