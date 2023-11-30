package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ViewEndpointDto {

    @NotBlank(message = "Параметр app не должен быть пустым")
    private String app;

    @NotBlank(message = "Параметр uri не должен быть пустым")
    private String uri;

    private long hits;
}