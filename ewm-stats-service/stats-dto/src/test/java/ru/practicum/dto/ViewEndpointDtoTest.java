package ru.practicum.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {ViewEndpointDtoTest.class})
public class ViewEndpointDtoTest {

    @Autowired
    private JacksonTester<ViewEndpointDto> viewStatsDtoJacksonTester;

    @Test
    void endpointHitDtoTest() throws IOException {
        ViewEndpointDto viewEndpointDto = ViewEndpointDto.builder()
                .app("app")
                .uri("uri")
                .hits(100L)
                .build();

        JsonContent<ViewEndpointDto> jsonContent = viewStatsDtoJacksonTester.write(viewEndpointDto);

        assertThat(jsonContent).extractingJsonPathStringValue("$.app").isEqualTo("app");
        assertThat(jsonContent).extractingJsonPathStringValue("$.uri").isEqualTo("uri");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.hits").isEqualTo(100);
    }
}